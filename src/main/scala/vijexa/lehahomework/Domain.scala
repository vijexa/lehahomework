package vijexa.lehahomework

import io.circe.Encoder
import io.circe.generic.semiauto._

import java.text.DecimalFormat

object Domain {
  final case class ReportBody(average: Ruble)

  object ReportBody {
    implicit val reportBodyEncoder = deriveEncoder[ReportBody]
  }

  final case class Ruble(value: BigDecimal)
  object Ruble {
    def ofKopeyki(kopeyki: BigDecimal): Ruble = Ruble(kopeyki / 100)

    private val decFormat = new DecimalFormat("0.00")
    implicit val rubleEncoder: Encoder[Ruble] =
      Encoder.encodeString.contramap(x => decFormat.format(x.value))
  }
}
