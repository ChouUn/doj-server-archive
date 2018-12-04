package doj.sangria

import doj.daos.{RoleDAO, UserDAO, UserRoleDAO}

case class MyContext(userDAO: UserDAO, roleDAO: RoleDAO, userRoleDAO: UserRoleDAO) {

}