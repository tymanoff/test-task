name := "test-task-scala"
scalaVersion := "2.13.14"

lazy val root = (project in file("."))
  .settings(
    libraryDependencies ++= Seq(
      "org.slf4j" % "slf4j-api" % "1.7.36",
      "com.typesafe" % "config" % "1.4.3",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "org.scalatest" %% "scalatest" % "3.2.19" % "test",
      "org.scalamock" %% "scalamock" % "6.0.0" % Test
    )
  )
