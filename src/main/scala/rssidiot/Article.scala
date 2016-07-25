package rssidiot
import rssidiot.Types._
import scala.xml.NodeSeq

class Article(val url:Url,
              val title:Title,
              var read:Boolean = false) 
    extends JsonSerializable  {

    def ==(that:Article):Boolean = 
        if(that != null) this.url == that.url
        else false

    def unread = !this.read
    def markAsRead() {this.read = true}

    private def quote(s:String) = "\"" + s + "\""
    def jsonString() = "{" + 
        "\"url\":" + quote(url) + ","+
        "\"title\":" + quote(title) + "," +
        "\"read\":" + this.read + 
    "}"
}
object Article {
    def fromXmlItem(item:NodeSeq):Article = {
        //extract relevant data from the xml <item>
        //filter out double quotes so that they do not disturb serialization
        val title = (item \ "title").text.filter(x => x != '"')
        val url = (item \ "link").text
        return new Article(url,title)
    }
    def fromJsonElement(value:net.liftweb.json.JsonAST.JValue):Article = {
        implicit val formats = net.liftweb.json.DefaultFormats
        val obj = value.asInstanceOf[net.liftweb.json.JsonAST.JObject]
        val res = new Article(url = (obj \ "url").extract[String],
                              title = (obj \ "title").extract[String],
                              read  = (obj \ "read").extract[Boolean])
        return res
    }
}
