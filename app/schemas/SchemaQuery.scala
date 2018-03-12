package schemas

import models.UserMixin.User
import sangria.macros.derive._
import sangria.schema._
import schemas.scalar.DateTime._

object SchemaQuery {

  val UserType: ObjectType[MyContext, User] = deriveObjectType()

}