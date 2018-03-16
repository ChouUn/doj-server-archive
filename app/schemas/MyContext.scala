package schemas

import daos.{RoleDAO, UserDAO, UserRoleDAO}

case class MyContext(userDAO: UserDAO, roleDAO: RoleDAO, userRoleDAO: UserRoleDAO) {

}