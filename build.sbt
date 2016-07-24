name := "rssidiot"

version := "0.1"

libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.0-RC4",
    "net.liftweb" %% "lift-json" % "2.6",
    "org.scalafx" %% "scalafx" % "8.0.92-R10"
)

mainClass in Compile := Some("rssidiot.Gui")

//TODO Necessary bacause otherwise jsx stylesheets not found
//But really, stylesheets should be bundled with the application
unmanagedJars in Compile += {
      val ps = new sys.SystemProperties
        val jh = ps("java.home")
          Attributed.blank(file(jh) / "lib/ext/jfxrt.jar")
}

scalacOptions += "-deprecation"
