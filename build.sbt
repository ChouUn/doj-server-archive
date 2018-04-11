name := "doj_server_scala"

version := "1.0"

val playVersion = "2.6.12"
val playSlickVersion = "3.0.3"
val slickVersion = "3.2.2"
val slickPgVersion = "0.16.0"
val sangriaVersion = "1.4.0"


lazy val commonSettings = Seq(
  scalaVersion := "2.12.4",

  resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
  resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/",

  libraryDependencies ++= Seq(
    // default
    ehcache,
    ws,
    specs2 % Test,
    guice,
    // play framework
    "com.typesafe.play" % "play-cache_2.12" % playVersion,
    // slick
    "com.typesafe.slick" %% "slick-codegen" % slickVersion,
    // slick for play framework
    // @ref https://www.playframework.com/documentation/2.6.x/PlaySlick
    // @ref https://www.playframework.com/documentation/2.6.x/PlaySlickMigrationGuide
    "com.typesafe.play" %% "play-slick" % playSlickVersion,
    "com.typesafe.play" %% "play-slick-evolutions" % playSlickVersion,
    // postgres for slick
    // @ref https://github.com/tminglei/slick-pg
    "com.github.tminglei" %% "slick-pg" % slickPgVersion exclude("org.postgresql" , "postgresql"),
    "com.github.tminglei" %% "slick-pg_play-json" % slickPgVersion,
    // Java security engine
    "org.pac4j" % "pac4j-oidc" % "2.2.1" exclude("commons-io" , "commons-io"),
    "org.pac4j" % "play-pac4j" % "4.1.1",
    // sangria
    "org.sangria-graphql" %% "sangria" % sangriaVersion,
    "org.sangria-graphql" %% "sangria-relay" % sangriaVersion,
    "org.sangria-graphql" %% "sangria-slowlog" % "0.1.5",
    "org.sangria-graphql" %% "sangria-play-json" % "1.0.4",
    // h2
    // @ref https://mvnrepository.com/artifact/com.h2database/h2
    "com.h2database" % "h2" % "1.4.192",
    // commons-io
    "commons-io" % "commons-io" % "2.6"
  ),

  dependencyOverrides ++= Seq(
    // https://mvnrepository.com/artifact/org.postgresql/postgresql
    "org.postgresql" % "postgresql" % "9.4.1212"
  ),

  unmanagedResourceDirectories in Test += (baseDirectory.value / "target/web/public/test"),

  evictionWarningOptions in update := EvictionWarningOptions.default
    .withWarnTransitiveEvictions(false)
    .withWarnDirectEvictions(false)
)


lazy val `macro` = project
  .settings(commonSettings)


lazy val `doj_server_scala` = (project in file("."))
  .dependsOn(`macro`)
  .aggregate(`macro`)
  .settings(commonSettings)
  .enablePlugins(PlayScala)
