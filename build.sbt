val scala3Version = "2.13.6"

val http4sVersion = "0.23.6"

lazy val root = project
  .in(file("."))
  .settings(
    name := "lehahomework",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies := Seq(
      "org.http4s" %% "http4s-core" % http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "io.circe" %% "circe-generic" % "0.14.1",
      "com.nrinaudo" %% "kantan.csv" % "0.6.1",
      "com.github.pureconfig" %% "pureconfig" % "0.17.0",
      "org.typelevel" %% "log4cats-slf4j" % "2.1.1",
      "org.slf4j" % "slf4j-api" % "1.7.32",
      "org.slf4j" % "slf4j-simple" % "1.7.32",
      "org.scalatest" %% "scalatest" % "3.2.10" % Test,
    ),
  )
