package org.moda.json

import scala.util.{Success, Try}

import org.moda.model.{SimpleEnum, SimpleEnumCompanion}
import io.circe._

object SimpleEnumJsonSupport extends SimpleEnumJsonSupport

trait SimpleEnumJsonSupport {

  implicit def simpleEnumEncoder[A <: SimpleEnum]: Encoder[A] = (x: A) => Json.fromInt(x.value)
  // implicit def simpleEnumEncoder[A <: SimpleEnum]: Encoder[A] = (x: A) => Json.obj("value" -> Json.fromInt(x.value), "name" -> Json.fromString(x.name))

//  implicit def simpleDecoder[A <: SimpleEnum: SimpleEnumCompanion]: Decoder[A] = Decoder.decodeInt.emap { v: Int =>
//    Either.catchNonFatal(implicitly[SimpleEnumCompanion[A]].fromValue(v)).leftMap(_ => s"decode error! value: $v")
//  }

  implicit def simpleDecoder[A <: SimpleEnum: SimpleEnumCompanion]: Decoder[A] = { c: HCursor =>
    val cc: SimpleEnumCompanion[A] = implicitly[SimpleEnumCompanion[A]]
    val f: DecodingFailure         = DecodingFailure(s"decode error! value: ${c.value}", Nil)
    Try(c.value.as[Int]) match {
      case Success(Right(value)) => Right(cc.fromValue(value))
      case _ =>
        c.value.as[String].flatMap { x =>
          cc.fromName(x).toRight(f)
        }
    }
  }
}
