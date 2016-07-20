package rssidiot
import rssidiot.Types._
import rssidiot.collection.CircularBuffer
class Feed(val url:Url,val name:String,val historySize:Int = 100) {
    val articles = new CircularBuffer[Article](historySize)
    def fetchNewArticles() {
        val items = WebContentFetcher.fetchContentFrom(this.url)
        val newArticles = items.map(Article.fromXmlItem)
        val oldArticles = this.articles.asArray
        newArticles.foreach(article => 
            if (! (oldArticles contains article)) this.articles += article
        )
    }
    def hasNewArticles =  !(this.articles.asArray.filter(_.unread).isEmpty)
}
