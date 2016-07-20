import org.scalatest.FlatSpec
import rssidiot.FeedDatabase
import rssidiot.Feed

class FeedDatabaseSpec extends FlatSpec {
    "A Feed Database" should "be able to store several Feeds" in {
        val db = new FeedDatabase
        db += new  Feed(url= "http://www.reddit.com/.rss",name="Reddit frontpage")
        db += new  Feed(url= "http://www.tagesschau.de/xml/rss2",name="Tagesschau")
        assertResult(2) {db.listFeedNames.length}
    }
    "A Feed Database" should "be able to correctly save and load itself" in {
        val db = new FeedDatabase
        db += new  Feed(url= "https://matthhan.github.io/rssidiot/test.rss",name="Test Feed")
        db.fetchAllNewArticles
        //print(db.jsonString)
        //db.saveTo("example.feeddb")
        //val loadedDb = FeedDatabase.loadFrom("example.feeddb")
        //assertResult(List("Tagesschau","Reddit frontpage")) { loadedDb.listFeedNames }
    }
}
