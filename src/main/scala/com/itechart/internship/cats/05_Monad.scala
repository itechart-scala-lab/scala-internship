package com.itechart.internship.cats

import cats.effect.ExitCode

object Monad {

  /** If we speak about monads in functional programming it would be more or less safe to say that anything,
    * that implements two operations `bind` and `unit` and satisfies three monadic laws is a monad.
    * `Unit` usually called `pure` in Scala, and `bind` is a `flatMap`.
    *
    * Monadic laws are:
    * right identity - calling pure and transforming the result with func is the same as calling func
    * left identity - passing pure to flatMap is the same as doing nothing
    * associativity - m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))
    * A simple definition may look as follows:
    */
  trait CustomMonad[F[_]] {
    def pure[A](a: A): F[A]

    def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

    /** Ex 5.0 implement map in terms of pure and flatMap.
      */
    def map[A, B](fa: F[A])(f: A => B): F[B] = ??? /* your code here */
  }

  /** Looks familiar, right? You already encountered some monads, for example  `Option[A]` is a monad that
    * models an effect of optionality of a value of type A, `Either[E, A]` models an effect which evaluation
    * may fail with an error of type `E` or succeed with a value of type `A`.
    *
    * Monads bring he power of composition. Having `flatMap` we can now chain our computations. For example:
    */
  trait User
  trait GiftCard
  trait Gift

  def findUser(name:     String):   Option[User]     = ???
  def findGiftCard(user: User):     Option[GiftCard] = ???
  def pickGift(card:     GiftCard): Option[Gift]     = ???

  lazy val maybeGift1: Option[Gift] =
    findUser("Bob").flatMap(findGiftCard).flatMap(pickGift)

  // Or, we can use `for-comprehension`:
  lazy val maybeGift2: Option[Gift] = for {
    user <- findUser("Bob")
    card <- findGiftCard(user)
    gift <- pickGift(card)
  } yield gift

  /** Notice, that we describe the whole computation as if we have all the parts in place despite
    * the fact that user, card, or gift may be missing.
    */

  /** Ex 5.1 implement CustomMonad for List
    */
  val listM: CustomMonad[List] = new CustomMonad[List] {
    override def pure[A](a: A): List[A] = List(a)

    override def flatMap[A, B](fa: List[A])(f: A => List[B]): List[B] =
      ???
  }

  /** Ex 5.2 implement CustomMonad for Option
    */
  val optionM: CustomMonad[Option] = new CustomMonad[Option] {
    override def pure[A](a: A): Option[A] = ???

    override def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] =
      ???
  }

  /** When we work with a concrete Monad like List or Option we can call .flatMap .pure or .map directly.
    * But what if we have some abstract effect F[_]?
    * As we have seen before, we can demand an instance of a certain type class to be present in the implicit context.
    * And we need also to import syntax for certain operations:
    */
  import cats.Monad
  import cats.syntax.flatMap._
  import cats.syntax.functor._ // map

  def someFancyFunc[F[_]: Monad](log: String => F[Unit], doSomethingElse: F[Unit]): F[ExitCode] =
    log("Feels good")
      .flatMap(_ => doSomethingElse)
      .map(_ => ExitCode.Success)
}
