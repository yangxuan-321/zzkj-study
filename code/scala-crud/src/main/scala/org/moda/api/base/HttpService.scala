package org.moda.api.base

import cats.effect._
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.moda.api.ApiService
import org.moda.config.AppConfig
import org.moda.database.DatabaseComponent

abstract class HttpService[F[_]: ConcurrentEffect: Timer: DatabaseComponent](){

  val F: ConcurrentEffect[F] = implicitly[ConcurrentEffect[F]]

  val routes: F[HttpRoutes[F]]

  def serve(config: AppConfig.HttpConfig): F[ExitCode] =
    for {
      r <- routes
      b <- F.delay(scala.io.Source.fromResource("issue.txt"))
      _ <- BlazeServerBuilder[F]
        .withBanner(b.mkString :: Nil)
        //            .withConnectorPoolSize(size = 512)
        //            .withSocketKeepAlive(true)
        //            .withTcpNoDelay(true)
        //            .withNio2(true)
        .bindHttp(config.port, config.host)
        .withHttpApp(Router("/" -> r).orNotFound)
        .serve
        .compile
        .drain
    } yield ExitCode.Success

}

object HttpService {

  def apply[F[_]: ConcurrentEffect: Timer: DatabaseComponent](): HttpService[F] =
    new HttpService[F] {
      val routes: F[HttpRoutes[F]] = ApiService[F].routes
    }
}