package rssidiot
import rssidiot.Types._
import scala.xml.NodeSeq
import rssidiot.JsonLibraryAdapter._

class Article(val url:Url,
              private val _title:Title,
              var read:Boolean = false) 
    {
    require(!(url contains '"'))
    require(url != null)
    require(_title != null)
    require(url != "")
    require(_title != "")

    def title() = this._title.filter(_ != '"')
    override def equals(that:Any):Boolean = {
        if(that == null || !that.isInstanceOf[Article]) false
        else this.url == that.asInstanceOf[Article].url 
    }
    def unread = !this.read
    def markAsRead() {this.read = true}

    private def quote(s:String) = "\"" + s + "\""
    def json() = "{" + 
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
        var url = (item \ "link").text
        //Atom feeds like to store this as href Attribute
        if(url == "") url = (item \ "link" \ "@href").text
        return new Article(url,title)
    }
    def fromJson(json:String):Article = {
        val jsonObject = JsonLibraryAdapter.parse(json)
        val res = new Article(url = jsonObject.getAttribute[String]("url"),
                              _title = jsonObject.getAttribute[String]("title"),
                              read  = jsonObject.getAttribute[Boolean]("read"))
        return res
    }
}
