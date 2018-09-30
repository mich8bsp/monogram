name := "monogram"

version := "0.1"

scalaVersion := "2.12.7"

enablePlugins(ScalaJSPlugin)

scalaJSUseMainModuleInitializer := true

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.6"
