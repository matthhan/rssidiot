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
    def fromJson(json:String):ArticleBuffer = {
        val value = JsonLibraryAdapter.parse(json)
        val res = new ArticleBuffer(value.getAttribute[Int]("size"))
        val arr = value.getChildren("content")
        arr.children.map(a => Article.fromJson(a.json)).foreach(res += _)
        res
    }
}
