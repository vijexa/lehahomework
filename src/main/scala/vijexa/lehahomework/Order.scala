package vijexa.lehahomework

import cats.effect.kernel.Async
import cats.implicits._
import kantan.csv._
import kantan.csv.ops._
import org.http4s.DecodeResult
import org.http4s.EntityDecoder
import org.http4s.InvalidMessageBodyFailure

import java.time.Instant
import scala.util.Try

final case class Order(
    id: Long,
    date: Instant,
    name: String,
    phone: String,
    delivery: String,
    orderTotal: BigDecimal,
)

object Order {

  private implicit val instantDecoder: CellDecoder[Instant] = cell =>
    Try(
      Instant.ofEpochMilli(cell.toLong),
    ).toEither.left.map(DecodeError.TypeError(_))

  private implicit val orderDecoder: HeaderDecoder[Order] =
    HeaderDecoder.decoder(
      "id",
      "date",
      "name",
      "phone",
      "delivery",
      "orderTotal",
    )(Order.apply _)

  def fromCSV(csv: String): Either[ReadError, List[Order]] =
    csv.asCsvReader[Order](rfc.withHeader).toList.sequence

  implicit def entityDecoder[F[_]: Async]: EntityDecoder[F, List[Order]] =
    EntityDecoder.text.flatMapR(
      fromCSV(_) match {
        case Left(err) =>
          DecodeResult.failureT(
            InvalidMessageBodyFailure(
              "CSV parser was not able to parse response body",
              err.some,
            ),
          )
        case Right(value) => DecodeResult.successT(value)
      },
    )

  def averageOfRussianSelfPickup(orders: List[Order]): BigDecimal = {
    val validOrders = orders.filter(x =>
      x.phone.startsWith("+7") && x.delivery == "self-pickup",
    )

    val sum = validOrders.map(_.orderTotal).sum

    if (validOrders.length > 0) sum / validOrders.length else 0
  }
}
