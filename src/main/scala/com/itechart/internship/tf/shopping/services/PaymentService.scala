package com.itechart.internship.tf.shopping.services

import cats.Applicative
import com.itechart.internship.tf.shopping.domain.payment.{Payment, PaymentId}

trait PaymentService[F[_]] {
  def process(payment: Payment): F[PaymentId]
}

object PaymentService {

  def apply[F[_]: Applicative]: PaymentService[F] = (payment: Payment) => {
    Applicative[F].pure(PaymentId("123"))
  }

}
