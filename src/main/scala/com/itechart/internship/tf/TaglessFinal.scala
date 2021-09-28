package com.itechart.internship.tf

import cats.Applicative
import cats.effect._
import cats.effect.concurrent.Ref
import cats.implicits._
import com.itechart.internship.tf.ItemValidator.ItemValidationError._
import com.itechart.internship.tf.ItemValidator._

import scala.concurrent.Future

/*
  Additional materials:

  Papers by Oleg Kiselyov http://okmij.org/ftp/tagless-final/index.html
  (Haskell, 18+)

  Что такое tagless final? https://www.youtube.com/watch?v=ZNK57IXgr3M
  (Scala 3, история развития кодировок от Черча до TF)

  Tagless Final lessons series https://www.youtube.com/watch?v=XJ2NjqkWdck&list=PLJGDHERh23x-3_T3Dua6Fwp4KlG0J25DI

  Practical FP in Scala https://leanpub.com/pfp-scala (Chapters 3, 4)
 */

final case class Item(id: Long, name: String, price: BigDecimal)

trait ClassicItemService {
  def all: Map[Long, Item]
  def create(name: String, price: BigDecimal): Either[ItemValidationError, Item]
  def update(item: Item): Either[ItemValidationError, Boolean]
  def find(id:     Long): Option[Item]
  def delete(id:   Long): Boolean
}

/*

The first problem with these functions is that they perform some side effects for sure.
The create function will probably persist information on a database, as will the add function.
The find function will eventually retrieve an item from a database, and this operation
can eventually fail. One option is to use Future (accepting all it cons)

 */

trait FutureItemService {
  def all: Future[Map[Long, Item]]
  def create(name: String, price: BigDecimal): Future[Either[ItemValidationError, Item]]
  def update(item: Item): Future[Either[ItemValidationError, Boolean]]
  def find(id:     Long): Future[Option[Item]]
  def delete(id:   Long): Future[Boolean]
}

/*

Fortunately, many Scala libraries allow developers to encapsulate the operation description
that produces side effects inside some declarative contexts. Such contexts are called effects.
We can use the type of a function to describe the value it will produce and the side effects
through the effect pattern. Hence, the description of the side effect is separated from its
execution. Libraries such as Cats Effects, Monix, or even ZIO, provide us some effect types.

 */

trait IOItemService {
  def all: IO[Map[Long, Item]]
  def create(name: String, price: BigDecimal): IO[Either[ItemValidationError, Item]]
  def update(item: Item): IO[Either[ItemValidationError, Boolean]]
  def find(id:     Long): IO[Option[Item]]
  def delete(id:   Long): IO[Boolean]
}

// How I can make ItemService abstract from the way how side effects are handled?
// I just want to define interface for business logic ...

// Tagless Final is on board!

trait ItemService[F[_]] {
  def all: F[Map[Long, Item]]
  def create(name: String, price: BigDecimal): F[Either[ItemValidationError, Item]]
  def update(item: Item): F[Either[ItemValidationError, Boolean]]
  def find(id:     Long): F[Option[Item]]
  def delete(id:   Long): F[Boolean]
}

object ItemService {

  // [F[_]: Sync] - instead of Sync you can define Functor, Monad, Application etc ... as a TYPE
  // to be more specific and use particular features of Monads
  // Example: For Expression with multiple generators

  def of[F[_]: Sync]: F[ItemService[F]] = for {
    counter <- Ref.of[F, Long](0)
    items   <- Ref.of[F, Map[Long, Item]](Map.empty)
  } yield new InMemoryItemService(counter, items)
}

object ItemValidator {
  sealed trait ItemValidationError extends Throwable

  object ItemValidationError {
    case object EmptyName extends ItemValidationError
    case object NegativePrice extends ItemValidationError
  }

  def validate(name: String, price: BigDecimal): Either[ItemValidationError, (String, BigDecimal)] =
    for {
      name  <- Either.cond(name.nonEmpty, name, EmptyName)
      price <- Either.cond(price > 0, price, NegativePrice)
    } yield (name, price)
}

// About Ref: https://www.pluralsight.com/tech-blog/scala-cats-effect-ref/

final private class InMemoryItemService[F[_]: Sync](
  counter: Ref[F, Long],
  items:   Ref[F, Map[Long, Item]]
) extends ItemService[F] {

  override def all: F[Map[Long, Item]] = items.get

  // Ref: update can perform a “get and set” operation
  override def create(name: String, price: BigDecimal): F[Either[ItemValidationError, Item]] =
    ItemValidator.validate(name, price).traverse { case (name, price) =>
      for {
        id  <- counter.updateAndGet(_ + 1)
        item = Item(id, name, price)
        _   <- items.update(_.updated(id, item))
      } yield item
    }

  // // Ref: modify can perform a “get and set and get” operation
  override def update(item: Item): F[Either[ItemValidationError, Boolean]] =
    ItemValidator.validate(item.name, item.price).traverse { _ =>
      items.modify { items =>
        if (items.contains(item.id)) items.updated(item.id, item) -> true
        else items                                                -> false
      }
    }

  override def find(id: Long): F[Option[Item]] = ???

  override def delete(id: Long): F[Boolean] =
    items.modify { items =>
      items.removed(id) -> items.contains(id)
    }
}

trait Logger[F[_]] {
  def info(msg:  String): F[Unit]
  def error(msg: String): F[Unit]
}

object Logger {
  def of[F[_]: Applicative](component: String): Logger[F] = {
    new Logger[F] {
      override def info(msg:  String): F[Unit] = println(s"$component - INFO: $msg").pure[F]
      override def error(msg: String): F[Unit] = println(s"$component - ERROR: $msg").pure[F]
    }
  }
}

object ItemApp extends IOApp {

  def program[F[_]: Sync]: F[Unit] = for {

    itemService <- ItemService.of[F]
    logger       = Logger.of[F]("ItemApp")

    allBefore <- itemService.all
    _         <- logger.info(s"Initial data: $allBefore")

    _ <- itemService.create("Test Item #1", BigDecimal(10.0))
    _ <- itemService.create("Test Item #2", BigDecimal(15.0))

    creationError <- itemService.create("Test Item #3", BigDecimal(0.0))

    _ <- logger.error(
      creationError.fold(_.toString, _ => s"Item creation isn't expected")
    )

    allAfter <- itemService.all
    _        <- logger.info(s"Result data: $allAfter")
  } yield ()

  override def run(args: List[String]): IO[ExitCode] = for {
    _ <- program[IO]
  } yield ExitCode.Success
}

/*
  CONCLUSION

  Tagless Final - is a functional design pattern, that helps to define an interface
  of your application in an abstract from side effects way.

  For example, you have UserService with some CRUD operations, which produce side effects,
  so those side effects are encapsulated into specific data structures, for example, Monads.
  But the thing is you want to be flexible which exact monad should be used…
  you want Either, Future, IO or something else… it doesn't matter with tagless final.

 */
