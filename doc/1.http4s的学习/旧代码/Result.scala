package org.moda

import cats.effect.IO
import org.http4s.{EntityDecoder, EntityEncoder}

case class ResultVO[T](val data: T, val code: String, val message: String) {
//  implicit def resultEncoder: EntityEncoder[IO, ResultVO] = ???
//  implicit def resultsEncoder: EntityEncoder[IO, ResultVO] = ???
}
