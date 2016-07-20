import org.scalatest.FlatSpec
import rssidiot.Feed

class FeedSpec extends FlatSpec {
    "A Feed" should "be able to fetch all of its new articles" in {
        val feed = new Feed(url="https://matthhan.github.io/rssidiot/test.rss",name="Testfeed")
        assert(!feed.hasNewArticles)
        feed.fetchNewArticles
        assert(feed.hasNewArticles)
    }
}
