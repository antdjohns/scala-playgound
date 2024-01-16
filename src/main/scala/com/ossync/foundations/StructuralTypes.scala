package com.ossync.foundations

import reflect.Selectable.reflectiveSelectable


object StructuralTypes {
    type SoundMaker = { // structural type, can be substituted for classes and traits that have same.
        def makeSound(): Unit
    }

    class Dog { 
        def makeSound(): Unit = println("Woof")
    }

    class Car { 
        def makeSound(): Unit = println("Vroom")
    }

    def makeSound(x: SoundMaker): Unit = {
        x.makeSound() // through reflection. this is slow. and happens at runtime. 
    }

    // compile time duck typing
    val dog: SoundMaker = new Dog
    val car: SoundMaker = new Car

    // type refinements
    abstract class Animal {
        def eat(): String
    }

    type WalkinAnimal = Animal {
        def walk(): Int

    }

    // why: creating type-safe APIs for existing types following the same structure, but no connection to eachother.
    type JavaClosable = java.io.Closeable
    class CustomClosable {
        def close(): Unit = println("ok ok I'm closing")
        def closeSilently(): Unit = println("......")
    }

    // def closeResource(closable: JavaClosable | CustomClosable): Unit = 
    //     closable.close() // not ok

    // solution: structural type
    type UnifiedClosable = {
        def close(): Unit
    }

    def closeResource(closable: UnifiedClosable): Unit = closable.close()
    val jClosable = new JavaClosable {
        def close(): Unit = println("closing jclosable")
    }

    val cClosabel = new CustomClosable

    def closeResource_v2(closabel: {def close(): Unit }): Unit = closabel.close()

    def main(args: Array[String]): Unit = {
        val c = new Car
        val d = new Dog
        makeSound(c)
        makeSound(d)
        closeResource(jClosable)
        closeResource(cClosabel)
    }
}
