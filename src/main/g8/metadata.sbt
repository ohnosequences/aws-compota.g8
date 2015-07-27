sourceGenerators in Compile += task[Seq[File]] {
  // Patterns:
  // mvn: "[organisation]/[module]_[scalaVersion]/[revision]/[artifact]-[revision]-[classifier].[ext]"
  // ivy: "[organisation]/[module]_[scalaVersion]/[revision]/[type]s/[artifact]-[classifier].[ext]"
  val fatJarUrl = {
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
               |package ohnosequences.compota.generated
               |import ohnosequences.compota.aws.deployment.Metadata
               |
               |object metadata {
               |  val metadata: Metadata = Metadata(
               |    artifact = "$artifact$",
               |    jarUrl = "$jarUrl$",
               |    testJarUrl = None,
               |    mainClass = $mainClass$
               |  )
               |}
               |""".stripMargin
    .replace("$artifact$", artifactPrepare(name.value + version.value))
    .replace("$jarUrl$", fatJarUrl)
    .replace("$mainClass$", stringOptionPrinter(None))
  val file = (sourceManaged in Compile).value / "metadata.scala"
  IO.write(file, text)
  Seq(file)
}