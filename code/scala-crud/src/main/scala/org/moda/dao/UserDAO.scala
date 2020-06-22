package org.moda.dao

import cats.effect.Async
import doobie.implicits._
import org.moda.model._
//import simple.dal.monoid.Implicits._
import cats.implicits._
import doobie.util.transactor.Transactor

object UserDAO {

  def apply[F[_]: Async](xa: Transactor[F]): UserDAO[F] = new UserDAO[F](xa)

}

class UserDAO[F[_]: Async](xa: Transactor[F]) {
  def deleteById(userId: String): F[Int] = UserSql.deleteById(userId).run.transact(xa)
//    sql"""delete from users where userid = """

  def saveBatch(rows: Seq[User]): F[Int] = UserSql.saveBatch(rows).transact(xa)

//  def insert3(rows: Seq[User]): F[Int] = {
//    val (ms, hs, ds) = rows.aggregated3
//    List(insertByMinute(ms), insertByHour(hs.toList), insertByDay(ds.toList)).sequence
//      .map(_.sum)
//  }

//  UserSql.listUserSql().to[Vector].transact(xa)
  def queryAll(): F[List[User]] = sql"""select userid, username, userpassword from users order by userid""".query[User].to[List].transact(xa)

  def updateById(userId: String, user: User) = UserSql.updateById(userId, user).run.transact(xa)
}
