package com.ossync.foundations

object TypeLevelProgramming  extends App {
  // Design High level list as a typed level list
  type DInt = Int
  val a: DInt = 1
  // Peano Arithmatic
  trait Nat
  

  
  class Succ[N <: Nat] extends Nat
  
  class _0 extends Nat
  
  type _1  = Succ[_0]
  type _2 = Succ[_1]
  type _3 = Succ[_2]
  type _4 = Succ[_3]
  type _5 = Succ[_4]

  trait <[A <: Nat, B <: Nat]
  object < {
    given basic[N <: Nat]: <[_0, Succ[N]] with {}
    given inductive[A <: Nat, B <: Nat](using <[A, B]): <[Succ[A], Succ[B]] with {}
    def apply[A <: Nat, B <: Nat](using lt: <[A, B]): <[A, B] = lt
  }

  val zeroLessThanTwo = <[_0, _2]
//   val threeLessThanOne = <[_3, _1]

  case class Person(name: String, age: Int)
  given p: Person = Person("sam", 1)
  def printName(using person: Person): Unit = {
    println(s"${person.name} ${person.age}")
  }

  printName
}
