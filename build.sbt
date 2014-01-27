name := "geoip"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "mysql" % "mysql-connector-java" % "5.1.21"
)     

templatesImport += "com.avaje.ebean._"

play.Project.playJavaSettings
