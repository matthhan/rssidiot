package rssidiot

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.SplitPane
import scalafx.scene.layout.VBox
import scalafx.scene.layout.HBox
import scalafx.scene.layout.Priority
import scalafx.scene.paint.Color._
import scalafx.scene.control._
import scalafx.scene.input.KeyEvent
import scalafx.event.EventType
import scalafx.event.ActionEvent
import scalafx.scene.input.KeyCode
import scalafx.geometry.Pos

import java.net.URI


object Gui extends JFXApp {
    JFXApp.userAgentStylesheet = "theme/theme.css"
    //TODO: Change this to a more reasonable save file
    val db = FeedDatabase.loadFrom("example.feeddb")
    db.fetchAllNewArticles

    val articleView = new ListView[Article] 
    val handleFeedSelectionChange = (_:Any,_:Any,newlySelectedFeed:Feed) => {
        if(newlySelectedFeed != null) {
            articleView.items().clear
            articleView.items() ++= newlySelectedFeed.unreadArticles
            articleView.selectionModel().selectFirst
        }
    }
    val feedView = new ListView[Feed] { 
        items() ++= db.listFeeds 
        vgrow = Priority.Always
        selectionModel().
            selectedItem.
            onChange(handleFeedSelectionChange)
        selectionModel().selectFirst
    }
    
    val handleMinusButton = { _:ActionEvent =>
        val selItem = feedView
                        .selectionModel()
                        .selectedItem()
        db.remove(selItem)  
        val lis = feedView.items()
        lis.remove(lis.indexOf(selItem))
    }
    val handlePlusButton = { _:ActionEvent =>
        //TODO: add handling dialog to add a new Feed
    }
    val handleKeyPress = {(event:KeyEvent) => 
            if(event.eventType == KeyEvent.KeyPressed) {
                var spacePressed = false
                event.code match {
                    case KeyCode.Space => spacePressed = true
                    case KeyCode.D => feedView.selectionModel().selectPrevious
                    case KeyCode.F => feedView.selectionModel().selectNext
                    case KeyCode.J => articleView.selectionModel().selectNext
                    case KeyCode.K => articleView.selectionModel().selectPrevious
                    case _ =>
                }
                //TODO make this scroll lazily instead of eagerly
                val selectedArticle = articleView .selectionModel() .selectedItem()
                val selectedFeed = feedView .selectionModel() .selectedItem()
                articleView.scrollTo(selectedArticle)
                feedView.scrollTo(selectedFeed)

                if(spacePressed) {
                    hostServices.showDocument(selectedArticle.url)
                    Thread.sleep(300)
                    stage.requestFocus
                }
            }
            event.consume
        }

    /*The .asInstanceOf[scalafx.scene.Node] is necessary
    because the variables are otherwise not converted to 
    the correct type. This appears to be a problem with
    the scala Compiler.*/
    stage = new PrimaryStage {
        alwaysOnTop = true
        title = "rssidiot"
        width = 1024
        height = 768
        filterEvent(KeyEvent.Any)(handleKeyPress)
        scene = new Scene {
            stylesheets = List("addedStyles.css")
            root = new SplitPane {
                items += (new VBox {
                    children += feedView
                    children += (new HBox {
                        children += (new Button {
                            text = "+"
                            onAction = handlePlusButton
                        }).asInstanceOf[scalafx.scene.Node]
                        children += (new Button {
                            text = "−"
                            onAction = handleMinusButton
                        }).asInstanceOf[scalafx.scene.Node]
                        alignment = Pos.BaselineRight
                    }).asInstanceOf[scalafx.scene.Node]
                    alignment = Pos.BottomRight
                }).asInstanceOf[scalafx.scene.Node]
                items += articleView
                dividerPositions = 0
            }
        }
    }
}
