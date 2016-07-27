package rssidiot
import net.liftweb.json.parse
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.JsonAST
import net.liftweb.json.Printer
object JsonLibraryAdapter {
    def parse(json:String) = net.liftweb.json.parse(json)
    implicit class AdaptedJsonClass(v:JValue) {
        def json:String = Printer.compact(JsonAST.render(v))
    }
}
