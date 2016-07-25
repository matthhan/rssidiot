package rssidiot
import rssidiot.Types._
import rssidiot.collection.SerializableCircularBuffer
class Feed(val url:Url,
           val name:String,
           val historySize:Int = 100,
           private var articleBuffer:SerializableCircularBuffer[Article] = null) 
              extends JsonSerializable {
    require(!(name contains '"'))
    require(!(url contains '"'))
    if(articleBuffer != null) {
        require(articleBuffer.size == this.historySize)
    }
    else {
        articleBuffer = 
            new SerializableCircularBuffer[Article](historySize) 
    }

    def articles() = articleBuffer.asArray

    def fetchNewArticles() {
        val items = WebContentFetcher.fetchContentFrom(this.url)
        val newArticles = items.map(Article.fromXmlItem)
        val oldArticles = this.articles
        newArticles.foreach(article => 
            if (! (oldArticles contains article)) this.articleBuffer += article
        )
    }

    def hasNewArticles =  !(this.articles.filter(_.unread).isEmpty)

    def unreadArticles = this.articles.filter(_.unread)

    private def quote(s:String):String = "\"" + s + "\""
    def jsonString():String = 
        "{" + 
          "\"url\":" +  quote(this.url) + ","  +
          "\"name\":" + quote(this.name) + "," +
          "\"historySize\":" + this.historySize + "," +
          "\"articleBuffer\":" + this.articleBuffer.jsonString + 
        "}"
    //TODO:implement
    def valid() = true
}

object Feed {
    def fromJsonElement(value:net.liftweb.json.JsonAST.JValue):Feed = {
        implicit val formats = net.liftweb.json.DefaultFormats
        val obj = value.asInstanceOf[net.liftweb.json.JsonAST.JObject] 
        val res = new Feed(url = (obj \ "url").extract[String],
                           name = (obj \ "name").extract[String],
                           historySize = (obj \ "historySize").extract[Int])
        val articleBuffer = SerializableCircularBuffer.
                                fromJsonElement(obj \ "articleBuffer")
        return res    
    }
}
