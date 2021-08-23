package com.itechart.internship.tf

import cats.effect._
import cats.effect.concurrent.Ref
import cats.implicits._
import com.itechart.internship.tf.ItemService.ValidationError
import com.itechart.internship.tf.ItemService.ValidationError.{EmptyName, NegativePrice}

/*
  Additional materials:

  Papers by Oleg Kiselyov http://okmij.org/ftp/tagless-final/index.html
  (Haskell, 18+)

  Что такое tagless final? https://www.youtube.com/watch?v=ZNK57IXgr3M
  (Scala 3, история развития кодировок от Черча до TF)

  Tagless Final lessons series https://www.youtube.com/watch?v=XJ2NjqkWdck&list=PLJGDHERh23x-3_T3Dua6Fwp4KlG0J25DI

  Practical FP in Scala https://leanpub.com/pfp-scala (Chapters 3, 4)
 */

// Part 1

final case class Item(id: Long, name: String, price: BigDecimal)

trait ClassicItemService {
  def all: Map[Long, Item]
  def create(name: String, price: BigDecimal): Either[ValidationError, Item]
  def update(item: Item): Either[ValidationError, Boolean]
  def find(id:     Long): Option[Item]
  def delete(id:   Long): Boolean
}

/*

The first problem with these functions is that they perform some side effects for sure.
The create function will probably persist information on a database, as will the add function.
The find function will eventually retrieve a shopping cart from a database, and this operation
can eventually fail.

Fortunately, many Scala libraries allow developers to encapsulate the operation description
that produces side effects inside some declarative contexts. Such contexts are called effects.
We can use the type of a function to describe the value it will produce and the side effects
through the effect pattern. Hence, the description of the side effect is separated from its
execution. Libraries such as Cats Effects, Monix, or even ZIO, provide us some effect types.

 */

trait ItemService[F[_]] {
  def all: F[Map[Long, Item]]
  def create(name: String, price: BigDecimal): F[Either[ValidationError, Item]]
  def update(item: Item): F[Either[ValidationError, Boolean]]
  def find(id:     Long): F[Option[Item]]
  def delete(id:   Long): F[Boolean]
}

object ItemService {

  sealed trait ValidationError extends Throwable

  object ValidationError {
    case object EmptyName extends ValidationError
    case object NegativePrice extends ValidationError
  }

  private[tf] def validate(name: String, price: BigDecimal): Either[ValidationError, (String, BigDecimal)] =
    for {
      name  <- Either.cond(name.nonEmpty, name, EmptyName)
      price <- Either.cond(price > 0, price, NegativePrice)
    } yield (name, price)

  def of[F[_]: Sync]: F[ItemService[F]] = for {
    counter <- Ref.of[F, Long](0)
    items   <- Ref.of[F, Map[Long, Item]](Map.empty)
  } yield new ItemServiceImpl(counter, items)
}

final private class ItemServiceImpl[F[_]: Sync](
  counter: Ref[F, Long],
  items:   Ref[F, Map[Long, Item]]
) extends ItemService[F] {

  override def all: F[Map[Long, Item]] = items.get

  override def create(name: String, price: BigDecimal): F[Either[ValidationError, Item]] =
    ItemService.validate(name, price).traverse { case (name, price) =>
      for {
        id  <- counter.updateAndGet(_ + 1)
        item = Item(id, name, price)
        _   <- items.update(_.updated(id, item))
      } yield item
    }

  override def update(item: Item): F[Either[ValidationError, Boolean]] =
    ItemService.validate(item.name, item.price).traverse { _ =>
      items.modify { items =>
        if (items.contains(item.id)) items.updated(item.id, item) -> true
        else items                                                -> false
      }
    }

  override def find(id: Long): F[Option[Item]] = items.get.map(_.get(id))

  override def delete(id: Long): F[Boolean] = items.modify { items =>
    items.removed(id) -> items.contains(id)
  }
}

object App1 extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      itemService <- ItemService.of[IO]

      all1 <- itemService.all
      _    <- IO(println(all1))

      _ <- itemService.create("Test Item #1", BigDecimal(10.0))
      _ <- itemService.create("Test Item #2", BigDecimal(15.0))

      all2 <- itemService.all
      _    <- IO(println(all2))

    } yield ExitCode.Success
  }
}
