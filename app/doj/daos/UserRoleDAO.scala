package doj.daos

import doj.mixins.UserRoleMixin
import doj.models.{Role, User}
import doj.util.MyPostgresProfile
import javax.inject.{Inject, Singleton}
import doj.models.{User, UserRole}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRoleDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
                           (implicit ec: ExecutionContext)
  extends UserRoleMixin
    with HasDatabaseConfigProvider[MyPostgresProfile] {

  import profile.api._

  def getRolesByUserIds(userIds: Seq[Int]): Future[Seq[(Seq[UserRole], Role)]] =
    db.run {
      UserRoles
        .filter(_.userId inSet userIds)
        .join(Roles).on(_.roleId === _.id)
        .result
    }.map(result => {
      result.groupBy(_._2.id).toSeq.map({
        case (_, pairs) => pairs.map(_._1) -> pairs.head._2
      })
    })

  def getUsersByRoleIds(roleIds: Seq[Int]): Future[Seq[(Seq[UserRole], User)]] =
    db.run {
      UserRoles
        .filter(_.roleId inSet roleIds)
        .join(Users).on(_.userId === _.id)
        .result
    }.map(result => {
      result.groupBy(_._2.id).toSeq.map({
        case (_, pairs) => pairs.map(_._1) -> pairs.head._2
      })
    })

}
