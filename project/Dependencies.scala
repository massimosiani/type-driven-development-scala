import sbt._

object Dependencies {
  object versions {
    val scalatest = "3.2.9"
    val scalatestplus = "3.2.9.0"
  }

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
