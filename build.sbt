ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.3"

lazy val root = (project in file("."))
  .settings(
    name := "ScalaFlinkStreamingApp"
  )

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.11" % Test
libraryDependencies += "org.apache.flink" % "flink-clients" % "1.16.1"
libraryDependencies += "org.apache.flink" % "flink-connector-kafka" % "1.16.1"
libraryDependencies += "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.19.0"
libraryDependencies += "org.apache.logging.log4j" % "log4j-api" % "2.19.0"
libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % "2.19.0"

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
