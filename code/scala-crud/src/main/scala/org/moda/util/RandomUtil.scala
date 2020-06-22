package org.moda.util

import scala.collection.immutable.IndexedSeq


sealed trait OptionFactor

object OptionFactor {

  case object FullFactor extends OptionFactor

  case object NoneFactor extends OptionFactor

  case object RandomFactor extends OptionFactor

}


object Random extends RandomUtil

object RandomUtil {
  def apply(): RandomUtil = new RandomUtil {}
}

object EnhancedRandom extends EnhancedRandom

trait EnhancedRandom extends RandomUtil {

  val chars: IndexedSeq[Char] = alphabet ++ """!@#$%^&*()_+|~`-={}[]:/.,<>?;"""

  override def rString(n: Int): String = rString(n, chars)

}

trait RandomUtil {

  import OptionFactor._

  val alphabet: IndexedSeq[Char] = List(
    ('A' to 'Z').toList
    , ('a' to 'z').toList
    , ('0' to '9').toList
  ).flatten.toIndexedSeq

  val random: scala.util.Random = new scala.util.Random(System.currentTimeMillis)

  def rString(n: Int): String = rString(n, alphabet)

  def rString(n: Int, alphabet: IndexedSeq[Char]): String = {
    val m = alphabet.length
    Seq.fill[Char](n)(alphabet(random.nextInt(m))).mkString
  }

  def randomOption[T](f: => T)(implicit factor: OptionFactor): Option[T] = factor match {
    case RandomFactor => if (random.nextBoolean()) Some(f) else None
    case FullFactor   => Some(f)
    case NoneFactor   => None
  }

  def rUUID(lowercase: Boolean = true): String = {
    val s = java.util.UUID.randomUUID().toString
    if (lowercase) s.toLowerCase else s.toUpperCase
  }

  /**
    * 产生正态分布的随机数, 指定数学期望和标准差
    * @param μ 数学期望
    * @param σ 标准察
    * @return
    */
  def gaussian(μ: Double, σ: Double): Double = μ + σ * random.nextGaussian

  /**
    * 产生正态分布的随机数, 指定数学期望和标准差
    * @param mean 期望值
    * @param min 标准差
    * @return
    */
  def gaussianUpper95(mean: Double, min: Double): Double = {
    //    require(min < mean, "min must lower than mean!")
    val μ = mean
    val σ = (mean - min) / 1.96
    gaussian(μ, σ)
  }

  def gaussianUpper(mean: Double, min: Double)(): Double = {
    //    require(min < mean, "min must lower than mean!")
    val μ = mean
    val σ = (mean - min) / 1.96
    gaussian(μ, σ)
  }
}

