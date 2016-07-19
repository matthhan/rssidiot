package rssidiot
import rssidiot.Types._
import scala.xml.NodeSeq

class Article(val url:Url,val title:Title) {


    def printableString(length:Int = 75):String = 
        if(this.title != null) this.title.take(length) 
        else "(null)"


    def ==(that:Article):Boolean = 
        if(that != null) this.url == that.url
        else false


    private var _read = false
    def read = this._read
    def markAsRead() {
        this._read = true
    }
}
object Article {
    def fromXmlItem(item:NodeSeq):Article = {
        val title = (item \ "title").text
        val url = (item \ "link").text
        return new Article(url,title)
    }
}
