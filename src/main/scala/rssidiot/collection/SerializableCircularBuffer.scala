package rssidiot.collection
import rssidiot.JsonSerializable

class SerializableCircularBuffer[T <: JsonSerializable](size:Int)(implicit mf:Manifest[T]) extends CircularBuffer[T](size) {
        def jsonString = 
            if(this.asArray.isEmpty) 
                "{" +
                    "\"size\":" + this.size + "," +
                    "\"content\":[]" + 
                "}"
            else 
                "{" + 
                    "\"size\":" + this.size + "," +
                    "\"content\":[" + 
                        this.asArray.map(_.jsonString).reduce((x,y) => x + "," + y) + 
                    "]" + 
                "}"
}
import rssidiot.Article
object SerializableCircularBuffer {
    def fromJsonElement(value:net.liftweb.json.JsonAST.JValue):SerializableCircularBuffer[Article] = {
        implicit val formats = net.liftweb.json.DefaultFormats
        val obj = value.asInstanceOf[net.liftweb.json.JsonAST.JObject]
        val res = new SerializableCircularBuffer[Article]((obj \ "size").extract[Int])
        val arr = (obj \ "content")
        arr.children.map(a => Article.fromJsonElement(a)).foreach(res += _)
        res
    }
}
