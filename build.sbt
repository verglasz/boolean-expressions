ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.1"

libraryDependencies ++= Seq(
	"org.scalactic" %% "scalactic" % "3.2.12",
	"org.scalatest" %% "scalatest" % "3.2.12" % "test",
  "io.spray" %%  "spray-json" % "1.3.6"
)


lazy val root = (project in file("."))
  .settings(
    name := "boolean-expr"
  )

