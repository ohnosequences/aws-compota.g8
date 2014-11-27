import ohnosequences.sbt._
import sbtrelease._
import ReleaseStateTransformations._
import ReleasePlugin._
import ReleaseKeys._
import AssemblyKeys._
import ohnosequences.sbt.nice.AssemblySettings

publishMavenStyle := false

isPrivate := true

bucketSuffix := "frutero.org"

//bucketSuffix := "$bucketSuffix$"

AssemblySettings.fatArtifactSettings

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) => {
  case "avsl.conf" => MergeStrategy.first
  case "mime.types" => MergeStrategy.first
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case PathList("META-INF", _*) => MergeStrategy.first
  case x => old(x)
}
}
