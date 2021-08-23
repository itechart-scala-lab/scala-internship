package com.itechart.internship.tf.shopping.routers

import cats.Monad
import cats.data.{Kleisli, OptionT}
import cats.syntax.all._
import com.itechart.internship.tf.shopping.domain.cart.Quantity
import com.itechart.internship.tf.shopping.domain.item.ItemId
import com.itechart.internship.tf.shopping.domain.money.Money
import com.itechart.internship.tf.shopping.domain.user.UserId
import com.itechart.internship.tf.shopping.effects.CurrencySupport.CurrencySupportStringOps
import com.itechart.internship.tf.shopping.effects.ToNumeric.ToNumericStringOps
import com.itechart.internship.tf.shopping.effects.{CurrencySupport, GenUUID, ToNumeric}
import com.itechart.internship.tf.shopping.services.ShoppingCartService

object ShoppingCartRouter {

  def apply[F[_]: Monad: GenUUID: ToNumeric: CurrencySupport](
    shoppingCartService: ShoppingCartService[F]
  ): Kleisli[OptionT[F, *], List[String], String] = Kleisli[OptionT[F, *], List[String], String] {
    case "get" :: userId :: _ =>
      OptionT.liftF {
        for {
          userId <- GenUUID[F].read(userId)
          result <- shoppingCartService.get(UserId(userId))
        } yield result.toString
      }

    case "add" :: userId :: itemId :: quantity :: price :: currency :: _ =>
      OptionT.liftF {
        for {
          userId   <- GenUUID[F].read(userId)
          itemId   <- GenUUID[F].read(itemId)
          quantity <- quantity.toLongF
          price    <- price.toBigDecimalF
          currency <- currency.toCurrency
          result   <- shoppingCartService.add(UserId(userId), ItemId(itemId), Quantity(quantity), Money(price, currency))
        } yield result.toString
      }

    case _ => OptionT.none
  }

}
