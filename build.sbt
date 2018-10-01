name := "monogram"

version := "0.1"

scalaVersion := "2.12.7"

enablePlugins(ScalaJSPlugin)

scalaJSUseMainModuleInitializer := true
mainClass in Compile := Some("com.github.monogram.PageBuilder")

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.6"
libraryDependencies += "de.sciss" %% "scalamidi" % "0.2.1"
