ThisBuild / tlBaseVersion := "0.0"

ThisBuild / startYear := Some(2024)
ThisBuild / licenses := Seq(License.MIT)
ThisBuild / developers := List(
    tlGitHubDev("ChristopherDavenport", "Christopher Davenport")
)
ThisBuild / tlSitePublishBranch := Some("main")
ThisBuild / githubWorkflowJavaVersions := Seq(JavaSpec.temurin("17"))
ThisBuild / crossScalaVersions := Seq("2.13.14", "3.3.3")

// Projects
lazy val shellfish = tlCrossRootProject
  .aggregate(core, examples)

lazy val core = crossProject(JVMPlatform, JSPlatform, NativePlatform)
  .in(file("core"))
  .settings(
    name := "shellfish",
    libraryDependencies ++= Seq(
      "org.typelevel"               %% "cats-core"                  % "2.10.0",
      "org.typelevel"               %% "alleycats-core"             % "2.10.0",
      "org.typelevel"               %% "cats-effect"                % "3.5.4",
      "co.fs2"                      %% "fs2-core"                   % "3.10.2",
      "co.fs2"                      %% "fs2-io"                     % "3.10.2",
      "org.typelevel"               %%% "munit-cats-effect"         % "2.0.0-RC1" % Test,
    )
  )

lazy val examples = project.in(file("examples"))
  .enablePlugins(NoPublishPlugin)
  .dependsOn(core.jvm)
  .settings(
    name := "shellfish-examples",
    mimaPreviousArtifacts := Set()
  )

lazy val site = project.in(file("site"))
  .enablePlugins(TypelevelSitePlugin)
  .dependsOn(core.jvm)

//  .settings{
//    import microsites._
//   Seq(
//     micrositeName := "shellfish",
//      micrositeDescription := "Shell Scripting for Cats-Effect",
//      micrositeAuthor := "Christopher Davenport",
//      micrositeGithubOwner := "davenverse",
//      micrositeGithubRepo := "shellfish",
//      micrositeBaseUrl := "/shellfish",
//      micrositeDocumentationUrl := "https://www.javadoc.io/doc/io.chrisdavenport/shellfish_2.13",
//      micrositeGitterChannelUrl := "ChristopherDavenport/libraries", // Feel Free to Set To Something Else
//      micrositeFooterText := None,
//      micrositeHighlightTheme := "atom-one-light",
//      micrositePalette := Map(
//        "brand-primary" -> "#3e5b95",
//        "brand-secondary" -> "#294066",
//        "brand-tertiary" -> "#2d5799",
//        "gray-dark" -> "#49494B",
//        "gray" -> "#7B7B7E",
//        "gray-light" -> "#E5E5E6",
//        "gray-lighter" -> "#F4F3F4",
//        "white-color" -> "#FFFFFF"
//      ),
//      micrositePushSiteWith := GitHub4s,
//      micrositeGithubToken := sys.env.get("GITHUB_TOKEN"),
//      micrositeExtraMdFiles := Map(
//          file("CODE_OF_CONDUCT.md")  -> ExtraMdFileConfig("code-of-conduct.md",   "page", Map("title" -> "code of conduct",   "section" -> "code of conduct",   "position" -> "100")),
//          file("LICENSE")             -> ExtraMdFileConfig("license.md",   "page", Map("title" -> "license",   "section" -> "license",   "position" -> "101"))
//      )
//    )
//  }