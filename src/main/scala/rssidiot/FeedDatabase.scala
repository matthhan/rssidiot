package rssidiot

import net.liftweb.json._

class FeedDatabase {
    private var feeds = List[Feed]()
    def add(feed:Feed) { 
        if(! (feeds contains feed)) {
            feeds = (feed :: feeds)
        }
    }
    def +=(that:Feed) {this add that}
    def fetchAllNewArticles() { feeds foreach (_.fetchNewArticles) }
    def listFeeds():List[Feed] = feeds
    def remove(f:Feed) {feeds = feeds filterNot(_ == f)}
    def saveTo(filename:String) { 
        Utility.writeStringToFile(s = this.jsonString,filename = filename)
    }
    def jsonString():String = "[" + this.feeds.map(_.jsonString).reduce((x,y) => x + "," + y) + "]"
}
object FeedDatabase {
    def loadFrom(filename:String):FeedDatabase = { 
        val str = Utility.readStringFromFile(filename)
        return FeedDatabase.parseFromJsonString(str)
    }
    private def parseFromJsonString(json:String):FeedDatabase = {
        val ast = net.liftweb.json.parse(json)
        val res = new FeedDatabase()
        ast.children.map(Feed.fromJsonElement(_)).foreach(res += _)
        return res
    }
}
