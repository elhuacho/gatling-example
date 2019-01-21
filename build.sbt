enablePlugins(GatlingPlugin)

name := "gatling-example"

version := "0.1"

scalaVersion := "2.12.8"

scalacOptions := Seq(
  "-encoding", "UTF-8", "-target:jvm-1.8", "-deprecation",
  "-feature", "-unchecked", "-language:implicitConversions", "-language:postfixOps")

libraryDependencies += "io.gatling.highcharts" % "gatling-charts-highcharts" % "3.0.0-RC4" % "test"
libraryDependencies += "io.gatling" % "gatling-test-framework" % "3.0.0-RC4" % "test"