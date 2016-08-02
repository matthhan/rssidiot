package rssidiot

import java.io.PrintWriter
import java.awt.Desktop
import java.net.URI
import rssidiot.Types._

import java.io.File

object Utility {
    def writeStringToFile(s:String,filename:String) {
        new PrintWriter(filename) {write(s);close}
    }
    def readStringFromFile(filename:String):String = 
        scala.io.Source.fromFile(filename).mkString
    object OperatingSystem extends Enumeration {
        type OperatingSystem = Value
        val Windows, MacOs, Linux = Value
    }
    import rssidiot.Utility.OperatingSystem._
    def getOperatingSystem():OperatingSystem = {
        val osName = System.getProperty("os.name")
        if(osName.startsWith("Windows")) return OperatingSystem.Windows
        else if(osName.startsWith("Linux")) return OperatingSystem.Linux
        else if(osName.startsWith("Mac") || osName.startsWith("Darwin")) return OperatingSystem.MacOs
        else return OperatingSystem.Linux
    }
    def defaultDataFolder:String = Utility.getOperatingSystem() match {
        //TODO: Determine reasonable default folder for Windows
        case OperatingSystem.Linux => 
            System.getProperty("user.home") + "/.rssidiot/"
        case OperatingSystem.MacOs => 
            System.getProperty("user.home") +"/Library/Application Support/rssidiot/"
        case OperatingSystem.Windows => ""
    }
    def makeSureFolderExists(path:String) {
        val file = new File(path)        
        if(!file.exists) {
           file.mkdir 
        }
    }
    def defaultFeedDatabaseFile = Utility.defaultDataFolder + "feeddb.json"

}
