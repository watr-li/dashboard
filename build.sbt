name := """dashboard"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws
)

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.34"

libraryDependencies += "com.typesafe.slick" %% "slick" % "2.1.0"

libraryDependencies +=  "com.typesafe.slick" %% "slick-codegen" % "2.1.0"

libraryDependencies += "org.eclipse.californium" % "californium-core" % "1.0.0-M3"

libraryDependencies += "org.twitter4j" % "twitter4j-core" % "4.0.2"

libraryDependencies += "org.twitter4j" % "twitter4j-core" % "4.0.2" classifier "sources"

libraryDependencies += "org.twitter4j" % "twitter4j-core" % "4.0.2" classifier "javadoc"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.10"


lazy val slick = TaskKey[Seq[File]]("gen-tables")

lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
  val outputDir = (dir / "slick").getPath // place generated files in sbt's managed sources folder
//val url = "jdbc:h2:mem:test;INIT=runscript from 'src/main/sql/create.sql'" // connection info for a pre-populated throw-away, in-memory db for this demo, which is freshly initialized on every run
val url = "jdbc:mysql://localhost/watrli-dashboard"
  val jdbcDriver = "com.mysql.jdbc.Driver"
  val slickDriver = "scala.slick.driver.MySQLDriver"
  val user = "root"
  val password = "root"
  val pkg = "db"
  toError(r.run("scala.slick.codegen.SourceCodeGenerator", cp.files, Array(slickDriver, jdbcDriver, url, outputDir, pkg, user, password), s.log))
  val fname = outputDir + "/" + pkg + "/Tables.scala"
  Seq(file(fname))
}

slick <<= slickCodeGenTask // register manual sbt command

sourceGenerators in Compile <+= slickCodeGenTask // register automatic code generation on every compile, remove for only manual use
