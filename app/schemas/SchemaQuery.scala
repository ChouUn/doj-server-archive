package schemas

import models._
import sangria.macros.derive._
import sangria.schema._
import schemas.scalar.DateTime._

object SchemaQuery {

  val UserType: ObjectType[MyContext, User] = deriveObjectType()

  val RoleType: ObjectType[MyContext, Role] = deriveObjectType()

}