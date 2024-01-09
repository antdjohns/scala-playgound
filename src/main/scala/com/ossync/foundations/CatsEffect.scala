package com.ossync.foundations


import cats.effect.IO
object CatsEffect {

  /*
    describing computations as values
  */

  // IO - ds describing arbitrary computations.
  val firstIO: IO[Int] = IO.pure(42)

  def main(args: Array[String]): Unit = {

  }

}
