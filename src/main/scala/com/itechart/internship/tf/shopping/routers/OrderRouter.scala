package com.itechart.internship.tf.shopping.routers

import cats.Monad
import cats.data.{Kleisli, OptionT}
import cats.syntax.all._
import com.itechart.internship.tf.shopping.domain.order.OrderId
import com.itechart.internship.tf.shopping.effects.{CurrencySupport, GenUUID, ToNumeric}
import com.itechart.internship.tf.shopping.services.OrderService

object OrderRouter {

  def apply[F[_]: Monad: GenUUID: ToNumeric: CurrencySupport](
    orderService: OrderService[F]
  ): Kleisli[OptionT[F, *], List[String], String] = Kleisli[OptionT[F, *], List[String], String] {
    case "find" :: orderId :: _ =>
      OptionT.liftF {
        for {
          orderId <- GenUUID[F].read(orderId)
          result  <- orderService.find(OrderId(orderId))
        } yield result.toString
      }

    case _ => OptionT.none
  }

}
