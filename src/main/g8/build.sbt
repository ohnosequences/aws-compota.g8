Nice.scalaProject

name := "$name$"

organization := "$organization$"

libraryDependencies ++= Seq(
  "ohnosequences" % "compota_2.10" % "0.10.0-RC2"
)

resolvers ++= Seq(
  "Era7 Releases"       at "http://releases.era7.com.s3.amazonaws.com",
  "Era7 Snapshots"      at "http://snapshots.era7.com.s3.amazonaws.com"
)


//dependencyOverrides += "ohnosequences" % "aws-scala-tools_2.10" % "0.13.2"


