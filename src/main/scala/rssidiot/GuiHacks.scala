package rssidiot
import scalafx.Includes._
import javafx.beans.property.ObjectProperty
import javafx.collections.ObservableList
object GuiHacks {
    //Rationale: an object property that is wrapped into a Gui Element
    //will notify that Gui element if its content changes. However,
    //this will only happen if the wrapped type implements the 
    //"Observable" Trait. 
    
    //Implementing this would needlessly pollute the
    //Model classes, since we only need this behavior once.

    //Therefore, we just hack a new method into this class that
    //"forces" a notification by making an insignificant update.
    implicit class ForceUpdateObjectProperty[T >: Null <: AnyRef]
        (op:ObjectProperty[ObservableList[T]]) {
        def forceUpdate {
            op().prepend(null)
            op() = op().drop(1)
        }
    }
}
    
