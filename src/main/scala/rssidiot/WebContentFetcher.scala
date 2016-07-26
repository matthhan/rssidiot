package rssidiot
import rssidiot.Types._
import scala.io.Source
import scala.xml.XML
import scala.xml.Elem
object WebContentFetcher {
    private def downloadString(url:Url,numTries:Int = 10):String = {
            for(i <- 1 to 10) {
                try {
                    return this.makeAttemptToDowloadString(url)
                } catch {
                    //Usually the case when we get HTML status code 503:
                    //Just try again after a while
                    case e:java.io.IOException => {
                        Thread.sleep(500)
                        System.err.println(e.getMessage)
                    }
                }
            }
            //Give up
            null
    }
    private def makeAttemptToDowloadString(url:Url):String = {
        System.err.println("Downloading from: " + url)
        try {
            return Source.fromURL(url).mkString
        } catch {
            //Just somehow unknown Url. We can really only give up in this case
            case e: java.net.UnknownHostException =>
                System.err.println("Could not download from ${url}")
                null
        }

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
