//a statika party is here
sourceGenerators in Compile += task[Seq[File]] {
  // Patterns:
  // mvn: "[organisation]/[module]_[scalaVersion]/[revision]/[artifact]-[revision]-[classifier].[ext]"
  // ivy: "[organisation]/[module]_[scalaVersion]/[revision]/[type]s/[artifact]-[classifier].[ext]"
  val fatUrl = {
    val isMvn = publishMavenStyle.value
    val scalaV = "_"+scalaBinaryVersion.value
    val module = moduleName.value + scalaV
    val artifact =
      (if (isMvn) "" else "jars/") +
        module +
        (if (isMvn) "-"+version.value else "") +
        "-fat.jar"
    Seq( publishS3Resolver.value.url
      , organization.value
      , module
      , version.value
      , artifact
    ).mkString("/")
  }
  val text = """
               |package generated.metadata
               |
               |object testCompota extends ohnosequences.compota.aws.deployment.Metadata { //$name$
               |  val artifact: String = "$artifact$"
               |  val jarUrl: String = "$jarUrl$"
               |}
               |""".stripMargin.
    replace("$artifact$", name.value.toLowerCase).
    replace("$jarUrl$", fatUrl)
  val file = (sourceManaged in Compile).value / "metadata.scala"
  IO.write(file, text)
  Seq(file)
}