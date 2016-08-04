name := "rssidiot"

version := "0.1"


libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.0-RC4",
    "net.liftweb" %% "lift-json" % "2.6",
    "org.scalafx" %% "scalafx" % "8.0.92-R10",
    "net.sourceforge.htmlcleaner" % "htmlcleaner" % "2.16"
)

mainClass in Compile := Some("rssidiot.Gui")

scalacOptions ++= Seq("-deprecation","-feature")

//For packaging as final apps
enablePlugins(JavaAppPackaging)

//specifically for packaging as windows or mac os apps
enablePlugins(JDKPackagerPlugin)

//Find os
val iconGlob = sys.props("os.name").toLowerCase match {
  case os if os.contains("mac") ⇒ ".icns"
  case os if os.contains("win") ⇒ ".ico"
  case _ ⇒ ".png"
}

jdkAppIcon := Some(file("src/main/resources/icon" + iconGlob))

//This is necessary to build debian packages
maintainer := "Matthias Hansen <matthias.hansen@rwth-aachen.de>"

//Good for debugging the scalafx Gui. Without this, we have
//To restart sbt each time we run the gui
fork in run := true 
