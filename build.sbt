
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

resolvers += "HMRC-open-artefacts-maven2" at "https://open.artefacts.tax.service.gov.uk/maven2"

//libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.5" % "test"

libraryDependencies ++= Seq(
  "uk.gov.hmrc.mongo" %% "hmrc-mongo-play-28" % "0.63.0",
  guice,
  ws,
  "org.scalatest" %% "scalatest" % "3.2.5" % Test,
  "org.scalamock" %% "scalamock" % "5.1.0" % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
  "org.typelevel" %% "cats-core" % "2.3.0",
  "com.github.tomakehurst" % "wiremock-jre8" % "2.35.0" % Test
)

dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.11.0"

parallelExecution in Test := false

lazy val root = (project in file("."))
  .settings(
    name := "play-template"

  )
  .enablePlugins(PlayScala)