package rssidiot.collection
import rssidiot.JsonSerializable

class SerializableCircularBuffer[T <: JsonSerializable](size:Int)(implicit mf:Manifest[T]) extends CircularBuffer[T](size) {
        def jsonString = 
            if(this.asArray.isEmpty) "[]"
            else "[" + this.asArray.map(_.jsonString).reduce((x,y) => x + "," + y) + "]"
}
