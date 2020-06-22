package org.moda.database

import cats.effect.{Async, Blocker, ContextShift, Resource}
import com.zaxxer.hikari.HikariConfig
import org.moda.config.AppConfig
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts

object DatabaseComponent {

  def transactorFromConfig[F[_]: Async: ContextShift](config: AppConfig.DbConfig): Resource[F, HikariTransactor[F]] = {
    val c = config
    val hc: HikariConfig = {
      val config = new HikariConfig

      config.setJdbcUrl(c.url)
      config.setUsername(c.properties.user)
      config.setPassword(c.properties.password)
      config.setMaximumPoolSize(c.maximumPoolSize)
      config.setConnectionTimeout(c.connectionTimeout)
      config
    }
    for {
      c1 <- ExecutionContexts.fixedThreadPool[F](size = c.numThreads)
      c2 <- ExecutionContexts.cachedThreadPool[F]
      xa <- HikariTransactor.fromHikariConfig(hikariConfig = hc, connectEC = c1, blocker = Blocker.liftExecutionContext(c2))
    } yield xa
  }

  def apply[F[_]: Async: ContextShift](config: AppConfig.DbConfigs): Resource[F, DatabaseComponent[F]] =
    for {
      main   <- transactorFromConfig(config.main)
    } yield DatabaseComponent(main)
}

final case class DatabaseComponent[F[_]: Async: ContextShift](
  scala_crud: HikariTransactor[F],
)
