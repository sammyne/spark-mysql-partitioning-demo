// The simplest possible sbt build file is just one line:

scalaVersion := "2.12.10"
// That is, to create a valid sbt build, all you've got to do is define the
// version of Scala you'd like your project to use.

// ============================================================================

// Lines like the above defining `scalaVersion` are called "settings". Settings
// are key/value pairs. In the case of `scalaVersion`, the key is "scalaVersion"
// and the value is "2.13.8"

// It's possible to define many kinds of settings, such as:

name := "helloworld"
organization := "com.github.sammyne"
version := "1.0"

run / fork := true

val mysqlVersion = "8.0.28"
val sparkVersion = "3.1.3"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "mysql" % "mysql-connector-java" % mysqlVersion,
  "org.scalatest" %% "scalatest" % "3.2.11" % Test,
)

resolvers += Resolver.mavenLocal

scalacOptions += "-Ywarn-unused"
inThisBuild(
  List(
    scalaVersion := "2.12.10",
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision
  )
)
