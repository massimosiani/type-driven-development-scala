import sbt._

object Dependencies {
  object versions {
    val cats = "2.6.1"
    val catsEffect = "3.2.3"
    val scalatest = "3.2.9"
    val scalatestplus = "3.2.9.0"
  }

  val catsDependencies = Seq(
    "org.typelevel" %% "cats-core" % versions.cats,
    "org.typelevel" %% "cats-effect" % versions.catsEffect,
  )
  case object org {
    case object scalatest {
      val scalatest =
        "org.scalatest" %% "scalatest" % "3.2.9"
    }

    case object scalatestplus {
      val `scalacheck-1-15` =
        "org.scalatestplus" %% "scalacheck-1-15" % "3.2.9.0"
    }
  }
}
