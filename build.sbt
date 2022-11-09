import Dependencies._

lazy val elevator = project.in(file("elevator")).settings(name := "elevator")    .settings(commonSettings)
    .settings(libraryDependencies ++= catsDependencies)

lazy val `process` =
  project
    .in(file("process"))
    .settings(name := "process")
    .settings(commonSettings)
    .settings(dependencies)

lazy val commonSettings = Seq(
  Compile / console / scalacOptions --= Seq(
    "-Wunused:_",
    "-Xfatal-warnings",
  ),
  Test / console / scalacOptions :=
    (Compile / console / scalacOptions).value,
)

lazy val dependencies = Seq(
  libraryDependencies ++= Seq(
    // main dependencies
  ),
  libraryDependencies ++= Seq(
    org.scalatest.scalatest,
    org.scalatestplus.`scalacheck-1-15`,
  ).map(_ % Test),
)
