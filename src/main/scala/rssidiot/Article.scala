package rssidiot
import rssidiot.Types._

class Article(val guid:Guid,val title:Title) {
    def printableString(length:Int = 75):String = 
        if(this.title.length > length) 
          this.title.substring(0,length-1) 
        else 
          this.title
    def equals(that:Article):Boolean = this.guid == that.guid
}
