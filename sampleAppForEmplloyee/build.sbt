name := """sampleAppForEmplloyee"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws
)

libraryDependencies += "org.scalatestplus" % "play_2.11" % "1.2.0" % "test"

libraryDependencies += "commons-io" % "commons-io" % "2.4"

libraryDependencies ++= List(
   "com.typesafe.slick" %% "slick" % "3.0.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.h2database" % "h2" % "1.4.187",
"postgresql" % "postgresql" % "9.1-901.jdbc4"
)
