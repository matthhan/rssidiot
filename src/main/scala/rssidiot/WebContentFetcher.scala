package rssidiot
import rssidiot.Types._
import scala.io.Source
import scala.xml.XML
import scala.xml.Elem
object WebContentFetcher {
    private def downloadString(url:Url):String = {
        var res = ""
        try {
            res = Source.fromURL(url).mkString
        } catch {
            //Just somehow unknown Url
            case e: java.net.UnknownHostException =>
                System.err.println("Could not download from ${url}")
            //Usually the case when we get HTML status code 503:
            //Just try again after a while
            //TODO: Limit the amount of tries so we do not get stuck in 
            //an infinite Loop
            case e:java.io.IOException => {
                Thread.sleep(100)
                return downloadString(url)
            }
        }
        return res
    }
    private def parseToXml(s:String):Elem = {
        try {
            XML.loadString(s)
        } catch{
            case e:org.xml.sax.SAXParseException => {
                System.err.println("Could not parse received xml String: " + s)
                null
            }
        }
    }
    def fetchContentFrom(url:Url):Elem = parseToXml(downloadString(url))
}
