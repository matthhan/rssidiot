package rssidiot

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.SplitPane
import scalafx.scene.paint.Color._
import scalafx.scene.control._
import scalafx.scene.input.KeyEvent
import scalafx.event.EventType
import scalafx.scene.input.KeyCode




object Gui extends JFXApp {
    //TODO: Change this to a more reasonable save file
    val db = FeedDatabase.loadFrom("example.feeddb")
    db.fetchAllNewArticles
    val feedView = new ListView[Feed] { items() ++= db.listFeeds }
    val articleView = new ListView[Article] 
    feedView.selectionModel().selectedItem.onChange( (_,_,newlySelectedFeed) => {
        articleView.items().clear
        articleView.items() ++= newlySelectedFeed.unreadArticles
        articleView.selectionModel().selectFirst
    })
    feedView.selectionModel().selectFirst
    
    stage = new PrimaryStage {
        title = "ScalaFX Hello World"
        width = 1024
        height = 768
        scene = new Scene {
            root = new SplitPane {
                items.addAll(feedView,articleView)
                dividerPositions = 0
            }
        }
        filterEvent(KeyEvent.Any) { (event:KeyEvent) => 
            if(event.eventType == KeyEvent.KeyPressed) {
                event.code match {
                    //TODO: add functionality to open browser at article when
                    //space is pressed
                    //TODO: make sure the articleView and FeedView get into
                    //Focus when their button is pressed
                    //TODO make sure they scroll to ensure that the selected
                    //Article/Feed is in view
                    case KeyCode.Space => println("spaaace")
                    case KeyCode.D => feedView.selectionModel().selectPrevious
                    case KeyCode.F => feedView.selectionModel().selectNext
                    case KeyCode.J => articleView.selectionModel().selectNext
                    case KeyCode.K => articleView.selectionModel().selectPrevious
                    case _ =>
                }
            }
            event.consume
        }
    }
}
