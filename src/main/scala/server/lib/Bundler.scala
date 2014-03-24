package server.lib

import server.Configuration

/**
 * Created by hernansaab on 3/7/14.
 */
object Bundler {

  /**
   *
   * @param resources
   * @return javascript commands for including files
   */
  def bundleJs(resources:Array[String]):String = {
    resources.map((resource:String) => {"<script src=\""+ Configuration.staticPath +"/"+resource +"\"></script>\n"} ).mkString+"\n"
  }

  def bundleStyles(resources:Array[String]):String = {
    resources.map((resource:String) => {"<link rel=\"stylesheet\" text=\"text/css\" href=\""+ Configuration.staticPath +"/"+resource +"\"></script>\n"} ).mkString+"\n"
  }

}
