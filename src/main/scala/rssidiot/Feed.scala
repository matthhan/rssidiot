package rssidiot
import rssidiot.Types._
class Feed(val url:Url,
           val name:String,
           val historySize:Int = 100,
           private var articleBuffer:ArticleBuffer = null) 
              extends JsonSerializable {
    require(!(name contains '"'))
    require(!(url contains '"'))
    require(!(url.isEmpty))
    require(!(name.isEmpty))
    require(historySize > 0)
    if(articleBuffer != null) {
        require(articleBuffer.size == this.historySize)
    }
    else {
        articleBuffer = 
            new ArticleBuffer(historySize) 
    }

    def articles() = articleBuffer.asArray

    def fetchNewArticles() {
        insertIntoBuffer(downloadNewArticles)
    }
    private def downloadNewArticles:List[Article] = 
        WebContentFetcher.fetchContentFrom(this.url)
            .map(Article.fromXmlItem)
            .toList
    private def insertIntoBuffer(newItems:List[Article]) { 
        this.articleBuffer ++= newItems.filter(article => !(this.articles contains article))
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
    def valid() = (articleBuffer.asArray.length > 0) || (downloadNewArticles.length > 0) 
}

object Feed {
    def fromJsonElement(value:net.liftweb.json.JsonAST.JValue):Feed = {
        implicit val formats = net.liftweb.json.DefaultFormats
        val obj = value.asInstanceOf[net.liftweb.json.JsonAST.JObject] 
        val res = new Feed(url = (obj \ "url").extract[String],
                           name = (obj \ "name").extract[String],
                           historySize = (obj \ "historySize").extract[Int])
        val articleBuffer = ArticleBuffer.
                                fromJsonElement(obj \ "articleBuffer")
        return res    
    }
}
