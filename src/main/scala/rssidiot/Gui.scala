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




object Gui extends JFXApp {
    JFXApp.userAgentStylesheet = "theme/theme.css"
    //TODO: Change this to a more reasonable save file
    val db = FeedDatabase.loadFrom("example.feeddb")
    db.fetchAllNewArticles

    val feedView = new ListView[Feed] { 
        items() ++= db.listFeeds 
        vgrow = Priority.Always
    }
    val articleView = new ListView[Article] 
    feedView.selectionModel().selectedItem.onChange( (_,_,newlySelectedFeed) => {
        articleView.items().clear
        articleView.items() ++= newlySelectedFeed.unreadArticles
        articleView.selectionModel().selectFirst
    })
    feedView.selectionModel().selectFirst
    
    val handleMinusButton = { _:ActionEvent =>
        val selItem = feedView
                        .selectionModel()
                        .selectedItem()
        db.remove(selItem)  
        //TODO exceptions are being thrown when the list
        //becomes Empty. Change SelectionModel Somehow?
        val lis = feedView.items()
        lis.remove(lis.indexOf(selItem))
    }
    val handlePlusButton = { _:ActionEvent =>
        //TODO: add handling dialog to add a new Feed
    }
    val handleKeyPress = { (event:KeyEvent) => 
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


    /*The .asInstanceOf[scalafx.scene.Node] is necessary
    because the variables are otherwise not converted to 
    the correct type. This appears to be a problem with
    the scala Compiler.*/
    stage = new PrimaryStage {
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
