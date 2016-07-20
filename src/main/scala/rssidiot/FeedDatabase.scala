package rssidiot

import scala.collection.Map
import scala.collection.immutable.ListMap
class FeedDatabase {
    private var feeds = List[Feed]()
    def saveTo(filename:String) { //TODO: implement 
    }
    def add(feed:Feed) { if(! (feeds contains feed)) feeds = (feed :: feeds) }
    def +=(that:Feed) {this add that}
    def fetchAllNewArticles() { feeds foreach (_.fetchNewArticles) }
    def listFeedNames():List[String] = feeds map(_.name)
    def getUnreadArticlesForFeed(i:Int) = feeds(i).unreadArticles
}
object FeedDatabase {
    def loadFrom(filename:String) { //TODO:implement 
    }
}
