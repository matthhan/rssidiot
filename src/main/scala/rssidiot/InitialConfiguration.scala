package rssidiot
import scalafx.application.JFXApp
import java.io.File
object InitialConfiguration {
    def initEnvironment {
        System.setProperty("http.agent","rssidiot/0.1")
        Utility.makeSureFolderExists(Utility.defaultDataFolder)
        JFXApp.userAgentStylesheet = "theme/theme.css"
    }
    def initFeedDatabase:FeedDatabase =
        if(new File(Utility.defaultFeedDatabaseFile).exists) {
            val db = FeedDatabase.loadFrom(Utility.defaultFeedDatabaseFile)
            db.fetchAllNewArticles
            db
        }
        else
            new FeedDatabase
}
