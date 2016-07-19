import org.scalatest.FlatSpec
import rssidiot.Article

class ArticleSpec extends FlatSpec {
    val nullArticle = new Article(title=null,guid=null)
    "Two Articles" should "be equal iff their guids are defined and equal" in {
        val a = new Article(title="Hello",guid="blub")
        val b = new Article(title="World",guid="blub")
        assert (a == b)
        assert (!(a == nullArticle))
        assert (!(nullArticle == b))
        assert (!( a == null))
        assert (!( null == a))
    }
    "An Article" should "be able to return a truncated version for display on screens" in {
        val a = new Article(title="Loremipsumdolorsitametsedconsectetursadipiscingelit",guid="bam")
        assertResult(4) {a.printableString(4).length}
        val len = a.title.length
        assertResult(len) { a.printableString(1000).length }
        assertResult(a.title) { a.printableString(len) }
        assertResult("(null)") { nullArticle.printableString(5) }
    }
}
