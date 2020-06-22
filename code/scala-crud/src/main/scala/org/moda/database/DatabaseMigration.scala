package org.moda.database

import cats.effect.{Effect, IO}
//import simple.core.dao.{DspAppPlacementSql, DspAppSql, DspSql, PubAppPlacementSql, PubAppSql, PubSql}
import cats.implicits._
import doobie.implicits._
//import simple.core.dao


object DatabaseMigration {

  def apply[F[_]: Effect: DatabaseComponent](): DatabaseMigration[F] = new DatabaseMigration[F]

}

class DatabaseMigration[F[_]: Effect](implicit dc: DatabaseComponent[F]) {

//  def dropAllTables: F[List[Int]] =
//    List(
//      DspSql.dropTable
//      , DspAppSql.dropTable
//      , DspAppPlacementSql.dropTable
//      , PubSql.dropTable
//      , PubAppSql.dropTable
//      , PubAppPlacementSql.dropTable
//    ).map(_.run.transact(dc.ady)).sequence
//
//  def createAllTables: F[List[Int]] =
//    List(
//      DspSql.createTable()
//      , DspAppSql.createTable()
//      , DspAppPlacementSql.createTable()
//      , PubSql.createTable()
//      , PubAppSql.createTable()
//      , PubAppPlacementSql.createTable()
//    ).map(_.run.transact(dc.ady)).sequence
//
//  def initDB: F[Unit] = {
//    for {
//      //      _ <- this.dropAllTables
//      _ <- this.createAllTables
//    } yield ()
//  }
}

object DatabaseMigrationIO {

  def apply()(implicit dc: DatabaseComponent[IO]): DatabaseMigration[IO] = DatabaseMigration[IO]()

}
