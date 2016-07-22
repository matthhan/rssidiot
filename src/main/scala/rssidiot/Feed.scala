package rssidiot
import rssidiot.Types._
import rssidiot.collection.SerializableCircularBuffer
class Feed(val url:Url,val name:String,val historySize:Int = 100) extends JsonSerializable {
    require(!(name contains '"'))
    require(!(url contains '"'))
    private val articleBuffer = new SerializableCircularBuffer[Article](historySize)
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
}
