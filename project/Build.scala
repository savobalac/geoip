import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "GEOIP"
    val appVersion      = "1.0"

    val appDependencies = Seq(
        // Add your project dependencies here,
        "postgresql" % "postgresql" % "9.1-901.jdbc4",
        "org.reflections" % "reflections" % "0.9.8",
        javaCore, javaJdbc, javaEbean,
        filters,
        "com.atlassian.connect" % "ac-play-java_2.10" % "0.7.0-BETA9" withSources(),
        "com.maxmind.geoip2" % "geoip2" % "0.7.0"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here
      testOptions in Test += Tests.Argument("junitxml", "console")
    )

}
