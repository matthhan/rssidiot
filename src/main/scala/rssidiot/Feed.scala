package rssidiot
import rssidiot.Types._
import rssidiot.collection.CircularBuffer
class Feed(val url:Url,val name:String,val historySize:Int = 100) {
    private val articleBuffer = new CircularBuffer[Article](historySize)
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
}
