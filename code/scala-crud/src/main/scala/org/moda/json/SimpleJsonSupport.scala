package org.moda.json

import java.time.{Instant, ZoneOffset}

import scala.concurrent.duration.FiniteDuration

import com.google.protobuf.timestamp
import io.circe.{Encoder, Json}

object SimpleJsonSupport extends SimpleJsonSupport

trait SimpleJsonSupport extends SimpleEnumJsonSupport with ScalaPbEnumJsonSupport {

  implicit val finiteDurationEncoder: Encoder[FiniteDuration] = (x: FiniteDuration) => Json.fromString(x.toString())
  implicit val instantEncoder: Encoder[Instant]               = (x: Instant) => Json.fromString(x.atOffset(ZoneOffset.ofHours(8)).toString)

  implicit val googleTimestampEncoder: Encoder[timestamp.Timestamp] = (x: timestamp.Timestamp) =>
    Json.fromString(Instant.ofEpochMilli(x.seconds * 1000 + x.nanos / 1000).atOffset(ZoneOffset.ofHours(8)).toString)

  // implicit def colEncoder[A, B]: Encoder[Col[A, B]] = (x: Col[A, B]) => Json.fromJsonObject(JsonObject("name" -> Json.fromString(x.name), "cl" -> Json.fromString(x.cl)))

}
