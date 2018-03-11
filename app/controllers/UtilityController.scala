package controllers

import javax.inject.{Inject, Singleton}

import daos._
import models.EntityTable
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}
import sangria.execution._
import sangria.execution.deferred.DeferredResolver
import sangria.marshalling.ResultMarshaller
import sangria.marshalling.playJson._
import sangria.parser.QueryParser
import sangria.renderer.SchemaRenderer
import sangria.schema.Schema
import schemas.SchemaDefinition.personFetcher
import schemas.{Auth, Repository, SchemaDefinition}
import slick.codegen.SourceCodeGenerator
import slick.model.Model
import utils.MyPostgresProfile

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag
import scala.util.{Failure, Success}

/**
  * This controller creates an `Action` that demonstrates how to write
  * simple asynchronous code in a controller. It uses a timer to
  * asynchronously delay sending a response for 1 second.
  *
  * @param cc standard controller components
  */
@Singleton
class UtilityController @Inject()(cc: ControllerComponents,
                                  dbConfigProvider: DatabaseConfigProvider,
                                  userDAO: UserDAO,
                                  roleDAO: RoleDAO,
                                  permissionDAO: PermissionDAO,
                                  userRoleDAO: UserRoleDAO,
                                  userPermissionDAO: UserPermissionDAO,
                                  rolePermissionDAO: RolePermissionDAO)
                                 (implicit exec: ExecutionContext) extends AbstractController(cc) {
  val dbConfig = dbConfigProvider.get[MyPostgresProfile]

  import dbConfig._
  import dbConfig.profile.api._
  val repository = Repository.createDatabase()
  val rejectComplexQuery = QueryReducer.rejectComplexQueries(300, (_: Double, _: Repository) => TooComplexQuery)
  val exceptionHandler = ExceptionHandler {
    case (_: ResultMarshaller, TooComplexQuery) => HandledException("Too complex query. Please reduce the field selection.")
  }
  // fetch data model
  private val modelAction = profile.createModel(Some(profile.defaultTables)) // you can filter specific tables here
  private val modelFuture = db.run(modelAction)
  // customize code generator
  private val codegenFuture = modelFuture.map(model => {
    val newModel = Model(model.tables.filter(_.name.schema.contains("old")), model.options)

    new SourceCodeGenerator(newModel) {
      // override mapped table and class name
      override def entityName: String => String =
        dbTableName => dbTableName.dropRight(1).toLowerCase.toCamelCase

      override def tableName: String => String =
        dbTableName => dbTableName.toLowerCase.toCamelCase

      // add some custom import
      override def code: String =
        "import foo.{MyCustomType,MyCustomTypeMapper}" + "\n" + super.code

      // override table generator
      override def Table = new Table(_) {
        // disable entity class generation and mapping
        override def EntityType = new EntityType {
          override def classEnabled = false
        }

        // override contained column generator
        override def Column = new Column(_) {
          // use the data model member of this column to change the Scala type,
          // e.g. to a custom enum or anything else
          override def rawType: String =
            if (model.name == "SOME_SPECIAL_COLUMN_NAME") "MyCustomType" else super.rawType
        }
      }
    }
  })

  def sql = Action {
    def fn[U, T <: slick.relational.RelationalProfile#Table[U]](tq: Query[T, U, Seq] with TableQuery[T])
                                                               (implicit tag: ClassTag[T]) = {

      val tableName = tq.baseTableRow.tableName
      val isEntityTable = classOf[EntityTable] isAssignableFrom tag.runtimeClass
      val createTrigger =
        s"""create trigger \"${tableName}_update_trigger\" before update on \"$tableName\" """ +
          "for each row execute procedure entity_update_func()"

      List(
        s"# --- !$tableName.sql" :: Nil,
        "# --- !Ups" :: Nil,
        tq.schema.createStatements ++ (if (isEntityTable) createTrigger :: Nil else Nil),
        "# --- !Downs" :: Nil,
        tq.schema.dropStatements
      )
    }

    val sqlStr = List(
      fn(TableQuery[userDAO.UserTable]),
      fn(TableQuery[roleDAO.RoleTable]),
      fn(TableQuery[permissionDAO.PermissionTable]),
      fn(TableQuery[userRoleDAO.UserRoleTable]),
      fn(TableQuery[userPermissionDAO.UserPermissionTable]),
      fn(TableQuery[rolePermissionDAO.RolePermissionTable])
    )
      .map(_.flatten.map(s => if (s.startsWith("# --- !")) s else s + ";").mkString("\n" * 2))
      .mkString("\n" * 4)

    Ok(sqlStr)
  }

  def codegen = Action.async {
    codegenFuture.map({ case codegen =>
      Ok(codegen.code)
    })
  }

  def graphiql = Action {
    Ok(views.html.graphiql())
  }

  def graphql = Action.async(parse.json) { request ⇒
    val queryInfo = request.body.as[JsValue]
    executeGraphQLQuery(SchemaDefinition.schema, queryInfo)
  }

  def executeGraphQLQuery(schema: Schema[Repository, Unit], queryInfo: JsValue) = {
    val JsObject(fields) = queryInfo
    val JsString(query) = fields("query")
    val operation = fields.get("operationName") collect {
      case JsString(op) => op
    }

    val vars = fields.get("variables") match {
      case Some(obj: JsObject) ⇒ obj
      case Some(JsString(s)) if s.trim.nonEmpty => Json.parse(s)
      case _ => JsObject.empty
    }

    QueryParser.parse(query) match {

      // query parsed successfully, time to execute it!
      case Success(queryDocument) =>
        Executor.execute(schema, queryDocument, repository,
          variables = vars,
          operationName = operation,
          queryReducers = rejectComplexQuery :: Nil,
          exceptionHandler = exceptionHandler,
          deferredResolver = DeferredResolver.fetchers(personFetcher)
        )
          .map(Ok(_))
          .recover {
            case error: QueryAnalysisError ⇒ BadRequest(error.resolveError)
            case error: ErrorWithResolver ⇒ InternalServerError(error.resolveError)
          }

      // can't parse GraphQL query, return error
      case Failure(error) =>
        //        BadRequest(Json.stringify(JsObject("error" → JsString(error.getMessage))))
        Future {
          BadRequest(Json.stringify(JsString(error.getMessage)))
        }
    }
  }

  def sangria = Action {
    Ok(SchemaRenderer renderSchema Auth.schema)
  }

  case object TooComplexQuery extends Exception
}
