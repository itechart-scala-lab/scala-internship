package com.itechart.internship.adt

object Variance {

  // list is recursive
  object MyList {

    sealed trait List[+T]

    object Empty extends List[Nothing]

    case class NonEmpty[T](head: T, tail: List[T]) extends List[T]

    NonEmpty(1, NonEmpty(2, NonEmpty(3, Empty)))
  }

  val list  = List(1, 2, 3, 4, 5, 6)
  val list2 = List("a", "b", "c", "d", "e")

  /*
       |        Variance        |       CoVariance       |
       +------------------------+------------------------+
       |       MyType[+T]       |       MyType[-T]       |
       +------------------------+------------------------+
       | A -> B                 | A -> B                 |
       | MyType[A] -> MyType[B] | MyType[A] <- MyType[B] |
       +------------------------+------------------------+

     Lets say Int is a Number

     List has + because
     if I want List[Number] I can have List[Int] instead

     JsonPrinter (it converts a type to json) would have - because
     if I want JsonPrinter[Int] I can have JsonPrinter[Number] as it can print my Int as well as any other Number
   */

  /*
    Function is -A => +B
    if i need a function which returns Numbers I am ok with functions returning Int since Int is a Number
    if i need a function which processes Ints I am ok with functions processing any Number
   */

  // More information:
  // https://www.youtube.com/watch?v=aUmj7jnXet4
}
