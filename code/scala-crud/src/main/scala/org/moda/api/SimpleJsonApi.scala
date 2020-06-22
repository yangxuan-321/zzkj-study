package org.moda.api

import org.moda.json.SimpleJsonSupport
import org.http4s.circe.{CirceEntityDecoder, CirceEntityEncoder}
import org.moda.api.base.{Api, EffectApi}

trait SimpleJsonApi[F[_]] extends EffectApi[F] with Api[F] with CirceEntityEncoder with CirceEntityDecoder with SimpleJsonSupport
