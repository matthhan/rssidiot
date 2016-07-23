name := "rssidiot"

version := "0.1"

scalaVersion := "2.10.5"

libraryDependencies ++= Seq(
    "org.scalatest" % "scalatest_2.10" % "3.0.0-SNAP13",
    "net.liftweb" % "lift-json_2.10" % "2.6",
    "org.scalafx" % "scalafx_2.10" % "8.0.92-R10"
)

mainClass in Compile := Some("rssidiot.Gui")

//TODO Necessary bacause otherwise jsx stylesheets not found
//But really, stylesheets should be bundled with the application
unmanagedJars in Compile += {
      val ps = new sys.SystemProperties
        val jh = ps("java.home")
          Attributed.blank(file(jh) / "lib/ext/jfxrt.jar")
}
