package rssidiot
import rssidiot.collection.CircularBuffer
import rssidiot.JsonLibraryAdapter._
class ArticleBuffer(size:Int) extends CircularBuffer[Article](size) {
        def json = 
            if(this.asArray.isEmpty) 
                "{" +
                    "\"size\":" + this.size + "," +
                    "\"content\":[]" + 
                "}"
            else 
                "{" + 
                    "\"size\":" + this.size + "," +
                    "\"content\":[" + 
                    this.asArray.map(ar => ar.json).reduce((x,y) => x + "," + y) + 
                    "]" + 
                "}"
}
object ArticleBuffer {
    def fromJsonElement(json:String):ArticleBuffer = {
        val value = JsonLibraryAdapter.parse(json)
        implicit val formats = net.liftweb.json.DefaultFormats
        val obj = value.asInstanceOf[net.liftweb.json.JsonAST.JObject]
        val res = new ArticleBuffer((obj \ "size").extract[Int])
        val arr = (obj \ "content")
        arr.children.map(a => Article.fromJson(a.json)).foreach(res += _)
        res
    }
}
