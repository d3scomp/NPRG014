lazy val commonSettings = Seq(
  organization := "com.example",
  version := "0.1.0",
  scalaVersion := "2.11.8"
)

lazy val macros = (project in file("macros")).
  settings(commonSettings: _*).
  settings(
    name := "NPRG014 Scala (solved) - macros",

    libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value
  )

lazy val core = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "NPRG014 Scala (solved)",

    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",

    libraryDependencies += "net.liftweb" %% "lift-json" % "2.6.3",
    libraryDependencies += "org.specs2" %% "specs2-core" % "3.0" % "test",

    libraryDependencies += "org.specs2" %% "specs2-mock" % "3.0" % "test",

    resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
  ).
  dependsOn(macros)





