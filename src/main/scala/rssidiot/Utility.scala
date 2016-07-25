package rssidiot

import java.io.PrintWriter
import java.awt.Desktop
import java.net.URI
import rssidiot.Types._
object Utility {
    def writeStringToFile(s:String,filename:String) {
        new PrintWriter(filename) {write(s);close}
    }
    def readStringFromFile(filename:String):String = 
        scala.io.Source.fromFile(filename).mkString
}
