package org.moda

import cats.{Applicative, Defer}
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object Api {

  def of[F[_]: Defer: Applicative]() = new Api[F].rootR

}

class Api[F[_]: Defer: Applicative] extends Http4sDsl[F] {
  val rootR: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root =>
      Ok("http4s service works.")
  }
}

