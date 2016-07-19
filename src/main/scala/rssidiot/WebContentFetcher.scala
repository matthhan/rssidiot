package rssidiot
import rssidiot.Types._
import scala.io.Source
import scala.xml.XML
object WebContentFetcher {
    private def downloadString(url:Url):String = Source.fromURL(url).mkString
    private def parseToXml(s:String) = XML.loadString(s)
    private def extractItems(website:scala.xml.Node) = website \\ "item"
    def fetchContentFrom(url:Url) = extractItems(parseToXml(downloadString(url)))
}
