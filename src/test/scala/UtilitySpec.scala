import org.scalatest.FlatSpec
import rssidiot.Utility

class UtilitySpec extends FlatSpec {
    "The Utility" should "be able to determine which os we are currently running" in {
        val opSys = Utility.getOperatingSystem()
    }
}
