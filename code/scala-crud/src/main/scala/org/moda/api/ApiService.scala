package org.moda.api

import cats.effect.{ConcurrentEffect, Timer}
import cats.implicits._
import io.prometheus.client.CollectorRegistry
import org.http4s.HttpRoutes
import org.http4s.metrics.prometheus.Prometheus
import org.http4s.server.middleware.{CORS, CORSConfig, Metrics, Timeout}
import org.moda.api.base.Api
import org.moda.database.DatabaseComponent

import scala.concurrent.duration._

object ApiService {
  def apply[F[_]: ConcurrentEffect: Timer: DatabaseComponent](): ApiService[F] = new ApiService[F]()
}

class ApiService[F[_]: ConcurrentEffect: Timer: DatabaseComponent](val cors: Boolean = false) {


  import ApiServiceMiddleware._

  //  def routes: F[HttpRoutes[F]] = apis.routes.corsed(cors).prometheusMeasured(null)
  def routes: F[HttpRoutes[F]] = apis.routes.corsed(cors).prometheusMeasured(null)

  val dc: DatabaseComponent[F] = implicitly[DatabaseComponent[F]]

  val apis: List[Api[F]] = List(
    new UserApi[F](dc.scala_crud)
  )

  val a = 10
}

object ApiServiceMiddleware {

  implicit class PublicRoutes[F[_]: ConcurrentEffect](apis: List[Api[F]]) {
    def routes: HttpRoutes[F] = apis.map(_.publicR).foldLeft(HttpRoutes.empty[F])(_ <+> _)
  }

  implicit class CorsRoutes[F[_]: ConcurrentEffect](routes: HttpRoutes[F]) {
    def corsed(cors: Boolean): HttpRoutes[F] =
      if (cors) {
        val methodConfig = CORSConfig(
          anyOrigin = true,
          anyMethod = false,
          allowedMethods = Some(Set("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD")),
          allowCredentials = true,
          maxAge = 1.day.toSeconds
        )
        CORS(routes, methodConfig)
      } else routes
  }

  implicit class PrometheusRoutes[F[_]: ConcurrentEffect: Timer](routes: HttpRoutes[F]) {
    def prometheusMeasured(registry: CollectorRegistry): F[HttpRoutes[F]] =
      // Prometheus.metricsOps[F](registry, "server").map(Metrics[F](_)(routes)).allocated.map(_._1)
      Prometheus
        .metricsOps[F](CollectorRegistry.defaultRegistry, "server")
        .map(Metrics[F](_)(routes))
        .use(
          implicitly[ConcurrentEffect[F]].pure
        )
  }

  implicit class TimeoutRoutes[F[_]: ConcurrentEffect: Timer](routes: HttpRoutes[F]) {
    def timeoutTo(timeout: FiniteDuration): HttpRoutes[F] = Timeout(timeout)(routes)
  }

}
