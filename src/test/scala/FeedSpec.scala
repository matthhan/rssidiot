import org.scalatest.FlatSpec
import rssidiot.Feed

class FeedSpec extends FlatSpec {
    "A Feed" should "be able to fetch all of its new articles" in {
        val feed = new Feed(url="https://matthhan.github.io/rssidiot/test.rss",name="Testfeed")
        assert(!feed.hasNewArticles)
        feed.fetchNewArticles
        assert(feed.hasNewArticles)
    }
    it should "be able to return exactly the unread articles" in {
        val feed = new Feed(url="https://matthhan.github.io/rssidiot/test.rss",name="Testfeed")
        feed.fetchNewArticles
        feed.articles()(1).markAsRead
        assertResult(2) { feed.unreadArticles.length }
        assertResult(List("New RSS Reader on the block", "Maybe try it out?")) { feed.unreadArticles.map(_.title) }
    }
    it should "be able to serialize itself into a valid json string" in {
        val feed = new Feed(url="https://matthhan.github.io/rssidiot/test.rss",name="Testfeed")
        net.liftweb.json.parse(feed.json)
    }
    //This is necessary because otherwise, json serialization will be hard
    it should "Not be able to have a title or url that contains double quotes" in {
        val feed = new Feed(url="lol\"",name="defaultname")
        assertResult("lol") {feed.url}
        val feed2 = new Feed(url="http://www.standardurl.com",name="\"defaul\"tname")
        assertResult("defaultname") {feed2.name}
    }
}
