package vijexa.lehahomework

import pureconfig.generic.semiauto._

final case class Credentials(username: String, password: String)

object Credentials {
  implicit val credentialsReader = deriveReader[Credentials]
}
