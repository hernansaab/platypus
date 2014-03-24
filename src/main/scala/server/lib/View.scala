package server.lib

import org.fusesource.scalate.{TemplateEngine, Binding, RenderContext}

import java.io.File

/**
 * Created by hernansaab on 3/5/14.
 */
object View {
  var engine: TemplateEngine = null
  def html(path:String, args:Map[String, Any]):String =  {
    val content = engine.layout(path, args)
    views.headers.Common.response200HtmlNoCookies(content.size)+content
  }
}
