/*

Plugins

A plugin is a way to use external code in a build definition.

 - a plugin can be a library used to implement a task.

 - a plugin can define a sequence of sbt settings that are automatically
 added to all projects or that are explicitly declared for selected projects.

 For example, a plugin might add a proguard task and associated (overridable) settings.

 - a plugin can define new commands (via the commands setting).

 */

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.0")
addSbtPlugin("com.eed3si9n"  % "sbt-assembly" % "0.14.6")
