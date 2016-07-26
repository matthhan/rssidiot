package rssidiot
import rssidiot.Types._
class Feed(val url:Url,
           val name:String,
           val historySize:Int = 100,
           private var articleBuffer:ArticleBuffer = null) 
              extends JsonSerializable {
    require(!(name contains '"'))
    require(!(url contains '"'))
    require(url != null)
    require(name != null)
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

    private def downloadNewArticles:List[Article] = {
        val content = WebContentFetcher.fetchContentFrom(this.url)
        if(content == null) List[Article]()
        else (content \\ "item")
                .map(Article.fromXmlItem)
                .toList
    }
    private def insertIntoBuffer(newItems:List[Article]) { 
        newItems.foreach(a => if(!(this.articles contains a)) articleBuffer += a)
    }
    def fetchNewArticles() {
        insertIntoBuffer(downloadNewArticles)
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
    def valid:Boolean = {
        //A feed is valid if we have already downloaded articles for it successfully 
        //or if we can do so now
        try {
            return (articleBuffer.asArray.length > 0) || (downloadNewArticles.length > 0) 
        } catch {
            //Something went wrong downloading new articles
            case e:Exception => return false
        }
    }
}

object Feed {
    def fromJsonElement(value:net.liftweb.json.JsonAST.JValue):Feed = {
        implicit val formats = net.liftweb.json.DefaultFormats
        val obj = value.asInstanceOf[net.liftweb.json.JsonAST.JObject] 
        val res = new Feed(url = (obj \ "url").extract[String],
                           name = (obj \ "name").extract[String],
                           historySize = (obj \ "historySize").extract[Int],
                           articleBuffer = ArticleBuffer.fromJsonElement(obj \ "articleBuffer"))
        return res    
    }
}


