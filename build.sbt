name := "geoip"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache
)     

templatesImport += "com.avaje.ebean._"

play.Project.playJavaSettings

resolvers += "Atlassian's Maven Public Repository" at "https://maven.atlassian.com/content/groups/public"

resolvers += "Local Maven Repository" at "file://" + Path.userHome + "/.m2/repository"
