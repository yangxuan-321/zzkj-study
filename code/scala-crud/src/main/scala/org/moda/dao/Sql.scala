package org.moda.dao

import scalapb._
import scala.reflect.ClassTag

import org.moda.model.{SimpleEnum, SimpleEnumCompanion}
import doobie.util.update.Update0
import doobie.postgres.implicits._
import doobie.util.meta.Meta
import doobie.implicits.javasql.TimestampMeta

trait Sql extends MappingExtra

trait SimpleSql[T] extends Sql {
  def tt: String
  def tableCreation: Update0
  def tableDeletion: Update0
}

object MappingExtra extends MappingExtra

trait MappingExtra {

  implicit val JavaTimeInstantMeta: Meta[java.time.Instant] =
    TimestampMeta.imap(_.toInstant)(java.sql.Timestamp.from)

  implicit def scalaPbEnumMeta[A <: GeneratedEnum: GeneratedEnumCompanion]: Meta[A] =
    Meta[Short].imap(s => implicitly[GeneratedEnumCompanion[A]].fromValue(s.toInt))(_.value.toShort)

  implicit def simpleEnumMeta[A <: SimpleEnum: SimpleEnumCompanion]: Meta[A] =
    Meta[Int].imap(implicitly[SimpleEnumCompanion[A]].fromValue)(_.value)

//  implicit val googlePbTimestampMeta: Meta[Timestamp] =
//    Meta[Instant].imap(i => Timestamp(seconds = i.getEpochSecond, nanos = i.getNano))(t =>
//      Instant.ofEpochMilli(t.seconds * 1000 + t.nanos / 1000))

  implicit def scalaPbEnumVectorMeta[A <: GeneratedEnum: GeneratedEnumCompanion: ClassTag]: Meta[Vector[A]] =
    Meta[Array[Int]].imap(_.map(implicitly[GeneratedEnumCompanion[A]].fromValue).toVector)(_.map(_.value).toArray)

  implicit def scalaPbEnumListMeta[A <: GeneratedEnum: GeneratedEnumCompanion: ClassTag]: Meta[List[A]] =
    Meta[Array[Int]].imap(_.map(implicitly[GeneratedEnumCompanion[A]].fromValue).toList)(_.map(_.value).toArray)

//  implicit def simpleEnumVectorMeta[A <: SimpleEnum: SimpleEnumCompanion: ClassTag]: Meta[Vector[A]] =
//    Meta[Array[Int]].imap(_.map(implicitly[SimpleEnumCompanion[A]].fromValue).toVector)(_.map(_.value).toArray)
//
//  implicit def SimpleEnumListMeta[A <: SimpleEnum: SimpleEnumCompanion: ClassTag]: Meta[List[A]] =
//    Meta[Array[Int]].imap(_.map(implicitly[SimpleEnumCompanion[A]].fromValue).toList)(_.map(_.value).toArray)

}