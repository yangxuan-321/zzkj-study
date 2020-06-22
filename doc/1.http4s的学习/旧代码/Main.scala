package org.moda

import cats.effect._
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.blaze._

/**
 * 用户服务
 */
object Main extends IOApp {
//  implicit val cs: ContextShift[IO] = IO.contextShift(global)
//  implicit val timer: Timer[IO] = IO.timer(global)

//  val userService = HttpRoutes.of[IO] {
//    case GET -> Root / "user" / userId =>
//      Ok(s"Hello, $userId.")
//  }.orNotFound

//  def run(args: List[String]): IO[ExitCode] =
//    BlazeServerBuilder[IO].bindHttp(8080, "localhost")
//      .withHttpApp(userService)
//      .resource
//      .use(_ => IO.never)

}
