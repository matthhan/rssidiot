package rssidiot

import JsonLibraryAdapter._

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
        Utility.writeStringToFile(s = this.json,filename = filename)
    }
    def json():String = "[" + this.feeds.map(_.json).reduce((x,y) => x + "," + y) + "]"
}
object FeedDatabase {
    def loadFrom(filename:String):FeedDatabase = { 
        val str = Utility.readStringFromFile(filename)
        return FeedDatabase.fromJson(str)
    }
    private def fromJson(json:String):FeedDatabase = {
        val ast = JsonLibraryAdapter.parse(json)
        val res = new FeedDatabase()
        ast.children.map(feedJson => Feed.fromJson(feedJson.json)).foreach(res += _)
        return res
    }
}
