package vijexa.lehahomework

import io.circe.Encoder
import io.circe.generic.semiauto._

import java.text.DecimalFormat

object Domain {
  final case class ReportBody(average: BigDecimal)

  object ReportBody {
    private val decFormat = new DecimalFormat("0.00")
    implicit val bigDecimalEncoder: Encoder[BigDecimal] =
      Encoder.encodeString.contramap(decFormat.format)

    implicit val reportBodyEncoder = deriveEncoder[ReportBody]
  }
}
