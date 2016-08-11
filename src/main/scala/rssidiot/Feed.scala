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
            quote("url")           + ":" + quote(this.url) + ","   +
            quote("name")          + ":" + quote(this.name) + ","  +
            quote("historySize")   + ":" + this.historySize + ","  +
            quote("articleBuffer") + ":" + this.articleBuffer.json + 
        "}"
    def valid:Boolean = {
        //A feed is valid if we have already downloaded articles for it successfully 
        //or if we can do so now
        try {
            return ((articleBuffer.asArray.length > 0) || 
                    (this.parseArticles(this.downloadNewArticles).length > 0)) 
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
    def withAutodiscovery(name:QuotelessString,url:QuotelessString):List[Feed] = {
        try {
            val feed = new Feed(url,name)
            if(!feed.valid) throw new IllegalArgumentException("name or url not valid")
            return List(feed)
        } catch {
            //Could be thrown from feed creation or directly from try block
            case e:IllegalArgumentException => return autodiscoverFeeds(name,url)
        }
    }
    private def autodiscoverFeeds(name:QuotelessString,url:QuotelessString):List[Feed] = {
        autodiscoverUrls(url).flatMap{ discoveredUrl =>
            try {
                val feed = new Feed(discoveredUrl,name)
                if(feed.valid) Some(feed) else None
            } catch {
                case e:IllegalArgumentException => None
            }
        }.toList
    }
    private def feedContentTypes = Array("application/rss+xml","application/atom+xml")
    private def autodiscoverUrls(url:String):Seq[String] = {
        import org.htmlcleaner.HtmlCleaner
        (new HtmlCleaner)
            .clean(new java.net.URL(url))
            .getElementsByName("link",true)
            .filter(feedContentTypes contains _.getAttributeByName("type"))
            .map(_.getAttributeByName("href"))
            .map(discovered => if(discovered.startsWith("http")) discovered 
                               else if(url.last == '/') url + discovered
                               else url + '/' + discovered)
    }
}
