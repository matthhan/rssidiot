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
        val jsonObject = JsonLibraryAdapter.parse(json)
        val res = new ArticleBuffer(jsonObject.getAttribute[Int]("size"))
        val arr = jsonObject.getChild("content")
        arr.children.map(_.json).map(Article.fromJson(_)).foreach(res += _)
        res
    }
}
