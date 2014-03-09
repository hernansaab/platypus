name := "actorTest"

version := "1.0"

resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.1.0"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.6"

libraryDependencies += "net.liftweb" % "lift-json_2.9.0-1" % "2.4"

libraryDependencies += "com.typesafe.slick" %% "slick" % "2.0.0"

libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.6.4"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0" % "test"

libraryDependencies +="mysql" % "mysql-connector-java" % "5.1.2"

libraryDependencies +="org.fusesource.scalate" % "scalate-core_2.10" % "1.6.1"

libraryDependencies += "org.joda" % "joda-convert" % "1.2"

libraryDependencies += "joda-time" % "joda-time" % "2.0"

libraryDependencies += "log4j" % "log4j" % "1.2.14"

resolvers ++= Seq(
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.11.3" % "test"
)

libraryDependencies += "commons-codec" % "commons-codec" % "1.2"

libraryDependencies += "org.specs2" %% "specs2" % "2.1.1"

libraryDependencies += "org.apache.commons" % "commons-io" % "1.3.2"

logLevel := Level.Warn

//∂logLevel := Level.Debug