package rssidiot.collection

class CircularBuffer[T](val size:Int)(implicit mf:Manifest[T]) {
    private var nextPointer = 0
    private val content = new Array[T](size)
    private def incrementNextPointer {
        this.nextPointer += 1
        this.nextPointer %= this.size
    }
    def insert(newElement:T) {
        this.content(this.nextPointer) = newElement
        this.incrementNextPointer
    }
    def asArray():Array[T] = 
      if(this.nextPointer == 0)
          this.content.clone
      else 
          this.content.slice(this.nextPointer, this.content.length) ++
          this.content.slice(0,this.nextPointer)
    def +=(that:T) {
        this.insert(that)
    }
}
