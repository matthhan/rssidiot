import org.scalatest.FlatSpec
import rssidiot.FeedDatabase
import rssidiot.Feed
import rssidiot.JsonLibraryAdapter
import java.io.File
import scala.language.reflectiveCalls

class FeedDatabaseSpec extends FlatSpec {
    def fixture = new {
        val db = new FeedDatabase
        db += new  Feed(url= "https://matthhan.github.io/rssidiot/test.rss",name="Test Feed")
        db += new  Feed(url= "http://www.tagesschau.de/xml/rss2",name="Tagesschau")
        db.fetchAllNewArticles
    }
    "A Feed Database" should "be able to store several Feeds" in {
        val f = fixture
        assertResult(2) {f.db.listFeeds.length}
    }
    it should "be able to serialize itself into valid json" in {
        val f = fixture
        JsonLibraryAdapter.parse(f.db.json)
    }
    it should "be able to correctly save and load itself" in {
        val f = fixture
        val file = new File("example.feeddb") 
        assume(!file.exists)
        f.db.saveTo("example.feeddb")
        val loadedDb = FeedDatabase.loadFrom("example.feeddb")
        assertResult(List("Test Feed","Tagesschau")) { loadedDb.listFeeds.map(_.name) }
        file.delete
    }
    it should "Not allow inserting the exact same Feed twice" in {
        //Feeds with the same url and title are allowed though
        val f = fixture
        val feed = new Feed(url= "https://matthhan.github.io/rssidiot/test.rss",name="Test Feed")
        f.db += feed
        assertResult(3) {f.db.listFeeds.length}
        f.db += feed
        assertResult(3) {f.db.listFeeds.length}
    }
    it should "Be able to remove feeds from its list" in {
        val f = fixture
        assert(f.db.listFeeds(0).name == "Tagesschau")
        f.db.remove(f.db.listFeeds(1))
        assertResult(1) {f.db.listFeeds.length}
        assert(f.db.listFeeds(0).name=="Tagesschau")
    }

}
