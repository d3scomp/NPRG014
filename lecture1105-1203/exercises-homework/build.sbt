lazy val commonSettings = Seq(
  organization := "com.example",
  version := "0.1.0",
  scalaVersion := "2.12.7"
)

lazy val macros = (project in file("macros")).
  settings(commonSettings: _*).
  settings(
    name := "NPRG014 Scala - macros",

    libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value
  )

lazy val core = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "NPRG014 Scala",

    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.1.1",

    libraryDependencies += "net.liftweb" %% "lift-json" % "3.3.0",
    libraryDependencies += "org.specs2" %% "specs2-core" % "4.3.5" % "test",

    libraryDependencies += "org.specs2" %% "specs2-mock" % "4.3.5" % "test",

    resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
  ).
  dependsOn(macros)





