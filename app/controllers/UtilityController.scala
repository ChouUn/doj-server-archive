package controllers

import doj.daos._
import doj.sangria.{MyContext, Utility}
import doj.util.MyPostgresProfile
import io.circe.parser.{parse => parseJson}
import io.circe.syntax._
import io.circe.{Json, JsonObject}
import io.circe.generic.auto._
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.circe.Circe
import play.api.libs.json._
import play.api.mvc._
import sangria.renderer.SchemaRenderer
import sangria.schema.Schema

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
                                 (implicit exec: ExecutionContext)
  extends AbstractController(cc)
    with Circe {
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

  def sql: Action[AnyContent] = Action {
    import slick.relational.RelationalProfile

    def generateMigrationSQL[U, T <: RelationalProfile#Table[U]](tableQuery: Query[T, U, Seq] with TableQuery[T])
                                                                (implicit tag: ClassTag[T]) = {
      import doj.mixins.EntityTable
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

  def codegen: Action[AnyContent] = Action.async {
    codegenFuture
      .map(codegen => Ok(codegen.code))
  }

  def graphiql: Action[AnyContent] = Action {
    Ok(views.html.graphiql())
  }

  def graphql: Action[Json] = Action.async(circe.json) { implicit request: Request[Json] =>
    val queryInfo: Json = request.body
    executeGraphQLQuery(Utility.schema, queryInfo)
  }

  private def executeGraphQLQuery(schema: Schema[MyContext, Unit], queryInfo: Json): Future[Result] = {
    import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
    import sangria.marshalling.circe._
    import sangria.parser.QueryParser

    case class QueryInfo (query: Option[String], operationName: Option[String], variables: Option[JsonObject])
    val qi: Option[QueryInfo] = queryInfo.as[QueryInfo].toOption

    val query: String = qi.flatMap(_.query).getOrElse("")
    val operation: Option[String] = qi.flatMap(_.operationName)
    val variables = qi.flatMap(_.variables).getOrElse(JsonObject.empty).asJson

    QueryParser.parse(query) match {
      // query parsed successfully, time to execute it!
      case Success(queryDocument) =>
        Executor
          .execute(
            schema,
            queryDocument,
            variables = variables,
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
          BadRequest(JsString(error.getMessage))
        }
    }
  }

  def schema: Action[AnyContent] = Action {
    Ok(SchemaRenderer renderSchema Utility.schema)
  }

}
