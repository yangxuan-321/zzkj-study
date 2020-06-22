package org.moda.config

import java.time._

import pureconfig.generic.ProductHint
import pureconfig.{CamelCase, ConfigFieldMapping, ConfigSource}
import pureconfig.ConfigReader.Result
import tsec.authentication.TSecJWTSettings
import pureconfig.generic.auto._

object AppConfig {

  final case class App(timezone: String) {
    lazy val offset: ZoneOffset = zoneId.getRules.getOffset(Instant.now)
    lazy val zoneId: ZoneId     = ZoneId.of(timezone)
  }

  final case class Http(main: HttpConfig)

  final case class HttpConfig(port: Int, host: String)

  final case class DbProperties(
                                 serverName: String
                                 , portNumber: Int
                                 , databaseName: String
                                 , user: String
                                 , password: String
                               )

  final case class DbConfig(
                             dataSourceClass: String
                             , properties: DbProperties
                             , maximumPoolSize: Int = 32
                             , connectionTimeout: Int = 500
                             , numThreads: Int = 8
                           ) {
    val url: String = if (dataSourceClass.contains("PGSimpleDataSource"))
      s"jdbc:postgresql://${properties.serverName}:${properties.portNumber}/${properties.databaseName}"
    else
      s"jdbc:mysql://${properties.serverName}:${properties.portNumber}/${properties.databaseName}"
  }

  final case class DbConfigs(main: DbConfig)

  object DbConfig {
    private[this] val mapping: ConfigFieldMapping = ConfigFieldMapping(CamelCase, CamelCase)
    implicit val hintA: ProductHint[DbProperties] = ProductHint[DbProperties](mapping)
    implicit val hintB: ProductHint[DbConfig]     = ProductHint[DbConfig](mapping)
    def loadOrThrow(config: String): DbConfig     = ConfigSource.default.at(config).loadOrThrow[DbConfig]
  }

  final case class AuthSettings(secret: String, tsec: TSecJWTSettings)
  final case class StatelessAuthConfig(jwt: AuthSettings)
  final case class AuthConfig(stateless: StatelessAuthConfig)

  import DbConfig.{hintA, hintB}

  def loadOrThrow(): AppConfig  = ConfigSource.default.loadOrThrow[AppConfig]
  def load(): Result[AppConfig] = ConfigSource.default.load[AppConfig]

}


import java.time.{Instant, ZoneId, ZoneOffset}

import AppConfig._
import pureconfig.ConfigReader.Result
import pureconfig.{CamelCase, ConfigFieldMapping}
import pureconfig.generic.ProductHint
import tsec.authentication.TSecJWTSettings

final case class AppConfig(
                            app: App
                            , http: Http
                            , database: DbConfigs
                            , auth: AuthConfig
                          )