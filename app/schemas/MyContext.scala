package schemas

import daos.UserDAO

case class MyContext(userDAO: UserDAO) {

}