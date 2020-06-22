package org.moda.api

import cats.effect._
import cats.implicits._
import doobie.util.transactor.Transactor
import io.circe.JsonObject
import io.circe.generic.auto._
import org.http4s.HttpRoutes
import org.http4s.multipart.Multipart
import org.moda.api.base.api.Pretty
import org.moda.model.User
import org.moda.service.UserService
import cats._
import cats.implicits._
import cats.data.Kleisli
import cats.effect._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.server.blaze._
import org.http4s.implicits._

import scala.concurrent.ExecutionContext.Implicits.global


//object UserApi {
//
//  final case class User(id: Int, name: String)
//
//  final case class SimpleData(token: String)
//
//  def apply[F[_]: Effect](xa: Transactor[F]): UserApi[F] = new UserApi[F](xa)
//
//}

class UserApi[F[_]](xa: Transactor[F])(implicit val F: Effect[F])
    extends SimpleJsonApi[F] {

  private val service: UserService[F] = UserService[F](xa)

  private[this] object NameQueryParamMatcher extends QueryParamDecoderMatcher[String]("username")

  protected def r1: HttpRoutes[F] = HttpRoutes.of[F] {
    // 查询
    case GET -> Root / "user" =>
      val result = for {
        user <- service.queryAll()
      } yield Pretty(user)
      result.flatMap(Ok(_))

    // 增加
    case req @ POST -> Root / "user" =>
      val users = for {
        user <- req.as[User]
        rs <- service.save(user)
      } yield Pretty(rs)
      users.flatMap(Ok(_))

    case req @ PUT -> Root / "user" / id =>
      val users = for {
        user <- req.as[User]
        rs <- service.updateById(id, user)
      } yield Pretty(rs)
      users.flatMap(Ok(_))

    case req @ DELETE -> Root / "user" / id =>
      val users = for {
        rs <- service.deleteById(id)
      } yield Pretty(rs)
      users.flatMap(Ok(_))

    case _ => Ok("http4s is miss!")
  }

  override val publicR: HttpRoutes[F] = r1
}

//trait SimpleGetParametersApi[F[_]] { this: SimpleJsonApi[F] =>
//
//  private[this] object NameQueryParamMatcher extends QueryParamDecoderMatcher[String]("username")
//
////  GET '/q1?name=simple'
//  protected def r1: HttpRoutes[F] = HttpRoutes.of[F] {
//    case GET -> Root / "user" :? NameQueryParamMatcher(username) =>
//
//      val simple = User(1, username)
//      Ok(Pretty(simple))
//  }
//
//}

//trait SimplePostJsonApi[F[_]] { this: SimpleJsonApi[F] =>
//
//  // POST '/q2 @1.json
//  protected def r2: HttpRoutes[F] = {
//    HttpRoutes.of[F] {
//      case req @ POST -> Root / "q2" =>
//        val u: F[Pretty[User]] = req.as[User].map(Pretty.apply[User])
//        Ok(u)
//    }
//  }
//
//}

//trait SimplePostJsonWithParametersApi[F[_]] { this: SimpleJsonApi[F] =>
//
//  private[this] object IdQueryParamMatcher extends QueryParamDecoderMatcher[Int]("id")
//
//  // POST '/q3?id=1234' @1.json
//  protected def r3: HttpRoutes[F] = {
//    HttpRoutes.of[F] {
//      case req @ POST -> Root / "q3" :? IdQueryParamMatcher(id) =>
//        val u: F[Pretty[User]] = req.as[User].map(x => Pretty(x.copy(id = id)))
//        Ok(u)
//    }
//  }
//
//}

//trait SimpleMultiPartApi[F[_]] { this: SimpleJsonApi[F] =>
//  import io.circe.syntax._
//  import fs2._
//  protected def r5: HttpRoutes[F] = {
//    HttpRoutes.of[F] {
//      case req @ POST -> Root / "multipart" =>
//        req.decode[Multipart[F]] { m =>
//          def f(name: String): Option[F[String]] =
//            m.parts.find(_.name.contains(name)).map(_.body.through(text.utf8Decode).compile.toList.map(_.reduce(_ + _)))
//          val r1 = for {
//            x <- f("name")
//            y <- f("fileData")
//          } yield {
//            for {
//              u <- x
//              v <- y
//            } yield
//              JsonObject(
//                  "parts"        -> m.parts.length.asJson
//                , "fields"       -> m.parts.flatMap(_.name).asJson
//                , "name"         -> u.asJson
//                , "file_content" -> v.asJson
//              )
//          }
//          Ok(r1.getOrElse(F.pure(JsonObject.empty)))
//        }
//    }
//  }
//
//}
