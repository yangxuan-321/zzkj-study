package org.moda

import java.util.UUID

import cats.effect.{ExitCode, IO, IOApp}
import com.typesafe.scalalogging.Logger
import org.moda.api.base.HttpService
import org.moda.config.{AppConfig, AppConfigExtension}
import org.moda.database.DatabaseComponent

object Main extends IOApp {

  val logger: Logger = com.typesafe.scalalogging.Logger(getClass)

  val selfUuid: String = UUID.randomUUID().toString


  val config: AppConfig = AppConfigExtension().config

  def run(args: List[String]): IO[ExitCode] = {
    DatabaseComponent[IO](config.database) use { implicit dc =>
      for {
        r <- HttpService[IO].serve(config.http.main)
      } yield r
    }
  }
}
