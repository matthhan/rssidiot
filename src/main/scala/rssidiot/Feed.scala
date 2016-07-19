package rssidiot
import rssidiot.Types._
import rssidiot.collection.CircularBuffer
class Feed(val url:Url,var name:String,val historySize:Int = 100) {
   val articles = new CircularBuffer[Article](historySize)
   def fetchNewArticles() {
        
   }
}
