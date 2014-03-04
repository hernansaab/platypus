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

//∂logLevel := Level.Debug