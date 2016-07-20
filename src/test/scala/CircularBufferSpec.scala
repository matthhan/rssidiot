import org.scalatest.FlatSpec
import rssidiot.collection.CircularBuffer
import rssidiot.JsonSerializable
import rssidiot.collection.SerializableCircularBuffer

class CircularBufferSpec extends FlatSpec {
    "A CircularBuffer" should "store the last n inserted elements" in {
        val buf = new CircularBuffer[Int](5)
        buf.insert(1)
        buf.insert(2)
        buf.insert(3)
        buf.insert(4)
        buf.insert(5)
        buf += 6
        buf.insert(7)
        assertResult(Array(3,4,5,6,7)) {
            buf.asArray
        }
    }
    it should "Only ever store n elements" in {
        val buf = new CircularBuffer[Double](10)
        for(i <- 1 to 100) 
            buf += (i * Math.PI)
        
        assertResult(10) {
            buf.asArray.length
        }
    }
    it should "be able to insert multiple elements at once" in {
        val buf = new CircularBuffer[String](5)
        val lis = List("a","b","c","d","e","f")
        buf ++= lis
        assertResult(Array("b","c","d","e","f")) {
            buf.asArray
        }
    }
    it should "Create arrays containing only as many elements as were actually inserted" in {
        val buf = new CircularBuffer[Array[Int]](100)
        for(i <- 1 to 40) {
            buf += Array(i)
        }
        assertResult(40) {
            buf.asArray.length
        }
    }
    it should "be able to serialize itself into a valid json string" in {
        implicit class SerializableInt(x:Int) extends JsonSerializable {
            def jsonString = x.toString
        }
        val buf = new SerializableCircularBuffer[SerializableInt](5)
        buf.insert(1)
        buf.insert(2)
        buf.insert(3)
        buf.insert(4)
        buf.insert(5)
        buf += 6
        buf.insert(7)
        buf.jsonString
        //TODO: add json validation
    }
}
