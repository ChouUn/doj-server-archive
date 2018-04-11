logLevel := Level.Warn

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += Resolver.sonatypeRepo("snapshots")

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.10")
