import org.scalatest.FlatSpec
import rssidiot.collection.CircularBuffer

class CircularBufferSpec extends FlatSpec {
    val buf = new CircularBuffer[Int](5)
    "A CircularBuffer" should "store the last n inserted elements" in {
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
}