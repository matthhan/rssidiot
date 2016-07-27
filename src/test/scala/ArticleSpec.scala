import org.scalatest.FlatSpec
import rssidiot.Article

class ArticleSpec extends FlatSpec {
    "Two Articles" should "be equal iff their urls are defined and equal" in {
        val a = new Article("blub","Hello")
        val b = new Article("blub","World")
        println("equality " + (a.url == b.url))
        println(a.url.s)
        println(b.url.s)
        assert (a == b)
        assert (!( a == null))
        assert (!( null == a))
    }
    it should "be marked as unread initially" in {
        var a = new Article("blub","bla")
        assert(!a.read)
    }
    it should "be able to be marked as read" in {
        var a = new Article("blub","bla")
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
        val a = new Article("blub","Hello")
        //Make sure json is valid
        net.liftweb.json.parse(a.json)

    }
}
