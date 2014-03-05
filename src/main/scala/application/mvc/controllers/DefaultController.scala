package application.mvc.controllers
import org.fusesource.scalate._
import scala.text.Document
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import org.fusesource.scalate.{TemplateEngine, Binding, RenderContext}

import java.io.File
import server.lib.View

/**
 * Created by hernansaab on 2/27/14.
 */
object DefaultController {
  def index(get:String):String = {
    val engine = new TemplateEngine
    View.html("/index.ssp", Map("name" -> ("Hiram", "Chirino"), "city" -> "Tampa"))

  }
  def indexx(get:String):String = {
    """HTTP/1.1 200 OK
Date: Thu, 27 Feb 2014 07:23:45 GMT
Server: Apache/2.2.3 (CentOS)
Accept-Ranges: bytes
Connection: keep-alive, close
Content-Length: 13016
Content-Type: text/html; charset=UTF-8
Accept-Ranges:bytes
Connection:keep-alive, close
Content-Length:13016
Content-Type:text/html; charset=UTF-8
Date:Thu, 27 Feb 2014 06:58:52 GMT
Server:Apache/2.2.3 (CentOS)


<HEAD>
<title>Basic HTML Sample Page</title>
</head>
<body bgcolor="white">
<center>
<!-- Ad Google -->
<script type="text/javascript"><!--
google_ad_client = "pub-1229949690989515";
/* 728x90, created 3/31/10 */
google_ad_slot = "8460462352";
google_ad_width = 728;
google_ad_height = 90;
//-->
</script>
<script type="text/javascript"
src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
</script>

<TABLE border cellspacing=4 cellpadding=14>
<TR><TD valign=top width="48%">
<center>
<H1>A Simple Sample Web Page</H1>
<IMG SRC="http://sheldonbrown.com/images/scb_eagle_contact.jpeg">
<H4>By Sheldon Brown</H4>
<H2>Demonstrating a few HTML features</H2>
</center>

</CENTER>
HTML is really a very simple language.  It consists of ordinary text, with commands that are enclosed by "&lt;" and "&gt;" characters, or bewteen an "&" and a ";". <P>
You don't really need to know much HTML to create a page, because you can copy bits of HTML from other pages that do what you want, then change the text!<P>
This page shows on the left as it appears in your browser, and the corresponding HTML code appears on the right.  The HTML commands are linked to explanations of what they do.
<H3>Line Breaks</H3>
HTML doesn't normally use line breaks for ordinary text.  A white space of any size is treated as a single space.  This is because the author of the page has no way of knowing the size of the reader's screen, or what size type they will have their browser set for.<P>
If you want to put a line break at a particular place, you can use the "&lt;BR&gt;" command, or, for a paragraph break, the "&lt;P&gt;" command, which will insert a blank line.  The heading command ("&lt;H4&gt;&lt;/H4&gt;") puts a blank line above and below the heading text.
<H4>Starting and Stopping Commands</H4>
Most HTML commands come in pairs: for example, "&lt;H4&gt;" marks the beginning of a size 4 heading, and "&lt;/H4&gt;" marks the end of it.  The closing command is always the same as the opening command, except for the addition of the "/".<P>
Modifiers are sometimes  included along with the basic command, inside the opening command's &lt; &gt;.  The modifier does not need to be repeated in the closing command.
<H1>This is a size "1" heading</H1>
<H2>This is a size "2" heading</H2>
<H3>This is a size "3" heading</H3>
<H4>This is a size "4" heading</H4>
<H5>This is a size "5" heading</H5>
<H6>This is a size "6" heading</H6>
<center>
<H4>Copyright &copy; 1997, by <A HREF="http://sheldonbrown.com/index.html">Sheldon Brown</A></H4>

If you would like to make a link or bookmark to this page, the URL is:<BR>
http://sheldonbrown.com/web_sample1.html

<TD valign=top bgcolor="gray" width="48%"><code>
<A HREF="#head">&lt;HEAD&gt;</A><BR>
<A HREF="#title">&lt;TITLE&gt;</A>Basic HTML Sample Page<A HREF="#title">&lt;/TITLE&gt;</A><BR>
<A HREF="#head">&lt;/HEAD&gt;</A><P>
<A HREF="#body">&lt;BODY</A><A HREF="#bgcolor"> BGCOLOR="WHITE"&gt;</A><BR>
<A HREF="#center">&lt;CENTER&gt;</A><BR>
<A HREF="#h">&lt;H1&gt;</A>A Simple Sample Web Page<A HREF="#h">&lt;/H1&gt;</A><P>&nbsp;<P>&nbsp;
<A HREF="#img">&lt;IMG SRC="</A>http://sheldonbrown.com/images/scb_eagle_contact.jpeg<A HREF="#img">"&gt;</A><P>&nbsp;<P><P>&nbsp;<P>&nbsp;
<A HREF="#h">&lt;H4&gt;</A>By Sheldon Brown<A HREF="#h">&lt;/H4&gt;</A><P>
<A HREF="#h">&lt;H2&gt;</A>Demonstrating a few HTML features<A HREF="#h">&lt;/H2&gt;</A><P>
<A HREF="#center">&lt;/CENTER&gt;</A><P>
HTML is really a very simple language.  It consists of ordinary text, with commands that are enclosed by "&lt;" and "&gt;" characters, or bewteen an "&" and a ";". <A HREF="#p">&lt;P&gt;</A><BR>&nbsp;<P>

You don't really need to know much HTML to create a page, because you can copy bits of HTML from other pages that do what you want, then change the text!<A HREF="#p">&lt;P&gt;</A><BR>&nbsp;<P>

This page shows on the left as it appears in your browser, and the corresponding HTML code appears on the right.  The HTML commands are linked to explanations of what they do.<BR>&nbsp;<P>&nbsp;<P>

<A HREF="#h">&lt;H3&gt;</A>Line Breaks<A HREF="#h">&lt;/H3&gt;</A><P>

HTML doesn't normally use line breaks for ordinary text.  A white space of any size is treated as a single space.  This is because the author of the page has no way of knowing the size of the reader's screen, or what size type they will have their browser set for.<A HREF="#p">&lt;P&gt;</A><P>&nbsp;<P>

If you want to put a line break at a particular place, you can use the "<A HREF="#br">&lt;BR&gt;</A>" command, or, for a paragraph break, the "<A HREF="#p">&lt;P&gt;</A>" command, which will insert a blank line.  The heading command ("<A HREF="#h">&lt;4&gt;&lt;/4&gt;</A>") puts a blank line above and below the heading text.<P>&nbsp;<P>

<A HREF="#h">&lt;H4&gt;</A>Starting and Stopping Commands<A HREF="#h">&lt;/H4&gt;</A><P>
Most HTML commands come in pairs: for example, "<A HREF="#h">&lt;H4&gt;</A>" marks the beginning of a size 4 heading, and "<A HREF="#h">&lt;/H4&gt;</A>" marks the end of it.  The closing command is always the same as the opening command, except for the addition of the "/".<A HREF="#p">&lt;P&gt;</A><P>&nbsp;<P>

Modifiers are sometimes  included along with the basic command, inside the opening command's &lt; &gt;.  The modifier does not need to be repeated in the closing command.<P>&nbsp;<P>&nbsp;<P>

<A HREF="#h">&lt;H1&gt;</A>This is a size "1" heading<A HREF="#h">&lt;/H1&gt;</A><P>
<A HREF="#h">&lt;H2&gt;</A>This is a size "2" heading<A HREF="#h">&lt;/H2&gt;</A><P>
<A HREF="#h">&lt;H3&gt;</A>This is a size "3" heading<A HREF="#h">&lt;/H3&gt;</A><P>
<A HREF="#h">&lt;H4&gt;</A>This is a size "4" heading<A HREF="#h">&lt;/H4&gt;</A><P>
<A HREF="#h">&lt;H5&gt;</A>This is a size "5" heading<A HREF="#h">&lt;/H5&gt;</A><P>
<A HREF="#h">&lt;H6&gt;</A>This is a size "6" heading<A HREF="#h">&lt;/H6&gt;</A><P>
<A HREF="#center">&lt;center&gt;</A><P>
<A HREF="#h">&lt;H4&gt;</A>Copyright &copy; 1997, by<BR> <A HREF="#href">&lt;A HREF="</A>http://sheldonbrown.com/index.html<A HREF="#href">"&gt;</A>Sheldon Brown<A HREF="#href">&lt;/A&gt;</A><A HREF="#h"><BR>&lt;/H4&gt;</A><P>

If you would like to make a link or bookmark to this page, the URL is:<A HREF="#br">&lt;BR&gt;</A>
http://sheldonbrown.com/web_sample1.html<A HREF="#body">&lt;/body&gt;</A>

</TABLE>
</code></CENTER>

<!-- ************ DEFINITIONS ************ -->

<BLOCKQUOTE><DL>
<A NAME="head"> </A>&nbsp;<P>&nbsp;<P>
<DT><H3>&lt;HEAD&gt;&lt;/HEAD&gt;</H3>
<DD>The "<B>&lt;HEAD&gt;&lt;/HEAD&gt;</B>" part of the document does not show in the main <A HREF="web_glossary.html#browser">browser</A> window, but it can give useful information to browsers or to <A HREF="web_glossary.html#searchengine">search engines</A>.  <P>&nbsp;<P>&nbsp;<P>

<A NAME="title"> </A>&nbsp;<P>&nbsp;<P>
<DT><H3>&lt;TITLE&gt;&lt;/TITLE&gt;</H3>
<DD>The only part of the "<A HREF="#head"><B>&lt;HEAD&gt;&lt;/HEAD&gt;</B></A>" that is normally visible is the <B>&lt;TITLE&gt;</B><CODE>Basic HTML Sample Page</CODE><B>&lt;/TITLE&gt;</B>, which usually appears in a small window on top of the browser screen.  When users add your site to their hotlists, the <B>&lt;TITLE&gt;</B><B>&lt;/TITLE&gt;</B> you have chosen will appear on their hotlists. <P>&nbsp;<P>&nbsp;<P>

<A NAME="body"> </A>&nbsp;<P>&nbsp;<P>
<DT><H3>&lt;BODY&gt;&lt;/BODY&gt;</H3>
<DD>The &lt;BODY&gt; is the main part of the page.  It is good practice to use a &lt;/BODY&gt; at the end of the page, but this is not absolutely necessary.<P>&nbsp;<P>&nbsp;<P>

<A NAME="bgcolor"></A><P>&nbsp;<P>&nbsp;<P>
<BLOCKQUOTE><DT><H3>&lt;BODY BGCOLOR="WHITE"&gt;&lt;/BODY&gt;</H3>
<DD><B>BGCOLOR=""</B> is a sub command of the<B> &lt;BODY&gt;</B> command.  It specifies the background color for the page.  The color may be specified by name, or by a <A HREF="web_glossary.html#hextriplet">hex triplet number</A>.<P>&nbsp;<P>&nbsp;<P>
</BLOCKQUOTE>

<A NAME="center"></A>&nbsp;<P>&nbsp;<P>
<DT><H3>&lt;CENTER&gt;&lt;/CENTER&gt;</H3>
<DD>The <B>&lt;CENTER&gt;</B> command causes everything following it to be centered on the page, until it is canceled by the use of the <B>&lt;/CENTER&gt;</B> command, which causes everything to be <A HREF="web_glossary.html#justified">left-justified</A>.<P>&nbsp;<P>&nbsp;<P>

<A NAME="img"></A>&nbsp;<P>&nbsp;<P>
<DT><H3>&lt;IMG SRC="..."&gt;</H3>
<DD>The <B>&lt;IMG SRC="</B><CODE>http://sheldonbrown.com/images/scb_eagle_contact.jpeg</CODE><B>"&gt;</B> command places an image on the screen.  The part between the quotation marks is the <A HREF="web_glossary.html#url">URL</A> or partial address of the image.  In this case, the image called for is called "<CODE>scb_eagle_contact.jpeg</CODE>", and it resides on the subdirectory of my sheldonbrown.com site that I call "<CODE>images</CODE>".<P>
Since the <CODE>image</CODE> subdirectory is in the same directory as this document, I could have used the  relative address:<BR>
&nbsp; <code>images/scb_eagle_contact.jpeg</code><BR> instead of the full URL:<BR>
&nbsp; <code>http://sheldonbrown.com/images/scb_eagle_contact.jpeg</code>&nbsp;<P>&nbsp;<P>

<A NAME="h"></A><P>&nbsp;<P>&nbsp;<P>
<DT><H3>&lt;H1&gt;&lt;/H1&gt;...&lt;H6&gt;&lt;/H6&gt;</H3>
<DD>There are 6 levels of headings available in HTML,<B> &lt;H1&gt;&lt;/H1&gt;</B> being the largest, <B>&lt;H6&gt;&lt;/H6&gt;</B> the smallest.  Headings are automatically in bold-face type, and automatically have a blank line above and below them.<P>&nbsp;<P>&nbsp;<P>

<A NAME="p"></A><P>&nbsp;<P>&nbsp;<P>
<DT><H3>&lt;P&gt;</H3>
<DD><B>&lt;P&gt;</B> creates a paragraph break.  It forces a return and two line feeds, so there will be a blank line between whatever preceded the <B>&lt;P&gt;</B> and the next line.<P>&nbsp;<P>&nbsp;<P>

<A NAME="href"></A><P>&nbsp;<P>&nbsp;<P>

<DT><H3>&lt;A HREF="..."&gt;...&lt;/A&gt;</H3>
<DD><B>&lt;A HREF="</B><CODE>[destination url]</CODE><B>"&gt;</B><CODE>[highlighted text/image]</CODE><B>&lt;/A&gt;</B> is the command for a hyperlink.  The <A HREF="web_sample1.html#href2"><CODE>[highlighted text/image]</CODE></A> appears in the document, normally underlined and in the highlight color.  When you click on the <A HREF="web_sample1.html#href2"><CODE>[highlighted text/image]</CODE></A> your browser takes you off to the site <CODE>[destination url]</CODE>.

<P>The destination site would begin with "<CODE>http://www.</CODE>" if the site is an external one, on a different server.  If the destination is on the same machine, a "relative" URL may be used, just giving the file name (<CODE>web_sample1.html</CODE>, for instance) if the file is in the same subdirectory, or a path plus file name (<CODE>webstuff/web_sample1.html</CODE>, for instance) if the file is elsewhere on the same machine as the page with the link.

<P>Otpionally, the <CODE>[destination url]</CODE> may end with <CODE>#[anchor]</CODE> if it is to point to an <A HREF="web_glossary.html#anchor">anchor</A> point in the destination page, rather than to the top of the page.<P>&nbsp;<P>&nbsp;<P>

<A NAME="br"></A><P>&nbsp;<P>&nbsp;<P>

<DT><H3>&lt;BR&gt;</H3>
<DD><B>&lt;BR&gt;</B> creates a line break.  It forces a return and a single line feed.  It is similar to <A HREF="web_sample1.html#p">&lt;P&gt;</A>, except that it does not put a blank line between the two lines.<P>&nbsp;<P>&nbsp;<P>
</DL>
</BLOCKQUOTE>
<HR>
<center>
<A HREF="index.html"><H2>Sheldon Brown Web Services</H2></A>
<H4>Copyright &copy; 1997, by <A HREF="http://sheldonbrown.com/index.html">Sheldon Brown</A></H4>
<!-- old counterweb_sample1.cnt"  --> since September 30, 1997<H2><!-- http://automaticlabs.com/cgi-bin/index.cgi
<H3><script language="javascript">
function hiveware_enkoder(){var i,j,x,y,x=
"x=\"783d22793e2337353767373438363765373637663835336638383833373a3835373633" +
"393333346437323331373938333736373734653664333337653732373a3764383537673462" +
"37343732383138353533373a37633736353138343739373637643735376737663733383337" +
"67383837663366373437673765346738343836373337623736373438353465373738333767" +
"376533313638373637333834373a38353736333135653732373a3764383537673664333333" +
"313835373a383537643736346536643333373635653732373a376433313634373937363764" +
"37353767376633313533383337673838376636643333346636343736376637353331373635" +
"653732373a3764333138353767333136343739373637643735376737663331353338333767" +
"3838376634643367373234663333333a346334313463233c7a3e28283c677073296a3e313c" +
"6a3d792f6d666f6875693c6a2c3e332a7c7a2c3e766f667464627166292826282c792f7476" +
"63747573296a2d332a2a3c7e7a223b793d27273b783d756e6573636170652878293b666f72" +
"28693d303b693c782e6c656e6774683b692b2b297b6a3d782e63686172436f646541742869" +
"292d313b6966286a3c3332296a2b3d39343b792b3d537472696e672e66726f6d4368617243" +
"6f6465286a297d79\";y='';for(i=0;i<x.length;i+=2){y+=unescape('%'+x.substr(" +
"i,2));}y";
while(x=eval(x));}hiveware_enkoder();
</script><H3> (Sheldon Brown)</H2>-->

Updated  Sunday, May 7, 2000</H2>

If you would like to make a link or bookmark to this page, the URL is:<BR>
http://sheldonbrown.com/web_sample1.html</body>
    """

  }
    def test(): JValue = {

      List(2, 45, 34, 23, 7, 5, 3)
    }

}
