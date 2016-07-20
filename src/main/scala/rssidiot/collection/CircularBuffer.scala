package rssidiot.collection

class CircularBuffer[T](val size:Int)(implicit mf:Manifest[T]) {
    private var nextPointer = 0
    private val content:Array[Option[T]] = new Array[Option[T]](size).map(_ => None)
    private def incrementNextPointer {
        this.nextPointer += 1
        this.nextPointer %= this.size
    }
    def insert(newElement:T) {
        this.content(this.nextPointer) = Some(newElement)
        this.incrementNextPointer
    }
    def +=(that:T) { this.insert(that) }
    def insert(newElements:List[T]) { newElements.foreach(this.insert) }
    def ++=(that:List[T]) { this.insert(that) }

    def asArray():Array[T] = 
      if(this.nextPointer == 0)
          this.content.clone.flatMap(x=>x)      
      else 
          (this.content.slice(this.nextPointer, this.content.length) ++
              this.content.slice(0,this.nextPointer)).flatMap(x=>x)
}
