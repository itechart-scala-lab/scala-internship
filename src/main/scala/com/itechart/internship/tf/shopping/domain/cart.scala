package com.itechart.internship.tf.shopping.domain

import com.itechart.internship.tf.shopping.domain.item.ItemId
import com.itechart.internship.tf.shopping.domain.money.Money
import com.itechart.internship.tf.shopping.domain.user.UserId
import io.circe.generic.JsonCodec

import java.util.Currency

object cart {

  @JsonCodec
  final case class Quantity(amount: Long)
  final case class CartTotal(items: List[CartItem], total: Money)
  @JsonCodec
  final case class CartItem(itemId: ItemId, quantity: Quantity, price: BigDecimal)
  final case class Cart(userId: UserId, items: List[CartItem], currency: Currency)

}
