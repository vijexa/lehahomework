package vijexa.lehahomework

import cats.effect.kernel.Async
import cats.effect.kernel.Resource
import cats.implicits._
import io.circe.syntax._
import org.http4s.BasicCredentials
import org.http4s.Headers
import org.http4s.Method._
import org.http4s.Status
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.circe.CirceEntityCodec._
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.headers.Authorization
import org.http4s.implicits._
import org.typelevel.log4cats.slf4j.Slf4jLogger

case class OrdersClient[F[_]: Async](
    client: Client[F],
    credentials: Credentials,
) extends Http4sClientDsl[F] {
  private val logger = Slf4jLogger.getLogger[F]

  private val auth = Authorization(
    BasicCredentials(credentials.username, credentials.password),
  )

  def getOrders: F[List[Order]] = {
    val req = GET(
      uri"https://kiraind.ru/leadball-test/orders",
      Headers(auth),
    )

    client.expect[List[Order]](req)
  }

  def postAverage(average: BigDecimal): F[Status] = {
    val body = Domain.ReportBody(Domain.Ruble.ofKopeyki(average))
    val req = POST(
      body,
      uri"https://kiraind.ru/leadball-test/reports",
      Headers(auth),
    )

    for {
      _ <- logger.info(s"posting request with body ${body.asJson}")
      status <- client.run(req).use(resp => resp.status.pure[F])
    } yield status
  }

}

object OrdersClient {
  def resource[F[_]: Async](
      credentials: Credentials,
  ): Resource[F, OrdersClient[F]] =
    BlazeClientBuilder[F].resource.map(OrdersClient(_, credentials))
}
