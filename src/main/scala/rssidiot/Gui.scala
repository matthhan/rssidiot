package rssidiot

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.layout.HBox
import scalafx.scene.layout.VBox
import scalafx.scene.layout.BorderPane
import scalafx.scene.control.SplitPane
import scalafx.scene.paint.Color._
import scalafx.collections.ObservableBuffer
import scalafx.scene.control._



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
    }
}
