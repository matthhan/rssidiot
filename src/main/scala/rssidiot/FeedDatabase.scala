package rssidiot

import java.io.PrintWriter

class FeedDatabase extends JsonSerializable{
    private var feeds = List[Feed]()
    def add(feed:Feed) { if(! (feeds contains feed)) feeds = (feed :: feeds)}
    def +=(that:Feed) {this add that}
    def fetchAllNewArticles() { feeds foreach (_.fetchNewArticles) }
    def listFeedNames():List[String] = feeds map(_.name)
    def getUnreadArticlesForFeed(i:Int) = feeds(i).unreadArticles

    def saveTo(filename:String) { 
        val str = this.jsonString
        new PrintWriter(filename) {write(str);close}
    }
    def jsonString():String = "[" + this.feeds.map(_.jsonString).reduce((x,y) => x + "," + y) + "]"
}
object FeedDatabase {
    def loadFrom(filename:String):FeedDatabase = { 
        val str = scala.io.Source.fromFile(filename).mkString
        return FeedDatabase.parseFromJsonString(str)
    }
    def parseFromJsonString(json:String):FeedDatabase = {
        //TODO:implement
        null
    }
}
