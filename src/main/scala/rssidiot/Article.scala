package rssidiot
import rssidiot.Types._

class Article(val guid:Guid,val title:Title) {


    def printableString(length:Int = 75):String = 
        if(this.title != null) this.title.take(length) 
        else "(null)"


    def ==(that:Article):Boolean = 
        if(that != null) this.guid == that.guid
        else false


    private var _read = false
    def read = this._read
    def markAsRead() {
        this._read = true
    }
}
