addCommandAlias(
  "fix",
  "; scalafmtAll ; scalafmtSbt",
)
addCommandAlias("coverAll", "; clean; coverage; test; it:test; coverageAggregate")
addCommandAlias(
  "up2date",
  "reload plugins; dependencyUpdates; reload return; dependencyUpdates",
)

autoCompilerPlugins := true

Global / onChangedBuildSource := ReloadOnSourceChanges

IntegrationTest / parallelExecution := false
IntegrationTest / turbo := true

Test / parallelExecution := false
Test / publishArtifact := false
Test / turbo := true

ThisBuild / name := "typedrivendevelopment"
ThisBuild / organization := "io.github.massimosiani"
ThisBuild / scalaVersion := "3.0.0"

ThisBuild / semanticdbEnabled := true
ThisBuild / turbo := true

ThisBuild / libraryDependencies ++= Seq(
  "org.apache.logging.log4j" % "log4j-core" % "2.14.1" % Runtime
)
ThisBuild / resolvers ++= Seq(
  Resolver.sonatypeRepo("releases")
)
