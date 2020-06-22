package org.moda.api.base

import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

trait Api[F[_]] extends Http4sDsl[F] { this: EffectApi[F] =>

  // 不需要验证的API
  def publicR: HttpRoutes[F] = HttpRoutes.empty
  /*// 需要验证登录token的API
  def authedR: AuthedRoutes[AuthUser, F] = AuthedRoutes.empty
  // 需要验证是否注册DSP账号，并且账号是有效的API
  def userAuthedR: AuthedRoutes[SimpleDspUser, F] = AuthedRoutes.empty*/

}

trait Api0[F[_]] extends Http4sDsl[F] {

  def routes: HttpRoutes[F]

}
