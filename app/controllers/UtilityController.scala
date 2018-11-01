package controllers

import daos._
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents, Request, Result}
import sangria.marshalling.playJson._
import sangria.renderer.SchemaRenderer
import sangria.schema.Schema
import gql.{MyContext, Utility}
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

  import dbConfig.profile
  import profile.api.{Query, TableQuery}

  private val codegenFuture = {
    import slick.codegen.SourceCodeGenerator
    import slick.model.Model

    val modelAction = profile.createModel(Some(profile.defaultTables)) // you can filter specific tables here
    val modelFuture = dbConfig.db.run(modelAction)

    // customize code generator
    modelFuture.map(model => {
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
  }

  def sql = Action {
    import slick.relational.RelationalProfile

    def generateMigrationSQL[U, T <: RelationalProfile#Table[U]](tableQuery: Query[T, U, Seq] with TableQuery[T])
                                                                (implicit tag: ClassTag[T]) = {
      import mixins.EntityTable
      import profile.api.tableQueryToTableQueryExtensionMethods

      val tableName = tableQuery.baseTableRow.tableName
      val isEntityTable = classOf[EntityTable] isAssignableFrom tag.runtimeClass
      val createTrigger =
        s"""create trigger \"${tableName}_update_trigger\" before update on \"$tableName\" """ +
          "for each row execute procedure entity_update_func()"

      List(
        s"# --- !$tableName.sql" :: Nil,
        "# --- !Ups" :: Nil,
        tableQuery.schema.createStatements ++ (if (isEntityTable) createTrigger :: Nil else Nil),
        "# --- !Downs" :: Nil,
        tableQuery.schema.dropStatements
      )
    }

    val sqlStr = List(
      generateMigrationSQL(TableQuery[userDAO.UserTable]),
      generateMigrationSQL(TableQuery[roleDAO.RoleTable]),
      generateMigrationSQL(TableQuery[permissionDAO.PermissionTable]),
      generateMigrationSQL(TableQuery[userRoleDAO.UserRoleTable]),
      generateMigrationSQL(TableQuery[userPermissionDAO.UserPermissionTable]),
      generateMigrationSQL(TableQuery[rolePermissionDAO.RolePermissionTable])
    )
      .map(_.flatten.map(s => if (s.startsWith("# --- !")) s else s + ";").mkString("\n" * 2))
      .mkString("\n" * 4)

    Ok(sqlStr)
  }

  def codegen = Action.async {
    codegenFuture
      .map(codegen => Ok(codegen.code))
  }

  def graphiql = Action {
    Ok(views.html.graphiql())
  }

  def graphql = Action.async(parse.json) { request: Request[JsValue] =>
    val queryInfo = request.body.as[JsValue]
    executeGraphQLQuery(Utility.schema, queryInfo)
  }

  private def executeGraphQLQuery(schema: Schema[MyContext, Unit], queryInfo: JsValue): Future[Result] = {
    import sangria.execution._
    import sangria.parser.QueryParser

    val JsObject(fields) = queryInfo
    val JsString(query) = fields("query")
    val operation = fields.get("operationName") collect {
      case JsString(op) => op
    }

    val vars = fields.get("variables") match {
      case Some(obj: JsObject) => obj
      case Some(JsString(s)) if s.trim.nonEmpty => Json.parse(s)
      case _ => JsObject.empty
    }

    QueryParser.parse(query) match {
      // query parsed successfully, time to execute it!
      case Success(queryDocument) =>
        Executor
          .execute(
            schema,
            queryDocument,
            variables = vars,
            userContext = MyContext(userDAO, roleDAO, userRoleDAO),
            operationName = operation,
            queryReducers = Utility.rejectComplexQuery :: Nil,
            exceptionHandler = Utility.ErrorHandler,
            deferredResolver = Utility.Resolver
          )
          .map(Ok(_))
          .recover {
            case error: QueryAnalysisError => BadRequest(error.resolveError)
            case error: ErrorWithResolver => InternalServerError(error.resolveError)
          }

      // can't parse GraphQL query, return error
      case Failure(error) =>
        Future {
          BadRequest(Json.stringify(JsString(error.getMessage)))
        }
    }
  }

  def schema = Action {
    Ok(SchemaRenderer renderSchema Utility.schema)
  }

}
