package com.ossync.foundations

object GivenInstances {
    given decendingOrdering: Ordering[Int]  = Ordering.fromLessThan(_ > _)

    val aList = List(1, 4, 3, 2, 5)
    val anOrderedList = aList.sorted

    case class Person(name: String, age: Int)

    val people = List(Person("Eric", 30), Person("Sarah", 34), Person("Eric", 29), Person("Jim", 12))

    given personOrdering: Ordering[Person] = new Ordering[Person] {
        override def compare(x: Person, y: Person): Int = {
            if x.name.compareTo(y.name) == 0 then x.age.compareTo(y.age)
            else  x.name.compareTo(y.name)
        }
    }

    val sortedPeople = people.sorted

    // using clauses
    trait Combinator[A] {
        def combine(x: A, y: A): A
    }

    def combineAll[A](list: List[A])(using combinator: Combinator[A]): A = {
        list.reduce(combinator.combine)
    }

    given intcompinator: Combinator[Int] with {
        override def combine(x: Int, y: Int): Int = {
            x + y
        }
    }

    given personCombinator: Combinator[Person] with {
        override def combine(x: Person, y: Person): Person = {
            Person(x.name+y.name, x.age+y.age)
        }
    }
    

    def main(args: Array[String]): Unit = {
        println(combineAll(aList))
        println(combineAll(sortedPeople))
        println(sortedPeople)
    }  
}
