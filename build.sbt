name := "rssidiot"

version := "0.1"

libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.0-RC4",
    "net.liftweb" %% "lift-json" % "2.6",
    "org.scalafx" %% "scalafx" % "8.0.92-R10"
)

mainClass in Compile := Some("rssidiot.Gui")

scalacOptions += "-deprecation"

//Use the finished package instead of the class files for "sbt run",
//"sbt test" etc. This is useful because we want to put e.g. css and
//images into the jar as well and tests would not see them otherwise
exportJars:=true
