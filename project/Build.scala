import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "GEOIP"
    val appVersion      = "1.0"

    val appDependencies = Seq(
        // Add your project dependencies here,
        "mysql" % "mysql-connector-java" % "5.1.21",
        "org.reflections" % "reflections" % "0.9.8",
        javaCore, javaJdbc, javaEbean,
        filters
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here
      testOptions in Test += Tests.Argument("junitxml", "console")
    )

}
