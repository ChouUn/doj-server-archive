name := "doj_server_scala"

version := "1.0"

lazy val `doj_server_scala` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

scalaVersion := "2.12.4"

val playVersion = "2.6.12"
val playSlickVersion = "3.0.3"
val slickVersion = "3.2.2"
val slickPgVersion = "0.16.0"
val sangriaVersion = "1.4.0"

libraryDependencies ++= Seq(
  ehcache,
  ws,
  specs2 % Test,
  guice,

  // https://www.playframework.com/documentation/2.6.x/PlaySlick
  // https://www.playframework.com/documentation/2.6.x/PlaySlickMigrationGuide
  "com.typesafe.play" % "play-cache_2.12" % playVersion,
  "com.typesafe.play" %% "play-slick" % playSlickVersion,
  "com.typesafe.play" %% "play-slick-evolutions" % playSlickVersion,
  "com.typesafe.slick" %% "slick-codegen" % slickVersion,

  // https://github.com/tminglei/slick-pg
  "com.github.tminglei" %% "slick-pg" % slickPgVersion exclude("org.postgresql" , "postgresql"),
  "com.github.tminglei" %% "slick-pg_play-json" % slickPgVersion,

  "org.pac4j" % "pac4j-oidc" % "2.2.1" exclude("commons-io" , "commons-io"),
  "org.pac4j" % "play-pac4j" % "4.1.1",

  "org.sangria-graphql" %% "sangria" % sangriaVersion,
  "org.sangria-graphql" %% "sangria-relay" % sangriaVersion,
  "org.sangria-graphql" %% "sangria-play-json" % "1.0.4",

  // https://mvnrepository.com/artifact/com.h2database/h2
  "com.h2database" % "h2" % "1.4.192",

  "commons-io" % "commons-io" % "2.6"
)

dependencyOverrides ++= Set(
  // https://mvnrepository.com/artifact/org.postgresql/postgresql
  "org.postgresql" % "postgresql" % "9.4.1212"
)


unmanagedResourceDirectories in Test += (baseDirectory.value / "target/web/public/test")
