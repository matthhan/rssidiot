package rssidiot
import rssidiot.Types._
import rssidiot.collection.CircularBuffer
class Feed(val url:Url,val name:String,val historySize:Int = 100) {
    val articles = new CircularBuffer[Article](historySize)
    def fetchNewArticles() {
        val items = WebContentFetcher.fetchContentFrom(this.url)
        val articles = items.map(Article.fromXmlItem)
        println(articles)
    }
}
