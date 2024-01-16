package com.ossync.foundations


object FBoundedPolymorphism {

    object Problem {
        trait Animal {
            def breed: List[Animal]
        }

        class Cat extends  Animal {
            override def breed: List[Animal] = List(new Cat, new Cat)
        }

        class Dog extends Animal {
            override def breed: List[Animal] = List(new Dog, new Dog, new Dog)
        }
    }

    object NaiveSolution {
        trait Animal {
            def breed: List[Animal]
        }

        class Cat extends  Animal {
            override def breed: List[Cat] = List(new Cat, new Cat)
        }

        class Dog extends Animal {
            override def breed: List[Dog] = List(new Dog, new Dog, new Dog)
        }
    }

    // This is an isue becasue we have to write the proper type signature.
    // problem: want the compiler to help.

    object FBP {
        trait Animal[A <: Animal[A]] { // recursive type, F-Bounded Polymorphism
            def breed: List[Animal[A]]
        }

        class Cat extends Animal[Cat] {
            override def breed: List[Animal[Cat]] = List(new Cat, new Cat)
        }
    }

    // example: some ORM libraries use F-bounded polymorphism
    trait Entity[E <: Entity[E]]
    // example: Java compairson/ sorting library
    class Person extends Comparable[Person] {
        override def compareTo(o: Person): Int = ???
    }

    // FBP + self types
    object FBPSelf {
        trait Animal[A <: Animal[A]] { self: A =>  // ensures that A is the class. class Croc extends Animal[T <- T must be Corc]
            def breed: List[Animal[A]]    
        }
        class Croc extends Animal[Croc] {
            override def breed: List[Animal[Croc]] = ???
        }
        class Rhino extends Croc {
            override def breed: List[Animal[Croc]] = List(new Croc, new Rhino)
        }

        class Fish[A <: Fish[A]] extends Animal[Fish[A]] { self: A => 
            override def breed: List[Animal[Fish[A]]] = ???    
        }

        class Tuna extends Fish[Tuna] {
            override def breed: List[Animal[Fish[Tuna]]] = List(new Tuna)
        }

        // class Cod extends Fish[Cod] {
        //     override def breed: List[Animal[Fish[Cod]]] = List(new Tuna)
        // }

        // class Shark extends Tuna {
        //     override def breed: List[Animal[Fish[Tuna]]] = List(new Cod)
        // }
    }


  

    def main(args: Array[String]): Unit = {

    }
}
