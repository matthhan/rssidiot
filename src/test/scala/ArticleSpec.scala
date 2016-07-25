import org.scalatest.FlatSpec
import rssidiot.Article

class ArticleSpec extends FlatSpec {
    val nullArticle = new Article(title=null,url=null)
    "Two Articles" should "be equal iff their urls are defined and equal" in {
        val a = new Article(title="Hello",url="blub")
        val b = new Article(title="World",url="blub")
        assert (a == b)
        assert (!(a == nullArticle))
        assert (!(nullArticle == b))
        assert (!( a == null))
        assert (!( null == a))
    }
    it should "be marked as unread initially" in {
        var a = new Article(title="bla",url="blub")
        assert(!a.read)
    }
    it should "be able to be marked as read" in {
        var a = new Article(title="bla",url="blub")
        a.markAsRead
        assert(a.read)
    }
    it should "be parseable from an item xml node (acc. rss standard)" in {
        val a = Article.fromXmlItem(<item><title>atitle</title><link>alink</link></item>)
        assert (a != null)
        assert (a.title == "atitle")
        assert (a.url == "alink")
    }
    it should "Be able to serialize itself to valid Json" in {
        val a = new Article(title="Hello",url="blub")
        //Make sure json is valid
        net.liftweb.json.parse(a.jsonString)

    }
}
