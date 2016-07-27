package rssidiot
import rssidiot.Types._
import scala.xml.NodeSeq
import rssidiot.JsonLibraryAdapter._

class Article(val url:QuotelessString,
              val title:QuotelessString,
              var read:Boolean = false) {


    require(url != null)
    require(title != null)

    require(url != "")
    require(title != "")

    override def equals(x:Any):Boolean = x match{
        case null => false
        case that:Article => this.url == that.url
        case _ => false
    }

    def unread = !this.read
    def markAsRead {this.read = true}
    private def quote(s:String) = "\"" + s + "\""
    def json() = "{" + 
        quote("url")   + ":" + quote(url)   + "," +
        quote("title") + ":" + quote(title) + "," +
        quote("read")  + ":" + this.read + 
    "}"
}


object Article {
    def fromXmlItem(item:NodeSeq):Article = {
        //filter out double quotes so that they do not disturb serialization
        val title = (item \ "title").text
        var url = (item \ "link").text
        //Atom feeds like to store this as href Attribute
        if(url == "") url = (item \ "link" \ "@href").text
        return new Article(url,title)
    }
    def fromJson(json:String):Article = {
        val jsonObject = JsonLibraryAdapter.parse(json)
        val res = new Article(jsonObject.getAttribute[String]("url"),
                              jsonObject.getAttribute[String]("title"),
                              jsonObject.getAttribute[Boolean]("read"))
        return res
    }
}
