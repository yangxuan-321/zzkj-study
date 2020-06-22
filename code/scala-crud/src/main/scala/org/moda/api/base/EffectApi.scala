package org.moda.api.base

import cats.effect.Effect

trait EffectApi[F[_]] {

  implicit def F: Effect[F]

}
