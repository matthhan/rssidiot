package rssidiot
import net.liftweb.json.parse
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.JsonAST.JObject
import net.liftweb.json.JsonAST
import net.liftweb.json.Printer
object JsonLibraryAdapter {
    implicit val formats = net.liftweb.json.DefaultFormats
    def parse(json:String) = net.liftweb.json.parse(json)
    implicit class AdaptedJsonClass(v:JValue) {
        def json:String = Printer.compact(JsonAST.render(v))
        def children = v.children
        def getAttribute[T](s:String)(implicit mf:Manifest[T]):T = {
            val obj = v.asInstanceOf[JObject] 
            return (obj \ s).extract[T]
        }
        def getChild(s:String):JValue = {
            val obj = v.asInstanceOf[JObject] 
            return (obj \ s)
        }
        def getChildren(s:String):JValue = {
            val obj = v.asInstanceOf[JObject] 
            return (obj \ s)
        }
    }
}
