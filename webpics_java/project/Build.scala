import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "webpics_java"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "be.objectify" %% "deadbolt-2" % "1.1.3-SNAPSHOT",
      "com.feth" %% "play-authenticate" % "0.2.0-SNAPSHOT",
      "mysql" % "mysql-connector-java" % "5.1.18"

      // Add your project dependencies here,
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(      resolvers += Resolver.url("Objectify Play Repository", url("http://schaloner.github.com/releases/"))(Resolver.ivyStylePatterns),
      resolvers += Resolver.url("Objectify Play Repository", url("http://schaloner.github.com/snapshots/"))(Resolver.ivyStylePatterns),

      resolvers += Resolver.url("play-easymail (release)", url("http://joscha.github.com/play-easymail/repo/releases/"))(Resolver.ivyStylePatterns),
      resolvers += Resolver.url("play-easymail (snapshot)", url("http://joscha.github.com/play-easymail/repo/snapshots/"))(Resolver.ivyStylePatterns),

      resolvers += Resolver.url("play-authenticate (release)", url("http://joscha.github.com/play-authenticate/repo/releases/"))(Resolver.ivyStylePatterns),
      resolvers += Resolver.url("play-authenticate (snapshot)", url("http://joscha.github.com/play-authenticate/repo/snapshots/"))(Resolver.ivyStylePatterns)
      // Add your own project settings here      
    )

}
