package server

import com.typesafe.config.ConfigFactory


object Configuration {
  def outputRouterCount():Int = 40
  def inputRouterCount():Int = 300
  val port:Int = ConfigFactory.load().getString("platypus.server.port").toInt;
  val serverName:String = ConfigFactory.load().getString("platypus.name");
  val staticPath:String = ConfigFactory.load().getString("platypus.static.path");
  val timeoutMilliseconds:Int = ConfigFactory.load().getString("platypus.server.timeoutMilliseconds").toInt;


}

