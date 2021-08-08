package com.itechart.internship.basics

import scala.util.Try

object DataStructures {

  // Mutable vs Immutable collections

  // Both mutable and immutable collections are provided by the Scala standard library
  // Mutable collections can be updated or extended in place and doing so is a side effect.

  // Immutable collections never change, however they have operations that return new collections with
  // added, removed or updated elements. The original collection, however, remains unchanged.

  val mutableList = scala.collection.mutable.ListBuffer(1, 2, 3)
  mutableList.update(1, -1)

  val immutableList1 = scala.collection.immutable.List(1, 2, 3)
  val updatedImmutableList = immutableList1.updated(1, -1)

  val doTheyHaveEqualContents1 = mutableList == updatedImmutableList // true
  val doTheyHaveEqualContents2 = (mutableList zip updatedImmutableList) forall { case (a, b) => a == b }

  // Arrays
  //
  // Arrays are mutable, indexed collections which are equivalent to Java's array's. They are indexed from 0.
  val array = Array(1, 2, 3, 4, 5)
  array(2) // read the 3rd element, it will be 3
  array(3) = 7 // update the 4th element to be 7

  // Lists
  //
  // Immutable list represents ordered collections of elements of type A.
  // It is optimal for last-in-first-out (LIFO) or stack like access patterns. Prepending an element
  // or obtaining the "tail" (all elements except the first one) are fast operations.

  val immutableList2 = List(1, 2, 3)
  val immutableList3 = 1 :: 2 :: 3 :: Nil
  require(immutableList2 == immutableList3) // the two ways above are the same

  val emptyList1 = Nil
  val emptyList2 = List()
  val emptyList3 = List.empty

  val prepend4 = 4 :: immutableList2 // 4 :: 1 :: 2 :: 3 :: Nil
  val prepend42 = 5 :: immutableList2 // 5 :: 1 :: 2 :: 3 :: Nil
  val tailOfList = immutableList2.tail // 2 :: 3 :: Nil

  val joinLists = immutableList2 ::: List(8, 9) // 1 :: 2 :: 3 :: 8 :: 9 :: Nil

  val headOfList1 = Try(emptyList1.head)// what will happen here?!
  val headOfList2 = emptyList1.headOption // None
  val headOfList3 = immutableList2.headOption // Some(1)

  // Question. We have seen `Nil`, `None`, `Nothing` and `null` so far. What do they each mean?

  // Vectors
  //
  // Vectors are general purpose, immutable data structures with effectively constant time random access
  // and updates, as well as fast append and prepend.

  val vector1 = Vector(1, 2, 3, 4)
  val vector2 = 5 +: vector1 // prepend
  val vector3 = vector1 :+ 6 // append
  val vector4 = vector2 ++ vector3 // concatenate

  // Sets
  //
  // Sets are data structures that don't contain any duplicate elements.
  // The default Scala `Set` is unordered and immutable.

  val vegetables = Set("tomatoes", "peppers", "pumpkins", "cucumbers", "olives")
  vegetables("tomatoes") // true
  vegetables("apple") // false
  vegetables.contains("tomatoes") // true, same thing

  val moreVegetables = vegetables + "avocado"
  val lessVegetables = moreVegetables - "peppers"

  // Exercise. Write a function that checks if all values in a `List` are equal.
  // Think about what you think your function should return if `list` is empty, and why.
  def allEqual[T](list: List[T]): Boolean = {
    false // TODO: implement
  }

  // Maps
  //
  // Maps consist of pairs of keys and values and usually offer fast lookup by key.
  // The default Scala `Map` is unordered and immutable.

  val vegetableWeights = Map(
    ("pumpkins", 10),
    ("cucumbers", 20),
    ("olives", 2),
  )

  val vegetablePrices = Map(
    "tomatoes" -> 4,
    "peppers" -> 5,
    "olives" -> 17,
  )

  val moreVegetablePrices = vegetablePrices + ("pumpkins" -> 3)
  val lessVegetableWeights = vegetableWeights - "pumpkins"

  val questionableMap = vegetableWeights ++ vegetablePrices

  // Question. Why should `questionableMap` be considered questionable?

  val vegetableAmounts = Map(
    "tomatoes" -> 17,
    "peppers" -> 234,
    "olives" -> 32,
    "cucumbers" -> 323,
  )

  val tomatoAmount: Int = vegetableAmounts("tomatoes")
  val tomatoAmountOpt: Option[Int] = vegetableAmounts.get("tomatoes")
  val carrotAmountWithDefault: Int = vegetableAmounts.getOrElse("carrots", 0)

  // Exercise. Calculate the total cost of all vegetables, taking vegetable amounts (in units) from
  // `vegetableAmounts` and prices per unit from `vegetablePrices`. Assume the price is 10 if not available
  // in `vegetablePrices`.
  val totalVegetableCost: Int = {
    17 // implement here
  }

  // Exercise. Given the vegetable weights (per 1 unit of vegetable) in `vegetableWeights` and vegetable
  // amounts (in units) in `vegetableAmounts`, calculate the total weight per type of vegetable, if known.
  //
  // For example, the total weight of "olives" is 2 * 32 == 64.
  val totalVegetableWeights: Map[String, Int] = { // implement here
    Map()
  }

  // Ranges and Sequences
  val inclusiveRange: Seq[Int] = 2 to 4    // 2, 3, 4, or <=
  val exclusiveRange: Seq[Int] = 2 until 4 // 2, 3, or <
  val withStep: Seq[Int] = 2 to 40 by 7    // 2, 9, 16, 23, 30, 37

  // Seq, IndexedSeq and LinearSeq traits are implemented by many collections and contain various useful
  // methods. See https://docs.scala-lang.org/overviews/collections/seqs.html in case you are interested
  // to learn more about them at this point.

  // The collections API is rich and implements a large number of useful methods, such as:
  // - contains
  // - containsSlice
  // - count
  // - distinct
  // - drop
  // - dropWhile
  // - empty
  // - endsWith
  // - exists
  // - filter
  // - filterNot
  // - find
  // - flatMap
  // - flatten
  // - foldLeft
  // - foldRight
  // - forall
  // - groupBy
  // - head
  // - headOption
  // - init
  // - intersect
  // - last
  // - lastOption
  // - map
  // - max
  // - min
  // - nonEmpty
  // - partition
  // - reverse
  // - size
  // - slice
  // - sort
  // - sortWith
  // - startsWith
  // - tail
  // - take
  // - takeRight
  // - takeWhile
  // - zip

  // Exercise: Return a set with all subsets of the provided set `set` with `n` elements
  // For example, `allSubsetsOfSizeN(Set(1, 2, 3), 2) == Set(Set(1, 2), Set(2, 3), Set(1, 3))`.
  // Hints for implementation:
  //   - Handle the trivial case where `n == 1`.
  //   - For other `n`, for each `set` element `elem`, generate all subsets of size `n - 1` from the set
  //     that don't include `elem`, and add `elem` to them.
  def allSubsetsOfSizeN[A](set: Set[A], n: Int): Set[Set[A]] = {
    // replace with correct implementation
    println(n)
    Set(set)
  }

  // Homework
  //
  // Implement a special sort which sorts the keys of a map (K) according to their associated
  // values (V).
  //
  // In case of "ties" (equal values) it should group these keys K into Set-s in the results.
  //
  // The input is a map where keys (K) are the values to be sorted and values are their associated numeric
  // values.
  //
  // The output is a list (in ascending order according to the associated `Int` values) of tuples of `Set`-s
  // with values from K, and the associated value V for these values in the `Set`.
  //
  // For example:
  //
  // Input `Map("a" -> 1, "b" -> 2, "c" -> 4, "d" -> 1, "e" -> 0, "f" -> 2, "g" -> 2)` should result in
  // output `List(Set("e") -> 0, Set("a", "d") -> 1, Set("b", "f", "g") -> 2, Set("c") -> 4)`.
  def sortConsideringEqualValues[T](map: Map[T, Int]): List[(Set[T], Int)] = ???


  /**
    * Exploring Functional Sets
     */

  val aSet = Set(1,2,3,4,5)

/*
  The critical API of a set consists of

  * the ability to tell whether an item is in the set or not
  * the ability to add an element to a set (and if it exists, don’t add it again)
  * the ability to remove an element from the set (and if it doesn’t exist, don’t remove it again, of course)
*/

//  Let’s concentrate on the first capability at the moment. The way we tell whether an element is in the set is by calling contains, or apply:

  aSet.contains(2) // true
  aSet(2) // also true

/*
  Notice that the apply method makes the set “callable” like a function.
  At the same time, that invocation always returns a value (true or false), for any argument you pass to it.

  So notice that a set behaves like a function A => Boolean, because you can pass any argument of type A, and you’ll get a Boolean (whether or not the argument is in the set).

  Here’s an outrageous idea: sets ARE functions!

  If you dive deeper into the Set definition from the standard library, you’ll eventually find a declaration that fits:

  trait Set[A] extends Iterable[A]
    with collection.Set[A]
    with SetOps[A, Set, Set[A]]
    with IterableFactoryDefaults[A, Set] {
    ...
  }

  // in the scala.collection.immutable package

  trait SetOps[A, +CC[X], +C <: SetOps[A, CC, C]]
    extends collection.SetOps[A, CC, C] {
    ...
  }

  // in the general scala.collection package

  trait SetOps[A, +CC[_], +C <: SetOps[A, CC, C]]
    extends IterableOps[A, CC, C]
      with (A => Boolean) { // <-- jackpot!
    ...
  }*/

  /**
    * Writing a Small Functional Set
    */

//  Here’s a small experiment — let’s write a small implementation of a set that implements the functional interface A => Boolean:

  trait RSet[A] extends (A => Boolean) {
    def apply(x: A): Boolean = contains(x)
    def contains(x: A): Boolean
    def +(x: A): RSet[A]
    def -(x: A): RSet[A]
  }

/*
  The main trait implements the crucial Set API:

  * testing if an element is in the set
  * adding an element
  * removing an element

  Let’s then continue with an implementation of an empty set, correctly typed.
  The standard library uses an object typed with Set[Any] and then type-checked via casting, but let’s use a small case class for our experiment:
*/

  case class REmpty[A]() extends RSet[A] {
    override def contains(x: A) = false
    def +(x: A): RSet[A] = ???
    def -(x: A): RSet[A] = this
  }

  /*
  The implementation of 2 out of 3 methods is easy:

  * the set doesn’t contain anything, so contains(x) == false for all x in A
  * the set can’t remove anything, so return the same set
  We’ll come back to the third method shortly.

  Let’s now consider a set given by a property, i.e. similarly to how we were taught in math classes.
  For example, the set of all even natural numbers is something like { x in N | x % 2 == 0 }.
  Pure sets in mathematics are described by their properties.
  Some sets may be finite, or infinite, some may be countable (or not).

  We’re not going to dive super-deep into the rabbit hole — that’s a job for my students in the advanced Scala course — but let’s try declaring a small set in terms of the property of their elements (a property-based set):
*/

  case class PBSet[A](property: A => Boolean) extends RSet[A] {
    def contains(x: A): Boolean = property(x)
    def +(x: A): RSet[A] = new PBSet[A](e => property(e) || e == x)
    def -(x: A): RSet[A] = if (contains(x)) new PBSet[A](e => property(e) && e != x) else this
  }

/*
  Let’s look at the main API methods:

  * this set is all about the property of the elements, so contains returns true only if that property is satisfied
  * adding an element means adjusting the property so that it also holds true for the element we want to add
  * removing an element means adjusting the property so that it definitely returns false for the element we’re removing
  And that’s it! The set will not contain duplicates nor change if we try removing a non-existent element, because neither makes any sense now.

  Coming back to REmpty’s add method, it will look like this:
  def +(x: A): RSet[A] = new PBSet[A](_ == x)

  A single-element set is a property-based set, where the property only returns true for that particular element.

  Of course, we can test this out. Let’s make a small helper method that will allow us to build sets more easily:
*/

  object RSet {
    def apply[A](values: A*) = values.foldLeft[RSet[A]](new REmpty())(_ + _)
  }
/*
  Then in main, we can run some tests:

  val first5Elements: RSet[Int] = REmpty[Int]() + 1 + 2 + 3 + 4 + 5
  val first5lementsFancy = RSet(1,2,3,4,5)
  val first1000Elements = RSet(1 to 1000: _*) // pass as varargs

  first5Elements(42) // false
  first5lementsFancy(3) // true
  first1000Elements(68) // true

  Is this cool or what?

  The interesting thing about this set definition is that you can now declare infinite sets, just based on their property.
  For example, the set of even natural numbers is now trivial:

  val allEvens = PBSet[Int](_ % 2 == 0)
*/


}
