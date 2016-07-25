import org.scalatest.FlatSpec
import rssidiot.FeedDatabase
import rssidiot.Feed

class FeedDatabaseSpec extends FlatSpec {
    val db = new FeedDatabase
    db += new  Feed(url= "https://matthhan.github.io/rssidiot/test.rss",name="Test Feed")
    db += new  Feed(url= "http://www.tagesschau.de/xml/rss2",name="Tagesschau")
    db.fetchAllNewArticles
    "A Feed Database" should "be able to store several Feeds" in {
        assertResult(2) {db.listFeeds.length}
    }
    "A Feed Database" should "be able to serialize itself into valid json" in {
        net.liftweb.json.parse(db.jsonString)
    }
    "A Feed Database" should "be able to correctly save and load itself" in {
        db.saveTo("example.feeddb")
        val loadedDb = FeedDatabase.loadFrom("example.feeddb")
        assertResult(List("Test Feed","Tagesschau")) { loadedDb.listFeeds.map(_.name) }
    }
}
