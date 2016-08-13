package rssidiot

import JsonLibraryAdapter._

class FeedDatabase {
  private var feeds = Vector[Feed]()
  def add(feed: Feed) {
    if (!(feeds contains feed)) feeds = (feed +: feeds)
  }
  def +=(that: Feed) { this add that }
  //The par means convert to a parallel collection that holds references to
  //the same objects. Then foreach is executed in parallel. This yields
  //speedup while fetching new articles (since that is IO bound)
  def fetchAllNewArticles { feeds.par.foreach(_.fetchNewArticles) }
  def listFeeds: List[Feed] = feeds.toList
  def remove(f: Feed) { feeds = feeds filterNot (_ == f) }
  def saveTo(filename: String) {
    Utility.writeStringToFile(s = this.json, filename = filename)
  }
  def json: String = "[" + this.feeds.map(_.json).reduce((x, y) => x + "," + y) + "]"
  def sort: Unit = {
    this.feeds = this.feeds.sortWith((a, b) => a.name < b.name)
  }
}
object FeedDatabase {
  def loadFrom(filename: String): FeedDatabase = {
    val str = Utility.readStringFromFile(filename)
    return FeedDatabase.fromJson(str)
  }
  private def fromJson(json: String): FeedDatabase = {
    val res = new FeedDatabase()
    val feedJsons = JsonLibraryAdapter.parse(json).children.map(_.json)
    feedJsons.map(Feed.fromJson(_)).foreach(res += _)
    return res
  }
}
