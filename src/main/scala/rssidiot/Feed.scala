package rssidiot
import rssidiot.Types._
import rssidiot.JsonLibraryAdapter._
class Feed(val url:QuotelessString,
           val name:QuotelessString,
           val historySize:Int = 100,
           private var articleBuffer:ArticleBuffer = null) 
              {
    require(url != null)
    require(name != null)
    require(!(url.isEmpty))
    require(!(name.isEmpty))
    require(historySize > 0)

    if(articleBuffer != null) require(articleBuffer.size == this.historySize)
    else articleBuffer = new ArticleBuffer(historySize) 

    def articles = articleBuffer.asArray

    private def downloadNewArticles:scala.xml.Elem = WebContentFetcher.fetchContentFrom(this.url)
    private def parseArticles(xml:scala.xml.Elem) =
        if(xml == null) List[Article]()
        else 
            //"item" is used in RSS feeds, "entry" is used in Atom feeds
            ((xml \\ "item") ++ (xml \\ "entry")).map(Article.fromXmlItem).toList

    private def insertIntoBuffer(newItems:List[Article]) { 
        newItems.foreach(a => if(!(this.articles contains a)) articleBuffer += a)
    }
    def fetchNewArticles() { insertIntoBuffer(parseArticles(downloadNewArticles)) }

    def unreadArticles = this.articles.filter(_.unread)

    private def quote(s:String):String = "\"" + s + "\""
    def json():String = 
        "{" + 
          "\"url\":" +  quote(this.url) + ","  +
          "\"name\":" + quote(this.name) + "," +
          "\"historySize\":" + this.historySize + "," +
          "\"articleBuffer\":" + this.articleBuffer.json+ 
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
    def fromJson(json:String):Feed = {
        val jsonObject = JsonLibraryAdapter.parse(json)
        val res = new Feed(
            url = jsonObject.getAttribute[String]("url"),
            name = jsonObject.getAttribute[String]("name"),
            historySize = jsonObject.getAttribute[Int]("historySize"),
            articleBuffer = ArticleBuffer.fromJson(jsonObject.getChild("articleBuffer").json)) 
        return res    
    }
}


