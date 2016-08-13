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

object Gui extends JFXApp {
  InitialConfiguration.initEnvironment
  var dbvar: FeedDatabase = _
  try {
    dbvar = InitialConfiguration.initFeedDatabase
  } catch {
    case e: Exception => {
      import scalafx.scene.control.Alert
      import scalafx.scene.control.Alert.AlertType
      (new Alert(AlertType.None) {
        contentText = "Failed to load database \n" + Utility.defaultFeedDatabaseFile + "is probably corrupted"
        buttonTypes = Seq(ButtonType.OK)
      }).showAndWait()
      System.exit(0)
    }
  }
  val db = dbvar
  db.sort

  val makeArticleCells = { _: ListView[Article] =>
    new ListCell[Article] {
      val articleChangeListener = { (_: Any, _: Any, article: Article) =>
        if (article != null) {
          text = article.title
          if (article.unread) style = "-fx-font-weight: bold"
          else style = "-fx-font-weight: normal"
        } else {
          text = ""
        }
      }
      item.onChange(articleChangeListener)
    }
  }
  val handleFeedSelectionChange: (Any, Any, Feed) => Unit = (_, _, newlySelectedFeed) => {
    if (newlySelectedFeed != null) {
      articleView.items().clear
      articleView.items() ++= newlySelectedFeed.unreadArticles
    }
  }
  val handleMinusButton = { _: ActionEvent =>
    val selItem = feedView
      .selectionModel()
      .selectedItem()
    db.remove(selItem)
    val lis = feedView.items()
    lis.remove(lis.indexOf(selItem))
  }
  val handlePlusButton = { _: ActionEvent =>
    val dialog = new Dialog[List[Feed]] {
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
        if (button.buttonData == ButtonData.OKDone)
          Feed.withAutodiscovery(name = titleField.text(), url = urlField.text())
        else null
      }
    }
    dialog.showAndWait()
    val newFeeds = dialog.result()
    if (!newFeeds.isEmpty) {
      db.add(newFeeds.head)
      newFeeds.head.fetchNewArticles
      feedView.items() += newFeeds.head
    } else {
      stage.alwaysOnTop = false
      import scalafx.scene.control.Alert
      import scalafx.scene.control.Alert.AlertType
      import scalafx.stage.Modality
      (new Alert(AlertType.None) {
        contentText = "Failed to create a new Feed.\n Make sure the chosen URL is valid."
        buttonTypes = Seq(ButtonType.OK)
        initModality(Modality.ApplicationModal)
      }).showAndWait()
      stage.alwaysOnTop = true
    }

  }
  val handleKeyPress = { (event: KeyEvent) =>
    if (event.eventType == KeyEvent.KeyPressed) {
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

      if (spacePressed) {
        hostServices.showDocument(selectedArticle.url)
        Thread.sleep(300)
        stage.requestFocus
      }
    }
    event.consume
  }

  val articleView = new ListView[Article] {
    cellFactory = makeArticleCells
    selectionModel().selectedItem.onChange { (_, _, newArticle) =>
      if (newArticle != null) {
        newArticle.markAsRead
        import rssidiot.GuiHacks._
        items.forceUpdate
      }
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
        item.onChange { (_, _, feed) =>
          if (feed != null) text = feed.name else text = ""
        }
      }
    }
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
    onCloseRequest = { _: Any => db.saveTo(Utility.defaultFeedDatabaseFile) }
  }
}
