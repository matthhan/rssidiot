name := "rssidiot"

version := "0.1"


libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.0-RC4",
    "net.liftweb" %% "lift-json" % "2.6",
    "org.scalafx" %% "scalafx" % "8.0.92-R10"
)

mainClass in Compile := Some("rssidiot.Gui")

scalacOptions ++= Seq("-deprecation","-feature")

enablePlugins(JavaAppPackaging)

//This is necessary to build debian packages
maintainer := "Matthias Hansen <matthias.hansen@rwth-aachen.de>"

//Good for debugging the scalafx Gui. Without this, we have
//To restart sbt each time we run the gui
fork in run := true 
