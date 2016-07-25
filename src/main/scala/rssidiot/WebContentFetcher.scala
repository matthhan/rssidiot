package rssidiot
import rssidiot.Types._
import scala.io.Source
import scala.xml.XML
object WebContentFetcher {
    private def downloadString(url:Url):String = {
        var res = ""
        try {
            res = Source.fromURL(url).mkString
        } catch {
            case e: java.net.UnknownHostException =>
                System.err.println("Could not download from ${url}")
        }
        return res
    }
    private def parseToXml(s:String) = XML.loadString(s)
    def fetchContentFrom(url:Url) = parseToXml(downloadString(url))
}
