ThisBuild / version := "0.2.0"

ThisBuild / scalaVersion := "3.1.1"

val circeVersion = "0.14.2"

libraryDependencies ++= Seq(
	"org.scalactic" %% "scalactic" % "3.2.12",
	"org.scalatest" %% "scalatest" % "3.2.12" % "test",
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion
)

lazy val root = (project in file("."))
  .settings(
    name := "boolean-expr"
  )

