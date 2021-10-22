package vijexa.lehahomework
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.EitherValues
import io.circe.syntax._

class ReportBodySpec extends AnyFreeSpec with Matchers with EitherValues {
  "ReportBody encoder" - {
    "should leave two decimal places" in {
      assert(
        Domain
          .ReportBody(Domain.Ruble(BigDecimal("34534.4436357365783758")))
          .asJson
          .noSpaces == """{"average":"34534.44"}""",
      )
    }

    "should round to two decimal places" in {
      assert(
        Domain
          .ReportBody(Domain.Ruble(BigDecimal("34534.479999")))
          .asJson
          .noSpaces == """{"average":"34534.48"}""",
      )
    }

    "should append two decimal places" in {
      assert(
        Domain
          .ReportBody(Domain.Ruble(BigDecimal("34534")))
          .asJson
          .noSpaces == """{"average":"34534.00"}""",
      )
    }
  }
}
