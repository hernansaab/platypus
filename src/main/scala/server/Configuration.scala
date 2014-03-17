package server

import com.typesafe.config.ConfigFactory


object Configuration {
  val factory =  ConfigFactory.load()
  def outputRouterCount():Int = 40
  def inputRouterCount():Int = 300
  val port:Int = factory.getString("platypus.server.port").toInt;
  val generators:Int =  factory.getString("platypus.server.generators").toInt;
  val serverName:String = factory.getString("platypus.name");
  val staticPath:String = factory.getString("platypus.static.path");
  val timeoutMilliseconds:Int = factory.getString("platypus.server.timeoutMilliseconds").toInt;


}

