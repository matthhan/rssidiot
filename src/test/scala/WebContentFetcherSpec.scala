import org.scalatest.FlatSpec
import rssidiot.WebContentFetcher

class WebContentFetcherSpec extends FlatSpec {
    "A WebContentFetcher" should "be able to fetch items from a given URL" in {
        val items = WebContentFetcher.fetchContentFrom("https://news.ycombinator.com/rss")
        assert(items.length != 0)
    }
}
