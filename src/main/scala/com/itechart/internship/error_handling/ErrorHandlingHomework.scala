package com.itechart.internship.error_handling

import cats.data.ValidatedNec

object ErrorHandlingHomework {

  // think about adding Refined integration here to provide type level validation
  final case class Account(person: Person, card: PaymentCard)
  final case class Person(/* Add parameters as needed */ ) // name, age, birthDay, passportNumber
  final case class PaymentCard(/* Add parameters as needed */ ) // card number, expirationDate, securityCode etc

  sealed trait AccountValidationError
  object AccountValidationError {
    ??? // Add errors as needed
  }

  object AccountValidator {

    type AllErrorsOr[A] = ValidatedNec[AccountValidationError, A]

    def validate(person: Person, card: PaymentCard): AllErrorsOr[Account] = ???
  }
}
