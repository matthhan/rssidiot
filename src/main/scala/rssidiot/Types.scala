package rssidiot
import scala.language.implicitConversions

object Types {
    //Some String should never contain quotes So that we can
    //serialize to Json easily
    class QuotelessString(val s:String)  {
        override def equals(x:Any) = x match {
            case that:QuotelessString => this.s == that.s
            case that:String => this.s == that
            case null => false
            case _ => false
        }
        override def toString:String = this.s
    }
    implicit def stringToQuotelessString(s:String):QuotelessString = new QuotelessString(s.filter(_!='"'))
    implicit def quotelessStringToString(t:QuotelessString):String = t.s

}
