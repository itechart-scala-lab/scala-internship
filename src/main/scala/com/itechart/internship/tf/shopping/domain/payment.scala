package com.itechart.internship.tf.shopping.domain

import com.itechart.internship.tf.shopping.domain.card.Card
import com.itechart.internship.tf.shopping.domain.money.Money
import com.itechart.internship.tf.shopping.domain.user.UserId
import io.circe.generic.JsonCodec

object payment {

  @JsonCodec
  final case class PaymentId(value: String)

  final case class Payment(userId: UserId, amount: Money, card: Card)

}
