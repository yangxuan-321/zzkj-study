package org.moda.dao

import cats.implicits._
import com.typesafe.scalalogging.Logger
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie.util.fragment.Fragment
import doobie.util.fragments.{in, orOpt, whereAndOpt}
import org.moda.model.User
//import org.moda.monoid.Implicits._

object UserSql extends UserSql

trait UserSql extends Sql {

  private[this] val logger = Logger(getClass)

  def saveBatch(rows: Seq[User]): ConnectionIO[Int] = insertGroupedUser(rows)

  private[this] def insertGroupedUser(rows: Seq[User]) =
    insertGrouped("users")(rows)
      .attemptSomeSqlState {
        case x => logger.error("{}", x)
      }
      .map(_ => 0)

  private[this] def insertGrouped(tableName: String)(rows: Seq[User]): doobie.ConnectionIO[Int] = {
    val q = insertSql(tableName)(rows)
    logger.debug(q.sql)
    q.run
  }

  private[this] def insertSql(tableName: String)(rows: Seq[User]) = {
    val f1 = Fragment.const(
      s"""insert into "$tableName" as t (userid, username, userpassword) VALUES """
    )

    def f(x: User) =
      fr"""(
      ${x.userid},
      ${x.username},
      ${x.userpassword}
    )""".stripMargin

    val f2 = rows.map(f).toVector.intercalate(fr", ")

    (f1 ++ f2).update
  }

  def deleteById(userId: String): doobie.Update0 = {
    val f1 = Option(userId).map(c => fr"userid = $c")
    (fr"delete from users " ++ whereAndOpt(f1)).update
  }

  def updateById(userId: String, user: User): doobie.Update0 = {
    val f1 = Option(userId).map(u => fr"userid = $u")
    val l1 = if (!user.userid.equals("")) sql"userid = ${user.userid}, " else sql""
    val l2 = if (!user.username.equals("")) sql"username = ${user.username}, " else sql""
    val l3 = if (!user.userpassword.equals("")) sql"userpassword = ${user.userpassword} " else sql""

    (sql"""update users
          set
        """ ++ l1 ++ l2 ++ l3 ++ sql""" """
        .stripMargin ++ whereAndOpt(f1)).update
  }
}