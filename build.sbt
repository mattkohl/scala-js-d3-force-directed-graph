enablePlugins(ScalaJSPlugin)

name := "Force-Directed Graph"

version := "0.1-SNAPSHOT"

scalaVersion := "2.12.6"

scalaJSUseMainModuleInitializer := true

libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.6",
    "org.singlespaced" %%% "scalajs-d3" % "0.3.4"
)
