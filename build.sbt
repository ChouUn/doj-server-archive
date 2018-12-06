import sbt.Keys.resolvers

name := "doj-server"

version := "1.0"

val playVersion = "2.6.20"
val playSlickVersion = "3.0.3"
val slickVersion = "3.2.3"
val slickPgVersion = "0.16.3"
val sangriaVersion = "1.4.2"
val circeVersion = "0.10.1"

lazy val commonSettings = Seq(
  scalaVersion := "2.12.7",

  resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/",
  resolvers += "Bintray sbt plugin" at "https://dl.bintray.com/playframework/sbt-plugin-releases/",
  resolvers += "Bintray Scalaz" at "https://dl.bintray.com/scalaz/releases",
  resolvers += Resolver.sonatypeRepo("snapshots"),

  libraryDependencies ++= Seq(
    // default
    ehcache,
    ws,
    specs2 % Test,
    guice,

    // play framework
    // @see https://mvnrepository.com/artifact/com.typesafe.play/play
    "com.typesafe.play" %% "play" % playVersion,
    // @see https://mvnrepository.com/artifact/com.typesafe.play/play-cache
    "com.typesafe.play" %% "play-cache" % playVersion,

    // circe
    // @see https://circe.github.io/circe/
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    // circe for play frameword
    // @see https://github.com/jilen/play-circe
    "com.dripower" %% "play-circe" % "2610.0",

    // slick for play framework
    // @see https://www.playframework.com/documentation/2.6.x/PlaySlick
    "com.typesafe.play" %% "play-slick" % playSlickVersion,
    // @see https://www.playframework.com/documentation/2.6.x/PlaySlickMigrationGuide
    "com.typesafe.play" %% "play-slick-evolutions" % playSlickVersion,

    // slick codegen
    // @see https://mvnrepository.com/artifact/com.typesafe.slick/slick-codegen
    "com.typesafe.slick" %% "slick-codegen" % slickVersion,

    // postgres for slick
    // @see https://github.com/tminglei/slick-pg
    // @see https://mvnrepository.com/artifact/com.github.tminglei/slick-pg
    "com.github.tminglei" %% "slick-pg" % slickPgVersion exclude("org.postgresql", "postgresql"),
    // @see https://mvnrepository.com/artifact/com.github.tminglei/slick-pg_circe-json
    "com.github.tminglei" %% "slick-pg_circe-json" % slickPgVersion
      excludeAll ExclusionRule(organization = "io.circe"),

    // Java security engine
    // @see http://pac4j.org/3.3.x/docs/index.html
    "org.pac4j" % "pac4j-oidc" % "3.3.0" exclude("commons-io", "commons-io"),
    "org.pac4j" %% "play-pac4j" % "6.1.0",

    // Sangria
    // @see https://mvnrepository.com/artifact/org.sangria-graphql/sangria
    "org.sangria-graphql" %% "sangria" % sangriaVersion,
    "org.sangria-graphql" %% "sangria-relay" % sangriaVersion,
    "org.sangria-graphql" %% "sangria-slowlog" % "0.1.8",
    // @see https://mvnrepository.com/artifact/org.sangria-graphql/sangria-circe
    "org.sangria-graphql" %% "sangria-circe" % "1.2.1"
      excludeAll ExclusionRule(organization = "io.circe"),

    // h2
    // @see https://mvnrepository.com/artifact/com.h2database/h2
    "com.h2database" % "h2" % "1.4.197",

    // commons-io
    // @see https://mvnrepository.com/artifact/commons-io/commons-io
    "commons-io" % "commons-io" % "2.6",
  ),

  dependencyOverrides ++= Seq(
    // Postgresql
    // @see https://mvnrepository.com/artifact/org.postgresql/postgresql
    "org.postgresql" %% "postgresql" % "42.2.5",
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
