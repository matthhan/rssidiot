package rssidiot
import rssidiot.Types._
import scala.io.Source
import scala.xml.XML
import scala.xml.Elem
object WebContentFetcher {
    private def downloadString(url:String,numTries:Int = 10):String = {
            for(i <- 1 to 10) {
                try {
                    return this.makeAttemptToDowloadString(url)
                } catch {
                    //Usually the case when we get HTML status code 503:
                    //Just try again after a while
                    case e:java.io.IOException => {
                        Thread.sleep(getWaitTime(url))
                    }
                }
            }
            //Give up
            null
    }
    private def getWaitTime(url:String) = url match {
        case "https://www.reddit.com/.rss" => 2000
        case _ => 100
    }
    private def makeAttemptToDowloadString(url:String):String = {
        try {
            return Source.fromURL(url).mkString
        } catch {
            //Just somehow unknown Url. We can really only give up in this case
            case e: java.net.UnknownHostException =>
                null
        }

    }
    private def parseToXml(s:String):Elem = {
        try {
            XML.loadString(s)
        } catch{
            case e:org.xml.sax.SAXParseException => null
            case e:java.lang.NullPointerException => null
        }
    }
    def fetchContentFrom(url:String):Elem = parseToXml(downloadString(url))
}
