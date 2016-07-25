package rssidiot
import rssidiot.collection.CircularBuffer
class ArticleBuffer(size:Int) extends CircularBuffer[Article](size) {
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
                    this.asArray.map(ar => ar.jsonString).reduce((x,y) => x + "," + y) + 
                    "]" + 
                "}"
}
object ArticleBuffer {
    def fromJsonElement(value:net.liftweb.json.JsonAST.JValue):ArticleBuffer = {
        implicit val formats = net.liftweb.json.DefaultFormats
        val obj = value.asInstanceOf[net.liftweb.json.JsonAST.JObject]
        val res = new ArticleBuffer((obj \ "size").extract[Int])
        val arr = (obj \ "content")
        arr.children.map(a => Article.fromJsonElement(a)).foreach(res += _)
        res
    }
}
