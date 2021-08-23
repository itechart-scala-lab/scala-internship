package com.itechart.internship.tf.shopping.domain

import com.itechart.internship.tf.shopping.domain.cart.CartItem
import com.itechart.internship.tf.shopping.domain.money.Money
import com.itechart.internship.tf.shopping.domain.payment.PaymentId
import com.itechart.internship.tf.shopping.domain.user.UserId
import io.circe.generic.JsonCodec

import java.util.UUID

object order {

  @JsonCodec
  final case class OrderId(value: UUID)

  @JsonCodec
  final case class Order(id: OrderId, userId: UserId, paymentId: PaymentId, items: List[CartItem], total: Money)

}
