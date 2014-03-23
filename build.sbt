import java.io.{InputStreamReader, BufferedReader}

name := "actorTest"

version := "1.0"

resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.0"

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


resolvers += "spray repo" at "http://repo.spray.io"


libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.11.3" % "test"
)

libraryDependencies += "commons-codec" % "commons-codec" % "1.2"

libraryDependencies += "org.specs2" %% "specs2" % "2.1.1"

libraryDependencies += "org.apache.commons" % "commons-io" % "1.3.2"

libraryDependencies += "io.spray" % "spray-can" % "1.3.0"

libraryDependencies += "com.cloudphysics" %% "jerkson" % "0.6.1"

libraryDependencies += "com.lmax" % "disruptor" % "3.2.1"

logLevel := Level.Info

lazy val runPlatypus = taskKey[Unit]("run in the background and kill previous job")

lazy val stopPlatypus = taskKey[Unit]("stop in the background and kill previous job")



runPlatypus := {
  println("----starting the platypus-----")
  if(new java.io.File("PID").exists){
    val PID = scala.io.Source.fromFile("PID").mkString.stripMargin.stripLineEnd
    try{
      println(f"killing process ${PID}")
      f"kill -9 ${PID}".!!
    }catch{
      case e:Throwable => println(f"process ${PID} is no longer running")
    }
  }
  // val p:java.lang.Process = java.lang.Runtime.getRuntime().exec("sbt run >>file.txt 2>&1");
  //val p:java.lang.Process = java.lang.Runtime.getRuntime().exec(Array("bash", "sbt run >>/home/solr/benchmarks/platypus/log.txt 2>&1" ));
  val builder:java.lang.ProcessBuilder = new java.lang.ProcessBuilder("sudo", "sbt", "run");
  builder.redirectOutput(new File("log.log"));
  builder.redirectError(new File("log.log"));
  builder.start();
}


stopPlatypus := {
  println("----stopping the platypus-----")
  if(new java.io.File("PID").exists){
    val PID = scala.io.Source.fromFile("PID").mkString.stripMargin.stripLineEnd
    try{
      println(f"killing process ${PID}")
      f"kill -9 ${PID}".!!
    }catch{
      case e:Throwable => println(f"process ${PID} is no longer running")
    }
  }
}

//∂logLevel := Level.Debug