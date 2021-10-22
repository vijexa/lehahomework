package vijexa.lehahomework

import cats.effect.IO
import cats.effect.IOApp
import org.typelevel.log4cats.slf4j.Slf4jLogger
import pureconfig.ConfigSource

object Main extends IOApp.Simple {
  private val logger = Slf4jLogger.getLogger[IO]

  def run: IO[Unit] = {
    val credentials = ConfigSource.default.loadOrThrow[Credentials]
    val res = OrdersClient.resource[IO](credentials)

    res.use(client =>
      for {
        orders <- client.getOrders

        _ <- logger.info(s"fetched ${orders.length} orders")

        average = Order.averageOfRussianSelfPickup(orders)

        _ <- logger.info(s"average is $average")

        status <- client.postAverage(average)

        _ <- logger.info(s"got $status")
      } yield (),
    )
  }
}
