package com.ossync.jobsboard.playground

import cats.effect.*
import cats.effect.implicits.*
import cats.effect.unsafe.implicits.global

object SimplePlayground {
  def main(args: Array[String]): Unit = {
    def fib(n: Int, a: Long = 0, b: Long = 1): IO[Long] = {
      IO(a + b ).flatMap { b2 => 
        if (n > 0) fib(n - 1, b, b2) 
        else IO.pure(b2)
      }
  }

  val a = fib(10)
  println(a.unsafeRunSync())
  println("success")
  }
}
