package org.moda.service

import cats.effect.Effect
import com.typesafe.scalalogging.Logger
import doobie.util.transactor.Transactor
import org.moda.dao.UserDAO
import org.moda.model.User
import cats.effect.Effect
import cats.implicits._
import org.moda.util.RandomUtil

object UserService {

  def apply[F[_]: Effect](xa: Transactor[F]): UserService[F] =
    new UserService[F](xa)

}

class UserService[F[_]: Effect](xa: Transactor[F]) {

  private[this] val rand: RandomUtil = RandomUtil()

  private[this] val logger = Logger(getClass)

  val effectEv: Effect[F] = implicitly

  protected val dao: UserDAO[F] = UserDAO[F](xa)

  def saveBatch(users: Seq[User]): F[Int] = {
    val users1 = users map{e => new User(rand.rUUID(), e.username, e.userpassword)}
    dao.saveBatch(users1)
  }

  def save(user: User): F[Int] = {
    val users = Seq(user)
    saveBatch(users)
  }

  def queryAll(): F[List[User]] = {
    val users = for {
      user <- dao.queryAll()
    } yield {
      user.toList.map { e =>
        new User (
          userid = e.userid,
          username = e.username,
          userpassword = e.userpassword
        )
      }
    }
    // 返回
    users
  }

  def deleteById(userId: String): F[Int] ={
    dao.deleteById(userId)
  }

  def updateById(userId: String, user: User) = {
    dao.updateById(userId, user)
  }
}

