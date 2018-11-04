package codegen;

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import foo.{MyCustomType,MyCustomTypeMapper}
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(AuthGroup.schema, AuthGroupPermissions.schema, AuthPermission.schema, AuthUser.schema, AuthUserGroups.schema, AuthUserUserPermissions.schema, DjangoAdminLog.schema, DjangoContentType.schema, DjangoMigrations.schema, DjangoSession.schema, ProblemProblem.schema, ProblemProblemtag.schema, ProblemProblemTags.schema, ProfileProfile.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Row type of table AuthGroup */
  type AuthGrou = (Int, String)
  /** Constructor for AuthGrou providing default values if available in the database schema. */
  def AuthGrou(id: Int, name: String): AuthGrou = {
    (id, name)
  }
  /** GetResult implicit for fetching AuthGrou objects using plain SQL queries */
  implicit def GetResultAuthGrou(implicit e0: GR[Int], e1: GR[String]): GR[AuthGrou] = GR{
    prs => import prs._
    AuthGrou.tupled((<<[Int], <<[String]))
  }
  /** Table description of table auth_group. Objects of this class serve as prototypes for rows in queries. */
  class AuthGroup(_tableTag: Tag) extends profile.api.Table[AuthGrou](_tableTag, Some("old"), "auth_group") {
    def * = (id, name) <> (AuthGrou.tupled, AuthGrou.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name)).shaped.<>({r=>import r._; _1.map(_=> AuthGrou.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(80,true) */
    val name: Rep[String] = column[String]("name", O.Length(80,varying=true))

    /** Index over (name) (database name auth_group_name_a6ea08ec_like) */
    val index1 = index("auth_group_name_a6ea08ec_like", name)
    /** Uniqueness Index over (name) (database name auth_group_name_key) */
    val index2 = index("auth_group_name_key", name, unique=true)
  }
  /** Collection-like TableQuery object for table AuthGroup */
  lazy val AuthGroup = new TableQuery(tag => new AuthGroup(tag))

  /** Row type of table AuthGroupPermissions */
  type AuthGroupPermission = (Int, Int, Int)
  /** Constructor for AuthGroupPermission providing default values if available in the database schema. */
  def AuthGroupPermission(id: Int, groupId: Int, permissionId: Int): AuthGroupPermission = {
    (id, groupId, permissionId)
  }
  /** GetResult implicit for fetching AuthGroupPermission objects using plain SQL queries */
  implicit def GetResultAuthGroupPermission(implicit e0: GR[Int]): GR[AuthGroupPermission] = GR{
    prs => import prs._
    AuthGroupPermission.tupled((<<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table auth_group_permissions. Objects of this class serve as prototypes for rows in queries. */
  class AuthGroupPermissions(_tableTag: Tag) extends profile.api.Table[AuthGroupPermission](_tableTag, Some("old"), "auth_group_permissions") {
    def * = (id, groupId, permissionId) <> (AuthGroupPermission.tupled, AuthGroupPermission.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(groupId), Rep.Some(permissionId)).shaped.<>({r=>import r._; _1.map(_=> AuthGroupPermission.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column group_id SqlType(int4) */
    val groupId: Rep[Int] = column[Int]("group_id")
    /** Database column permission_id SqlType(int4) */
    val permissionId: Rep[Int] = column[Int]("permission_id")

    /** Foreign key referencing AuthGroup (database name auth_group_permissions_group_id_b120cbf9_fk_auth_group_id) */
    lazy val authGroupFk = foreignKey("auth_group_permissions_group_id_b120cbf9_fk_auth_group_id", groupId, AuthGroup)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing AuthPermission (database name auth_group_permissio_permission_id_84c5c92e_fk_auth_perm) */
    lazy val authPermissionFk = foreignKey("auth_group_permissio_permission_id_84c5c92e_fk_auth_perm", permissionId, AuthPermission)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (groupId,permissionId) (database name auth_group_permissions_group_id_permission_id_0cd325b0_uniq) */
    val index1 = index("auth_group_permissions_group_id_permission_id_0cd325b0_uniq", (groupId, permissionId), unique=true)
  }
  /** Collection-like TableQuery object for table AuthGroupPermissions */
  lazy val AuthGroupPermissions = new TableQuery(tag => new AuthGroupPermissions(tag))

  /** Row type of table AuthPermission */
  type AuthPermissio = (Int, String, Int, String)
  /** Constructor for AuthPermissio providing default values if available in the database schema. */
  def AuthPermissio(id: Int, name: String, contentTypeId: Int, codename: String): AuthPermissio = {
    (id, name, contentTypeId, codename)
  }
  /** GetResult implicit for fetching AuthPermissio objects using plain SQL queries */
  implicit def GetResultAuthPermissio(implicit e0: GR[Int], e1: GR[String]): GR[AuthPermissio] = GR{
    prs => import prs._
    AuthPermissio.tupled((<<[Int], <<[String], <<[Int], <<[String]))
  }
  /** Table description of table auth_permission. Objects of this class serve as prototypes for rows in queries. */
  class AuthPermission(_tableTag: Tag) extends profile.api.Table[AuthPermissio](_tableTag, Some("old"), "auth_permission") {
    def * = (id, name, contentTypeId, codename) <> (AuthPermissio.tupled, AuthPermissio.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(contentTypeId), Rep.Some(codename)).shaped.<>({r=>import r._; _1.map(_=> AuthPermissio.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column content_type_id SqlType(int4) */
    val contentTypeId: Rep[Int] = column[Int]("content_type_id")
    /** Database column codename SqlType(varchar), Length(100,true) */
    val codename: Rep[String] = column[String]("codename", O.Length(100,varying=true))

    /** Foreign key referencing DjangoContentType (database name auth_permission_content_type_id_2f476e4b_fk_django_co) */
    lazy val djangoContentTypeFk = foreignKey("auth_permission_content_type_id_2f476e4b_fk_django_co", contentTypeId, DjangoContentType)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (contentTypeId,codename) (database name auth_permission_content_type_id_codename_01ab375a_uniq) */
    val index1 = index("auth_permission_content_type_id_codename_01ab375a_uniq", (contentTypeId, codename), unique=true)
  }
  /** Collection-like TableQuery object for table AuthPermission */
  lazy val AuthPermission = new TableQuery(tag => new AuthPermission(tag))

  /** Row type of table AuthUser */
  type AuthUse = (Int, String, Option[java.sql.Timestamp], Boolean, String, String, String, String, Boolean, Boolean, java.sql.Timestamp)
  /** Constructor for AuthUse providing default values if available in the database schema. */
  def AuthUse(id: Int, password: String, lastLogin: Option[java.sql.Timestamp] = None, isSuperuser: Boolean, username: String, firstName: String, lastName: String, email: String, isStaff: Boolean, isActive: Boolean, dateJoined: java.sql.Timestamp): AuthUse = {
    (id, password, lastLogin, isSuperuser, username, firstName, lastName, email, isStaff, isActive, dateJoined)
  }
  /** GetResult implicit for fetching AuthUse objects using plain SQL queries */
  implicit def GetResultAuthUse(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[java.sql.Timestamp]], e3: GR[Boolean], e4: GR[java.sql.Timestamp]): GR[AuthUse] = GR{
    prs => import prs._
    AuthUse.tupled((<<[Int], <<[String], <<?[java.sql.Timestamp], <<[Boolean], <<[String], <<[String], <<[String], <<[String], <<[Boolean], <<[Boolean], <<[java.sql.Timestamp]))
  }
  /** Table description of table auth_user. Objects of this class serve as prototypes for rows in queries. */
  class AuthUser(_tableTag: Tag) extends profile.api.Table[AuthUse](_tableTag, Some("old"), "auth_user") {
    def * = (id, password, lastLogin, isSuperuser, username, firstName, lastName, email, isStaff, isActive, dateJoined) <> (AuthUse.tupled, AuthUse.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(password), lastLogin, Rep.Some(isSuperuser), Rep.Some(username), Rep.Some(firstName), Rep.Some(lastName), Rep.Some(email), Rep.Some(isStaff), Rep.Some(isActive), Rep.Some(dateJoined)).shaped.<>({r=>import r._; _1.map(_=> AuthUse.tupled((_1.get, _2.get, _3, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column password SqlType(varchar), Length(128,true) */
    val password: Rep[String] = column[String]("password", O.Length(128,varying=true))
    /** Database column last_login SqlType(timestamptz), Default(None) */
    val lastLogin: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("last_login", O.Default(None))
    /** Database column is_superuser SqlType(bool) */
    val isSuperuser: Rep[Boolean] = column[Boolean]("is_superuser")
    /** Database column username SqlType(varchar), Length(150,true) */
    val username: Rep[String] = column[String]("username", O.Length(150,varying=true))
    /** Database column first_name SqlType(varchar), Length(30,true) */
    val firstName: Rep[String] = column[String]("first_name", O.Length(30,varying=true))
    /** Database column last_name SqlType(varchar), Length(30,true) */
    val lastName: Rep[String] = column[String]("last_name", O.Length(30,varying=true))
    /** Database column email SqlType(varchar), Length(254,true) */
    val email: Rep[String] = column[String]("email", O.Length(254,varying=true))
    /** Database column is_staff SqlType(bool) */
    val isStaff: Rep[Boolean] = column[Boolean]("is_staff")
    /** Database column is_active SqlType(bool) */
    val isActive: Rep[Boolean] = column[Boolean]("is_active")
    /** Database column date_joined SqlType(timestamptz) */
    val dateJoined: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("date_joined")

    /** Index over (username) (database name auth_user_username_6821ab7c_like) */
    val index1 = index("auth_user_username_6821ab7c_like", username)
    /** Uniqueness Index over (username) (database name auth_user_username_key) */
    val index2 = index("auth_user_username_key", username, unique=true)
  }
  /** Collection-like TableQuery object for table AuthUser */
  lazy val AuthUser = new TableQuery(tag => new AuthUser(tag))

  /** Row type of table AuthUserGroups */
  type AuthUserGroup = (Int, Int, Int)
  /** Constructor for AuthUserGroup providing default values if available in the database schema. */
  def AuthUserGroup(id: Int, userId: Int, groupId: Int): AuthUserGroup = {
    (id, userId, groupId)
  }
  /** GetResult implicit for fetching AuthUserGroup objects using plain SQL queries */
  implicit def GetResultAuthUserGroup(implicit e0: GR[Int]): GR[AuthUserGroup] = GR{
    prs => import prs._
    AuthUserGroup.tupled((<<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table auth_user_groups. Objects of this class serve as prototypes for rows in queries. */
  class AuthUserGroups(_tableTag: Tag) extends profile.api.Table[AuthUserGroup](_tableTag, Some("old"), "auth_user_groups") {
    def * = (id, userId, groupId) <> (AuthUserGroup.tupled, AuthUserGroup.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(userId), Rep.Some(groupId)).shaped.<>({r=>import r._; _1.map(_=> AuthUserGroup.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(int4) */
    val userId: Rep[Int] = column[Int]("user_id")
    /** Database column group_id SqlType(int4) */
    val groupId: Rep[Int] = column[Int]("group_id")

    /** Foreign key referencing AuthGroup (database name auth_user_groups_group_id_97559544_fk_auth_group_id) */
    lazy val authGroupFk = foreignKey("auth_user_groups_group_id_97559544_fk_auth_group_id", groupId, AuthGroup)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing AuthUser (database name auth_user_groups_user_id_6a12ed8b_fk_auth_user_id) */
    lazy val authUserFk = foreignKey("auth_user_groups_user_id_6a12ed8b_fk_auth_user_id", userId, AuthUser)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (userId,groupId) (database name auth_user_groups_user_id_group_id_94350c0c_uniq) */
    val index1 = index("auth_user_groups_user_id_group_id_94350c0c_uniq", (userId, groupId), unique=true)
  }
  /** Collection-like TableQuery object for table AuthUserGroups */
  lazy val AuthUserGroups = new TableQuery(tag => new AuthUserGroups(tag))

  /** Row type of table AuthUserUserPermissions */
  type AuthUserUserPermission = (Int, Int, Int)
  /** Constructor for AuthUserUserPermission providing default values if available in the database schema. */
  def AuthUserUserPermission(id: Int, userId: Int, permissionId: Int): AuthUserUserPermission = {
    (id, userId, permissionId)
  }
  /** GetResult implicit for fetching AuthUserUserPermission objects using plain SQL queries */
  implicit def GetResultAuthUserUserPermission(implicit e0: GR[Int]): GR[AuthUserUserPermission] = GR{
    prs => import prs._
    AuthUserUserPermission.tupled((<<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table auth_user_user_permissions. Objects of this class serve as prototypes for rows in queries. */
  class AuthUserUserPermissions(_tableTag: Tag) extends profile.api.Table[AuthUserUserPermission](_tableTag, Some("old"), "auth_user_user_permissions") {
    def * = (id, userId, permissionId) <> (AuthUserUserPermission.tupled, AuthUserUserPermission.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(userId), Rep.Some(permissionId)).shaped.<>({r=>import r._; _1.map(_=> AuthUserUserPermission.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(int4) */
    val userId: Rep[Int] = column[Int]("user_id")
    /** Database column permission_id SqlType(int4) */
    val permissionId: Rep[Int] = column[Int]("permission_id")

    /** Foreign key referencing AuthPermission (database name auth_user_user_permi_permission_id_1fbb5f2c_fk_auth_perm) */
    lazy val authPermissionFk = foreignKey("auth_user_user_permi_permission_id_1fbb5f2c_fk_auth_perm", permissionId, AuthPermission)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing AuthUser (database name auth_user_user_permissions_user_id_a95ead1b_fk_auth_user_id) */
    lazy val authUserFk = foreignKey("auth_user_user_permissions_user_id_a95ead1b_fk_auth_user_id", userId, AuthUser)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (userId,permissionId) (database name auth_user_user_permissions_user_id_permission_id_14a6b632_uniq) */
    val index1 = index("auth_user_user_permissions_user_id_permission_id_14a6b632_uniq", (userId, permissionId), unique=true)
  }
  /** Collection-like TableQuery object for table AuthUserUserPermissions */
  lazy val AuthUserUserPermissions = new TableQuery(tag => new AuthUserUserPermissions(tag))

  /** Row type of table DjangoAdminLog */
  type DjangoAdminLo = (Int, java.sql.Timestamp, Option[String], String, Short, String, Option[Int], Int)
  /** Constructor for DjangoAdminLo providing default values if available in the database schema. */
  def DjangoAdminLo(id: Int, actionTime: java.sql.Timestamp, objectId: Option[String] = None, objectRepr: String, actionFlag: Short, changeMessage: String, contentTypeId: Option[Int] = None, userId: Int): DjangoAdminLo = {
    (id, actionTime, objectId, objectRepr, actionFlag, changeMessage, contentTypeId, userId)
  }
  /** GetResult implicit for fetching DjangoAdminLo objects using plain SQL queries */
  implicit def GetResultDjangoAdminLo(implicit e0: GR[Int], e1: GR[java.sql.Timestamp], e2: GR[Option[String]], e3: GR[String], e4: GR[Short], e5: GR[Option[Int]]): GR[DjangoAdminLo] = GR{
    prs => import prs._
    DjangoAdminLo.tupled((<<[Int], <<[java.sql.Timestamp], <<?[String], <<[String], <<[Short], <<[String], <<?[Int], <<[Int]))
  }
  /** Table description of table django_admin_log. Objects of this class serve as prototypes for rows in queries. */
  class DjangoAdminLog(_tableTag: Tag) extends profile.api.Table[DjangoAdminLo](_tableTag, Some("old"), "django_admin_log") {
    def * = (id, actionTime, objectId, objectRepr, actionFlag, changeMessage, contentTypeId, userId) <> (DjangoAdminLo.tupled, DjangoAdminLo.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(actionTime), objectId, Rep.Some(objectRepr), Rep.Some(actionFlag), Rep.Some(changeMessage), contentTypeId, Rep.Some(userId)).shaped.<>({r=>import r._; _1.map(_=> DjangoAdminLo.tupled((_1.get, _2.get, _3, _4.get, _5.get, _6.get, _7, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column action_time SqlType(timestamptz) */
    val actionTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("action_time")
    /** Database column object_id SqlType(text), Default(None) */
    val objectId: Rep[Option[String]] = column[Option[String]]("object_id", O.Default(None))
    /** Database column object_repr SqlType(varchar), Length(200,true) */
    val objectRepr: Rep[String] = column[String]("object_repr", O.Length(200,varying=true))
    /** Database column action_flag SqlType(int2) */
    val actionFlag: Rep[Short] = column[Short]("action_flag")
    /** Database column change_message SqlType(text) */
    val changeMessage: Rep[String] = column[String]("change_message")
    /** Database column content_type_id SqlType(int4), Default(None) */
    val contentTypeId: Rep[Option[Int]] = column[Option[Int]]("content_type_id", O.Default(None))
    /** Database column user_id SqlType(int4) */
    val userId: Rep[Int] = column[Int]("user_id")

    /** Foreign key referencing AuthUser (database name django_admin_log_user_id_c564eba6_fk_auth_user_id) */
    lazy val authUserFk = foreignKey("django_admin_log_user_id_c564eba6_fk_auth_user_id", userId, AuthUser)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing DjangoContentType (database name django_admin_log_content_type_id_c4bce8eb_fk_django_co) */
    lazy val djangoContentTypeFk = foreignKey("django_admin_log_content_type_id_c4bce8eb_fk_django_co", contentTypeId, DjangoContentType)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table DjangoAdminLog */
  lazy val DjangoAdminLog = new TableQuery(tag => new DjangoAdminLog(tag))

  /** Row type of table DjangoContentType */
  type DjangoContentTyp = (Int, String, String)
  /** Constructor for DjangoContentTyp providing default values if available in the database schema. */
  def DjangoContentTyp(id: Int, appLabel: String, model: String): DjangoContentTyp = {
    (id, appLabel, model)
  }
  /** GetResult implicit for fetching DjangoContentTyp objects using plain SQL queries */
  implicit def GetResultDjangoContentTyp(implicit e0: GR[Int], e1: GR[String]): GR[DjangoContentTyp] = GR{
    prs => import prs._
    DjangoContentTyp.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table django_content_type. Objects of this class serve as prototypes for rows in queries. */
  class DjangoContentType(_tableTag: Tag) extends profile.api.Table[DjangoContentTyp](_tableTag, Some("old"), "django_content_type") {
    def * = (id, appLabel, model) <> (DjangoContentTyp.tupled, DjangoContentTyp.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(appLabel), Rep.Some(model)).shaped.<>({r=>import r._; _1.map(_=> DjangoContentTyp.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column app_label SqlType(varchar), Length(100,true) */
    val appLabel: Rep[String] = column[String]("app_label", O.Length(100,varying=true))
    /** Database column model SqlType(varchar), Length(100,true) */
    val model: Rep[String] = column[String]("model", O.Length(100,varying=true))

    /** Uniqueness Index over (appLabel,model) (database name django_content_type_app_label_model_76bd3d3b_uniq) */
    val index1 = index("django_content_type_app_label_model_76bd3d3b_uniq", (appLabel, model), unique=true)
  }
  /** Collection-like TableQuery object for table DjangoContentType */
  lazy val DjangoContentType = new TableQuery(tag => new DjangoContentType(tag))

  /** Row type of table DjangoMigrations */
  type DjangoMigration = (Int, String, String, java.sql.Timestamp)
  /** Constructor for DjangoMigration providing default values if available in the database schema. */
  def DjangoMigration(id: Int, app: String, name: String, applied: java.sql.Timestamp): DjangoMigration = {
    (id, app, name, applied)
  }
  /** GetResult implicit for fetching DjangoMigration objects using plain SQL queries */
  implicit def GetResultDjangoMigration(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[DjangoMigration] = GR{
    prs => import prs._
    DjangoMigration.tupled((<<[Int], <<[String], <<[String], <<[java.sql.Timestamp]))
  }
  /** Table description of table django_migrations. Objects of this class serve as prototypes for rows in queries. */
  class DjangoMigrations(_tableTag: Tag) extends profile.api.Table[DjangoMigration](_tableTag, Some("old"), "django_migrations") {
    def * = (id, app, name, applied) <> (DjangoMigration.tupled, DjangoMigration.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(app), Rep.Some(name), Rep.Some(applied)).shaped.<>({r=>import r._; _1.map(_=> DjangoMigration.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column app SqlType(varchar), Length(255,true) */
    val app: Rep[String] = column[String]("app", O.Length(255,varying=true))
    /** Database column name SqlType(varchar), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column applied SqlType(timestamptz) */
    val applied: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("applied")
  }
  /** Collection-like TableQuery object for table DjangoMigrations */
  lazy val DjangoMigrations = new TableQuery(tag => new DjangoMigrations(tag))

  /** Row type of table DjangoSession */
  type DjangoSessio = (String, String, java.sql.Timestamp)
  /** Constructor for DjangoSessio providing default values if available in the database schema. */
  def DjangoSessio(sessionKey: String, sessionData: String, expireDate: java.sql.Timestamp): DjangoSessio = {
    (sessionKey, sessionData, expireDate)
  }
  /** GetResult implicit for fetching DjangoSessio objects using plain SQL queries */
  implicit def GetResultDjangoSessio(implicit e0: GR[String], e1: GR[java.sql.Timestamp]): GR[DjangoSessio] = GR{
    prs => import prs._
    DjangoSessio.tupled((<<[String], <<[String], <<[java.sql.Timestamp]))
  }
  /** Table description of table django_session. Objects of this class serve as prototypes for rows in queries. */
  class DjangoSession(_tableTag: Tag) extends profile.api.Table[DjangoSessio](_tableTag, Some("old"), "django_session") {
    def * = (sessionKey, sessionData, expireDate) <> (DjangoSessio.tupled, DjangoSessio.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(sessionKey), Rep.Some(sessionData), Rep.Some(expireDate)).shaped.<>({r=>import r._; _1.map(_=> DjangoSessio.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column session_key SqlType(varchar), PrimaryKey, Length(40,true) */
    val sessionKey: Rep[String] = column[String]("session_key", O.PrimaryKey, O.Length(40,varying=true))
    /** Database column session_data SqlType(text) */
    val sessionData: Rep[String] = column[String]("session_data")
    /** Database column expire_date SqlType(timestamptz) */
    val expireDate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("expire_date")

    /** Index over (expireDate) (database name django_session_expire_date_a5c62663) */
    val index1 = index("django_session_expire_date_a5c62663", expireDate)
  }
  /** Collection-like TableQuery object for table DjangoSession */
  lazy val DjangoSession = new TableQuery(tag => new DjangoSession(tag))

  /** Row type of table ProblemProblem */
  type ProblemProble = (Int, java.sql.Timestamp, java.sql.Timestamp, Option[String], String, Option[String], Boolean, Int, Int, Int, String, String, String, String, Option[String], Option[String], Option[String], Option[Int])
  /** Constructor for ProblemProble providing default values if available in the database schema. */
  def ProblemProble(id: Int, createdAt: java.sql.Timestamp, updatedAt: java.sql.Timestamp, voj: Option[String] = None, vid: String, author: Option[String] = None, `private`: Boolean, `type`: Int, timeLimit: Int, memoryLimit: Int, title: String, description: String, inputFormat: String, outputFormat: String, example: Option[String] = None, subtaskAndHint: Option[String] = None, source: Option[String] = None, providerId: Option[Int] = None): ProblemProble = {
    (id, createdAt, updatedAt, voj, vid, author, `private`, `type`, timeLimit, memoryLimit, title, description, inputFormat, outputFormat, example, subtaskAndHint, source, providerId)
  }
  /** GetResult implicit for fetching ProblemProble objects using plain SQL queries */
  implicit def GetResultProblemProble(implicit e0: GR[Int], e1: GR[java.sql.Timestamp], e2: GR[Option[String]], e3: GR[String], e4: GR[Boolean], e5: GR[Option[Int]]): GR[ProblemProble] = GR{
    prs => import prs._
    ProblemProble.tupled((<<[Int], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<?[String], <<[String], <<?[String], <<[Boolean], <<[Int], <<[Int], <<[Int], <<[String], <<[String], <<[String], <<[String], <<?[String], <<?[String], <<?[String], <<?[Int]))
  }
  /** Table description of table problem_problem. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: private, type */
  class ProblemProblem(_tableTag: Tag) extends profile.api.Table[ProblemProble](_tableTag, Some("old"), "problem_problem") {
    def * = (id, createdAt, updatedAt, voj, vid, author, `private`, `type`, timeLimit, memoryLimit, title, description, inputFormat, outputFormat, example, subtaskAndHint, source, providerId) <> (ProblemProble.tupled, ProblemProble.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(createdAt), Rep.Some(updatedAt), voj, Rep.Some(vid), author, Rep.Some(`private`), Rep.Some(`type`), Rep.Some(timeLimit), Rep.Some(memoryLimit), Rep.Some(title), Rep.Some(description), Rep.Some(inputFormat), Rep.Some(outputFormat), example, subtaskAndHint, source, providerId).shaped.<>({r=>import r._; _1.map(_=> ProblemProble.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get, _13.get, _14.get, _15, _16, _17, _18)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column created_at SqlType(timestamptz) */
    val createdAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created_at")
    /** Database column updated_at SqlType(timestamptz) */
    val updatedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("updated_at")
    /** Database column voj SqlType(varchar), Length(16,true), Default(None) */
    val voj: Rep[Option[String]] = column[Option[String]]("voj", O.Length(16,varying=true), O.Default(None))
    /** Database column vid SqlType(varchar), Length(16,true) */
    val vid: Rep[String] = column[String]("vid", O.Length(16,varying=true))
    /** Database column author SqlType(varchar), Length(32,true), Default(None) */
    val author: Rep[Option[String]] = column[Option[String]]("author", O.Length(32,varying=true), O.Default(None))
    /** Database column private SqlType(bool)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `private`: Rep[Boolean] = column[Boolean]("private")
    /** Database column type SqlType(int4)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[Int] = column[Int]("type")
    /** Database column time_limit SqlType(int4) */
    val timeLimit: Rep[Int] = column[Int]("time_limit")
    /** Database column memory_limit SqlType(int4) */
    val memoryLimit: Rep[Int] = column[Int]("memory_limit")
    /** Database column title SqlType(varchar), Length(128,true) */
    val title: Rep[String] = column[String]("title", O.Length(128,varying=true))
    /** Database column description SqlType(text) */
    val description: Rep[String] = column[String]("description")
    /** Database column input_format SqlType(text) */
    val inputFormat: Rep[String] = column[String]("input_format")
    /** Database column output_format SqlType(text) */
    val outputFormat: Rep[String] = column[String]("output_format")
    /** Database column example SqlType(text), Default(None) */
    val example: Rep[Option[String]] = column[Option[String]]("example", O.Default(None))
    /** Database column subtask_and_hint SqlType(text), Default(None) */
    val subtaskAndHint: Rep[Option[String]] = column[Option[String]]("subtask_and_hint", O.Default(None))
    /** Database column source SqlType(varchar), Length(128,true), Default(None) */
    val source: Rep[Option[String]] = column[Option[String]]("source", O.Length(128,varying=true), O.Default(None))
    /** Database column provider_id SqlType(int4), Default(None) */
    val providerId: Rep[Option[Int]] = column[Option[Int]]("provider_id", O.Default(None))

    /** Foreign key referencing AuthUser (database name problem_problem_provider_id_e3917eed_fk_auth_user_id) */
    lazy val authUserFk = foreignKey("problem_problem_provider_id_e3917eed_fk_auth_user_id", providerId, AuthUser)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Index over (voj,vid) (database name problem_pro_voj_ac3d0b_idx) */
    val index1 = index("problem_pro_voj_ac3d0b_idx", (voj, vid))
    /** Uniqueness Index over (voj,vid) (database name problem_problem_voj_vid_d1c4ce44_uniq) */
    val index2 = index("problem_problem_voj_vid_d1c4ce44_uniq", (voj, vid), unique=true)
  }
  /** Collection-like TableQuery object for table ProblemProblem */
  lazy val ProblemProblem = new TableQuery(tag => new ProblemProblem(tag))

  /** Row type of table ProblemProblemtag */
  type ProblemProblemta = (Int, String)
  /** Constructor for ProblemProblemta providing default values if available in the database schema. */
  def ProblemProblemta(id: Int, name: String): ProblemProblemta = {
    (id, name)
  }
  /** GetResult implicit for fetching ProblemProblemta objects using plain SQL queries */
  implicit def GetResultProblemProblemta(implicit e0: GR[Int], e1: GR[String]): GR[ProblemProblemta] = GR{
    prs => import prs._
    ProblemProblemta.tupled((<<[Int], <<[String]))
  }
  /** Table description of table problem_problemtag. Objects of this class serve as prototypes for rows in queries. */
  class ProblemProblemtag(_tableTag: Tag) extends profile.api.Table[ProblemProblemta](_tableTag, Some("old"), "problem_problemtag") {
    def * = (id, name) <> (ProblemProblemta.tupled, ProblemProblemta.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name)).shaped.<>({r=>import r._; _1.map(_=> ProblemProblemta.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(64,true) */
    val name: Rep[String] = column[String]("name", O.Length(64,varying=true))

    /** Index over (name) (database name problem_problemtag_name_e16be4c6_like) */
    val index1 = index("problem_problemtag_name_e16be4c6_like", name)
    /** Uniqueness Index over (name) (database name problem_problemtag_name_key) */
    val index2 = index("problem_problemtag_name_key", name, unique=true)
  }
  /** Collection-like TableQuery object for table ProblemProblemtag */
  lazy val ProblemProblemtag = new TableQuery(tag => new ProblemProblemtag(tag))

  /** Row type of table ProblemProblemTags */
  type ProblemProblemTag = (Int, Int, Int)
  /** Constructor for ProblemProblemTag providing default values if available in the database schema. */
  def ProblemProblemTag(id: Int, problemId: Int, problemtagId: Int): ProblemProblemTag = {
    (id, problemId, problemtagId)
  }
  /** GetResult implicit for fetching ProblemProblemTag objects using plain SQL queries */
  implicit def GetResultProblemProblemTag(implicit e0: GR[Int]): GR[ProblemProblemTag] = GR{
    prs => import prs._
    ProblemProblemTag.tupled((<<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table problem_problem_tags. Objects of this class serve as prototypes for rows in queries. */
  class ProblemProblemTags(_tableTag: Tag) extends profile.api.Table[ProblemProblemTag](_tableTag, Some("old"), "problem_problem_tags") {
    def * = (id, problemId, problemtagId) <> (ProblemProblemTag.tupled, ProblemProblemTag.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(problemId), Rep.Some(problemtagId)).shaped.<>({r=>import r._; _1.map(_=> ProblemProblemTag.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column problem_id SqlType(int4) */
    val problemId: Rep[Int] = column[Int]("problem_id")
    /** Database column problemtag_id SqlType(int4) */
    val problemtagId: Rep[Int] = column[Int]("problemtag_id")

    /** Foreign key referencing ProblemProblem (database name problem_problem_tags_problem_id_d03cc04a_fk_problem_problem_id) */
    lazy val problemProblemFk = foreignKey("problem_problem_tags_problem_id_d03cc04a_fk_problem_problem_id", problemId, ProblemProblem)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing ProblemProblemtag (database name problem_problem_tags_problemtag_id_ae11dd8f_fk_problem_p) */
    lazy val problemProblemtagFk = foreignKey("problem_problem_tags_problemtag_id_ae11dd8f_fk_problem_p", problemtagId, ProblemProblemtag)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (problemId,problemtagId) (database name problem_problem_tags_problem_id_problemtag_id_aa6b9dba_uniq) */
    val index1 = index("problem_problem_tags_problem_id_problemtag_id_aa6b9dba_uniq", (problemId, problemtagId), unique=true)
  }
  /** Collection-like TableQuery object for table ProblemProblemTags */
  lazy val ProblemProblemTags = new TableQuery(tag => new ProblemProblemTags(tag))

  /** Row type of table ProfileProfile */
  type ProfileProfil = (Int, Boolean, Int)
  /** Constructor for ProfileProfil providing default values if available in the database schema. */
  def ProfileProfil(id: Int, `private`: Boolean, userId: Int): ProfileProfil = {
    (id, `private`, userId)
  }
  /** GetResult implicit for fetching ProfileProfil objects using plain SQL queries */
  implicit def GetResultProfileProfil(implicit e0: GR[Int], e1: GR[Boolean]): GR[ProfileProfil] = GR{
    prs => import prs._
    ProfileProfil.tupled((<<[Int], <<[Boolean], <<[Int]))
  }
  /** Table description of table profile_profile. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: private */
  class ProfileProfile(_tableTag: Tag) extends profile.api.Table[ProfileProfil](_tableTag, Some("old"), "profile_profile") {
    def * = (id, `private`, userId) <> (ProfileProfil.tupled, ProfileProfil.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(`private`), Rep.Some(userId)).shaped.<>({r=>import r._; _1.map(_=> ProfileProfil.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column private SqlType(bool)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `private`: Rep[Boolean] = column[Boolean]("private")
    /** Database column user_id SqlType(int4) */
    val userId: Rep[Int] = column[Int]("user_id")

    /** Foreign key referencing AuthUser (database name profile_profile_user_id_7b0aedd8_fk_auth_user_id) */
    lazy val authUserFk = foreignKey("profile_profile_user_id_7b0aedd8_fk_auth_user_id", userId, AuthUser)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (userId) (database name profile_profile_user_id_key) */
    val index1 = index("profile_profile_user_id_key", userId, unique=true)
  }
  /** Collection-like TableQuery object for table ProfileProfile */
  lazy val ProfileProfile = new TableQuery(tag => new ProfileProfile(tag))
}
