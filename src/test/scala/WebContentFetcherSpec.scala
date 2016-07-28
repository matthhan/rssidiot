import org.scalatest.FlatSpec
import rssidiot.WebContentFetcher

class WebContentFetcherSpec extends FlatSpec {
    "A WebContentFetcher" should "be able to fetch items from a given URL" in {
        val fetchedString = WebContentFetcher.fetchContentFrom("https://matthhan.github.io/rssidiot/test.rss")
        assert(fetchedString.length != 0)
    }
    it should "return null when handed invalid xml" in {
        assertResult(null) {WebContentFetcher.fetchContentFrom("file:src/test/resources/invalid.xml")}
    }
    it should "return null when handed an unreachable url" in {
        assertResult(null) {WebContentFetcher.fetchContentFrom("www.mis.bistutsurllurbflurgnurg")}
    }
}
