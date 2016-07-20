import org.scalatest.FlatSpec
import rssidiot.WebContentFetcher

class WebContentFetcherSpec extends FlatSpec {
    "A WebContentFetcher" should "be able to fetch items from a given URL" in {
        val items = WebContentFetcher.fetchContentFrom("https://matthhan.github.io/rssidiot/test.rss")
        assert(items.length != 0)
    }
}
