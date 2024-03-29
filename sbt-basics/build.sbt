// command `sbt new scala/hello-world.g8` to start a new Scala SBT project

// The simplest possible sbt build file is just one line:

scalaVersion := "2.13.6"
// That is, to create a valid sbt build, all you've got to do is define the
// version of Scala you'd like your project to use.

// ============================================================================

// Lines like the above defining `scalaVersion` are called "settings". Settings
// are key/value pairs. In the case of `scalaVersion`, the key is "scalaVersion"
// and the value is "2.13.6"

// It's possible to define many kinds of settings, such as:

name         := "sbt-basics"
organization := "com.itechart.internship"
version      := "1.0"

// Note, it's not required for you to define these three settings. These are
// mostly only necessary if you intend to publish your library's binaries on a
// place like Sonatype or other public repo.

// Want to use a published library in your project?
// You can define other libraries as dependencies in your build like this:

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"

// if we use a double percent symbol (%%), sbt will resolve the relevant Scala version for us
// If we need to use a specific Scala version, we can use % to resolve the dependencies

// Here, `libraryDependencies` is a set of dependencies, and by using `+=`,
// we're adding the scala-parser-combinators dependency to the set of dependencies
// that sbt will go and fetch when it starts up.
// Now, in any Scala file, you can import classes, objects, etc., from
// scala-parser-combinators with a regular import.

// TIP: To find the "dependency" that you need to add to the
// `libraryDependencies` set, which in the above example looks like this:

// "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"

// You can use Scaladex (or Maven central repository), an index of all known published Scala libraries.
// There, after you find the library you want, you can just copy/paste the dependency
// information that you need into your build file. For example, on the
// scala/scala-parser-combinators Scaladex page,
// https://index.scala-lang.org/scala/scala-parser-combinators, you can copy/paste
// the sbt dependency from the sbt box on the right-hand side of the screen.

//val catsVersion       = "2.2.0"
//val catsEffectVersion = "2.2.0"
//
//libraryDependencies ++= Seq(
//  "org.typelevel" %% "cats-core"   % catsVersion,
//  "org.typelevel" %% "cats-effect" % catsEffectVersion
//)

// ============================================================================

// Most moderately interesting Scala projects don't make use of the very simple
// build file style (called "bare style") used in this build.sbt file. Most
// intermediate Scala projects make use of so-called "multi-project" builds. A
// multi-project build makes it possible to have different folders which sbt can
// be configured differently for. That is, you may wish to have different
// dependencies or different testing frameworks defined for different parts of
// your codebase. Multi-project builds make this possible.

// Here's a quick glimpse of what a multi-project build looks like for this
// build, with only one "subproject" defined, called `root`:

// OPTIONS

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-Ymacro-annotations",
  "-Xfatal-warnings"
)

// SETTINGS

val basePackage = "com.itechart"

lazy val defaultSettings = scalafmtSettings ++ commonSettings

lazy val commonSettings = Seq(
  organization := s"$basePackage",
  version      := "1.0",
  scalaVersion := "2.13.6"
)

lazy val scalafmtSettings =
  Seq(
    scalafmtOnCompile := true,
  )

lazy val commonDependencies = Seq(
  "org.typelevel" %% "cats-core"   % "2.2.0",
  "org.typelevel" %% "cats-effect" % "2.2.0"
)

lazy val testDependencies = Seq(
  "org.scalatestplus" %% "scalatestplus-scalacheck" % "3.1.0.0-RC2" % Test,
  "org.scalatestplus" %% "selenium-2-45"            % "3.1.0.0-RC2" % Test,
)

// PROJECTS

lazy val root = (project in file("."))
  .aggregate(util, core)

lazy val util = project
  .in(file("util"))
  .settings(defaultSettings)

lazy val core = project
  .dependsOn(util)
  .in(file("core"))
  .settings(
    defaultSettings,
    libraryDependencies ++= commonDependencies ++ testDependencies,
  )

// use `sbt assembly` to build jar files for each module

// then use `scala pathToJarFile.jar arg1 arg2` - it will execute the main class

// example: `scala core/target/scala-2.13/core-assembly-1.0.jar testArg1 testArg2`

// To learn more about multi-project builds, head over to the official sbt
// documentation at https://www.scala-sbt.org/release/docs/Multi-Project.html

// some basic commands: https://alvinalexander.com/scala/sbt-how-to-compile-run-package-scala-project/
