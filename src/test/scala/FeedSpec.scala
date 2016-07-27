import org.scalatest.FlatSpec
import rssidiot.Feed
import scala.language.reflectiveCalls

class FeedSpec extends FlatSpec {
    def fixture = new {
        val feed = new Feed(url="https://matthhan.github.io/rssidiot/test.rss",name="Testfeed")
    }
    "A Feed" should "be able to fetch all of its new articles" in {
        val f = fixture
        f.feed.fetchNewArticles
        assert(!f.feed.articles.isEmpty)
    }
    it should "be able to return exactly the unread articles" in {
        val f = fixture
        f.feed.fetchNewArticles
        f.feed.articles(1).markAsRead
        assertResult(2) { f.feed.unreadArticles.length }
        assertResult(List("New RSS Reader on the block", "Maybe try it out?")) { f.feed.unreadArticles.map(_.title) }
    }
    it should "be able to serialize itself into a valid json string" in {
        val f = fixture
        net.liftweb.json.parse(f.feed.json)
    }
    //This is necessary because otherwise, json serialization will be hard
    it should "Not be able to have a title or url that contains double quotes" in {
        val feed1 = new Feed(url="lol\"",name="defaultname")
        assertResult("lol") {feed1.url}
        val feed2 = new Feed(url="http://www.standardurl.com",name="\"defaul\"tname")
        assertResult("defaultname") {feed2.name}
    }
    it should "Be able to check for validity" in {
        val f = fixture
        assert(f.feed.valid)
        assert(f.feed.valid)
        assert(!(new Feed("Bla","Blub")).valid)
    }
}
