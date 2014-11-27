Nice.scalaProject

//name := "$name$"

name := "testCompota"

description := ""

//organization := "$organization$"

organization := "ohnsequences"

libraryDependencies ++= Seq(
  "ohnosequences" % "compota_2.10" % "0.10.0-SNAPSHOT"
)

resolvers ++= Seq(
  "Era7 Releases"       at "http://releases.era7.com.s3.amazonaws.com",
  "Era7 Snapshots"      at "http://snapshots.era7.com.s3.amazonaws.com"
)

//resolvers +=  Resolver.url("era7" + " public ivy releases",  url("http://releases.era7.com.s3.amazonaws.com"))(Resolver.ivyStylePatterns)

//resolvers +=  Resolver.url("era7" + " public ivy snapshots",  url("http://snapshots.era7.com.s3.amazonaws.com"))(Resolver.ivyStylePatterns)

//resolvers += Resolver.sonatypeRepo("snapshots")

dependencyOverrides += "ohnosequences" % "aws-scala-tools_2.10" % "0.8.1-SNAPSHOT"

//dependencyOverrides += "commons-codec" % "commons-codec" % "1.6"

//dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-core" % "2.1.2"

//dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.1.2"

//dependencyOverrides += "jline" % "jline" % "2.6"

//dependencyOverrides += "org.slf4j" % "slf4j-api" % "1.7.5"

