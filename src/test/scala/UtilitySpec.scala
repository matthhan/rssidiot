import org.scalatest.FlatSpec
import rssidiot.Utility

class UtilitySpec extends FlatSpec {
    "The Utility" should "be able to determine which os we are currently running" in {
        val opSys = Utility.getOperatingSystem
    }
    it should "always give us a defined folder in which we can store files" in {
        val f = Utility.defaultDataFolder
        assert(f != "")
        assert(f != null)
    }
    it should "always give us a defined file in which to store the feeddb" in {
        val f = Utility.defaultFeedDatabaseFile
        assert(f != "")
        assert(f != null)
    }
}
