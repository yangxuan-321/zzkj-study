package org.moda.json

import cats.syntax.either._
import io.circe._
import scalapb._

trait ScalaPbEnumJsonSupport {

  implicit def pbEnumEncoder[A <: GeneratedEnum]: Encoder[A] = (x: A) => Json.fromInt(x.value)
  // implicit def pbEnumEncoder[A <: GeneratedEnum]: Encoder[A] = (x: A) => Json.obj("value" -> Json.fromInt(x.value), "name" -> Json.fromString(x.name))

  implicit def pbEnumDecoder[A <: GeneratedEnum: GeneratedEnumCompanion]: Decoder[A] = Decoder.decodeInt.emap { v: Int =>
    Either.catchNonFatal(implicitly[GeneratedEnumCompanion[A]].fromValue(v)).leftMap(_ => s"decode error! value: $v")
  }

}
