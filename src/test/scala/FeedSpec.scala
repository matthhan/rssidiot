import org.scalatest.FlatSpec
import rssidiot.Feed

class FeedSpec extends FlatSpec {
    "A Feed" should "be able to fetch all of its new articles" in {
        val feed = new Feed(url="https://www.tagesschau.de/xml/rss2",name="Tagesschau")
        assert(!feed.hasNewArticles)
        feed.fetchNewArticles
        assert(feed.hasNewArticles)
    }
}
