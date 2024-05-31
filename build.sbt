// import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}
ThisBuild / tlBaseVersion := "0.0"

ThisBuild / organization := "io.chrisdavenport"
ThisBuild / organizationName := "Typelevel"
ThisBuild / startYear := Some(2024)
ThisBuild / licenses := Seq(License.MIT)
ThisBuild / developers := List(
    tlGitHubDev("ChristopherDavenport", "Christopher Davenport")
)

//  Compile / doc / scalacOptions ++= Seq(
//    "-groups",
//    "-sourcepath", (LocalRootProject /baseDirectory).value.getAbsolutePath,
//    "-doc-source-url", "https://github.com/ChristopherDavenport/shellfish/blob/v" + version.value + "€{FILE_PATH}.scala"
//  )

ThisBuild / tlSitePublishBranch := Some("main")
ThisBuild / githubWorkflowJavaVersions := Seq(JavaSpec.temurin("17"))

val Scala213 = "2.13.14"
val Scala3 = "3.3.3"

ThisBuild / crossScalaVersions := Seq(Scala213, Scala3)
ThisBuild / scalaVersion := Scala213

val catsV = "2.10.0"
val catsEffectV = "3.5.4"
val fs2V = "3.10.2"

val munitCatsEffectV = "2.0.0-RC1"

val kindProjectorV = "0.13.3"
val betterMonadicForV = "0.3.1"


// Projects
lazy val shellfish = tlCrossRootProject
  .aggregate(core, examples)

lazy val core = project.in(file("core"))
  .settings(commonSettings)
  .settings(
    name := "shellfish"
  )

lazy val examples = project.in(file("examples"))
  .enablePlugins(NoPublishPlugin)
  .settings(commonSettings)
  .dependsOn(core)
  .settings(
    name := "shellfish-examples",
    mimaPreviousArtifacts := Set()
  )

lazy val site = project.in(file("site"))
  .enablePlugins(TypelevelSitePlugin)
  .settings(commonSettings)
  .dependsOn(core)
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

// General Settings
lazy val commonSettings = Seq(
//  libraryDependencies ++= {
//
//    if (isDotty(scalaVersion.value)) Seq.empty
//    else Seq(
//      compilerPlugin("org.typelevel" % "kind-projector" % kindProjectorV cross CrossVersion.full),
//      compilerPlugin("com.olegpy" %% "better-monadic-for" % betterMonadicForV),
//    )
//  },
//  scalacOptions ++= {
//    if (isDotty(scalaVersion.value)) Seq("-source:3.0-migration")
//    else Seq()
//  },
//  Compile / doc / sources := {
//    val old = (Compile / doc / sources).value
//    if (isDotty(scalaVersion.value))
//      Seq()
//    else
//      old
//  },
  
  libraryDependencies ++= Seq(
    // Maybe Eventually
    // "com.github.jnr"              % "jnr-posix"                  % "3.1.5",
    "org.typelevel"               %% "cats-core"                  % catsV,
    "org.typelevel"               %% "alleycats-core"             % catsV,

    "org.typelevel"               %% "cats-effect"                % catsEffectV,

    "co.fs2"                      %% "fs2-core"                   % fs2V,
    "co.fs2"                      %% "fs2-io"                     % fs2V,


    "org.typelevel"               %%% "munit-cats-effect"        % munitCatsEffectV         % Test,
  )
)