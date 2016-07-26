package rssidiot

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.layout.VBox
import scalafx.scene.layout.HBox
import scalafx.scene.layout.Priority
import scalafx.scene.paint.Color._
import scalafx.scene.control._
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.input.KeyEvent
import scalafx.event.EventType
import scalafx.event.ActionEvent
import scalafx.scene.input.KeyCode
import scalafx.geometry.Pos

import scalafx.beans.value.ObservableValue
import scalafx.beans.property.ReadOnlyObjectProperty


import java.net.URI
import java.io.File


object Gui extends JFXApp {
    Utility.makeSureFolderExists(Utility.defaultDataFolder)
    var db:FeedDatabase = null
    if(new File(Utility.defaultFeedDatabaseFile).exists)
        db = FeedDatabase.loadFrom(Utility.defaultFeedDatabaseFile)
    else
        db = new FeedDatabase
    
    JFXApp.userAgentStylesheet = "theme/theme.css"
    db.fetchAllNewArticles

    val makeArticleCells = { _:ListView[Article] =>
        new ListCell[Article] {
            val articleChangeListener = {(_:Any,_:Any,article:Article) =>
                if(article != null) {
                    text = article.title
                    if(article.unread) style = "-fx-font-weight: bold"
                    else style = "-fx-font-weight: normal"
                } else {
                    text = ""
                }
            }
            item.onChange(articleChangeListener)
        }
    }

    val articleView = new ListView[Article] {
        cellFactory = makeArticleCells
        selectionModel().selectedItem.onChange{ (_,_,newArticle) => 
            if(newArticle != null) {
                newArticle.markAsRead
                //This line is here only to trigger a changeEvent
                items().prepend(null)
                items() = items().drop(1)
            }
        }
    }
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
        cellFactory = { _ =>
            new ListCell[Feed] {
                item.onChange{ (_,_,feed) =>
                    if(feed != null) text = feed.name else text = ""
                }
            }
        }
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
        val dialog = new Dialog[Feed]{
            initOwner(stage) 
            title = "Feed Creation"
            headerText = "To create a new Feed, specify its Title and URL."
            val titleField = new TextField {
                    promptText = "Title"
            }
            val urlField = new TextField {
                    promptText = "URL"
            }
            dialogPane().content = new VBox {
                children += titleField
                children += urlField
            }
            dialogPane().buttonTypes += new ButtonType("Confirm", ButtonData.OKDone)
            dialogPane().buttonTypes += ButtonType.Cancel
            resultConverter = { button =>
                try {
                    if(button.buttonData == ButtonData.OKDone)
                        new Feed(name = titleField.text(),url = urlField.text())
                    else null
                } catch {
                    case e:Exception => null
                }
            }
        }
        dialog.showAndWait()
        val newFeed = dialog.result()
        if(newFeed != null && newFeed.valid) {
            db.add(newFeed)
            newFeed.fetchNewArticles
            feedView.items() += newFeed
        } else {
            //TODO: raise alert panel here
        }

    }
    val handleKeyPress = {(event:KeyEvent) => 
            if(event.eventType == KeyEvent.KeyPressed) {
                var spacePressed = false
                event.code match {
                    case KeyCode.Space => spacePressed = true
                    //The third line in all of these cases should do nothing.
                    //But actually if the selected cell is the last selectable 
                    //cell and selectNext is called then the cell will stay
                    //selected, but not be highlighted anymore, which is a bug.
                    //Therefore, we always select the same line we just selected
                    //again, using the sm.select accessor
                    case KeyCode.D => {
                        val sm = feedView.selectionModel()
                        sm.selectPrevious
                        sm.select(sm.selectedIndex())
                    }
                    case KeyCode.F => {
                        val sm = feedView.selectionModel()
                        sm.selectNext
                        sm.select(sm.selectedIndex())
                    }
                    case KeyCode.J => {
                        val sm = articleView.selectionModel()
                        sm.selectNext
                        sm.select(sm.selectedIndex())
                    }
                    case KeyCode.K => {
                        val sm = articleView.selectionModel()
                        sm.selectPrevious
                        sm.select(sm.selectedIndex())
                    }
                    case _ =>
                }
                //TODO make this scroll lazily instead of eagerly
                val selectedArticle = articleView.selectionModel().selectedItem()
                val selectedFeed = feedView.selectionModel().selectedItem()
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
        onCloseRequest = {_:Any => db.saveTo(Utility.defaultFeedDatabaseFile)}
    }
}
