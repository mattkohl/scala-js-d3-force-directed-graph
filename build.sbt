enablePlugins(ScalaJSPlugin)

name := "Force-Directed Graph"

version := "0.1-SNAPSHOT"

scalaVersion := "2.12.6"

scalaJSUseMainModuleInitializer := true
scalaJSModuleKind := ModuleKind.CommonJSModule

resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.6",
  "org.singlespaced" %%% "scalajs-d3" % "0.3.4",
  "com.github.fdietze" % "scala-js-d3v4" % "master-SNAPSHOT"
)
