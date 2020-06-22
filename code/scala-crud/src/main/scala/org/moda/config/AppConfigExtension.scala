package org.moda.config

import akka.actor.{ActorSystem, ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider}
import com.typesafe.scalalogging.Logger
import org.moda.json.SimpleJsonSupport
import io.circe.Printer
import io.circe.generic.auto._
import io.circe.syntax._

object AppConfigExtension {
  def apply(): AppConfigExtension = new AppConfigExtension()
}

class AppConfigExtension() extends SimpleJsonSupport {

  private [this] val logger  = Logger(getClass)

  private [this] val printer = Printer.spaces2SortKeys

  val config: AppConfig = {
    val c = AppConfig.loadOrThrow()
    logger.info("app config:\n{}", printer.print(c.asJson))
    c
  }

}