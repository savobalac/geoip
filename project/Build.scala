import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "GEOIP"
    val appVersion      = "1.0"

    val appDependencies = Seq(
        // Add your project dependencies here,
        "mysql" % "mysql-connector-java" % "5.1.18",
        "postgresql" % "postgresql" % "9.1-901.jdbc4",
        "org.reflections" % "reflections" % "0.9.8",
        javaCore, javaJdbc, javaEbean,
        filters,
        "com.atlassian.connect" % "ac-play-java_2.10" % "0.7.0-BETA6" withSources()
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here
      testOptions in Test += Tests.Argument("junitxml", "console")
    )

}
