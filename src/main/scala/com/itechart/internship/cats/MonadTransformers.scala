package com.itechart.internship.cats

import cats.Monad
import cats.data.EitherT
import cats.effect.IO
import cats.implicits._

object For {

  // API
  type Error
  type UserId
  type OrderId
  type Item

  trait Repository {

    def getFriends(userId: UserId):  Either[Error, List[UserId]] = ???
    def getOrder(userId:   UserId):  Either[Error, OrderId]      = ???
    def getItems(orderId:  OrderId): Either[Error, List[Item]]   = ???
  }

  class Task1(repo: Repository) {
    // find all friends for users passed
    def friends(users: List[UserId]): Either[Error, List[UserId]] = {
      users
        .map(x => repo.getFriends(x))
        .sequence
        .map(x => x.flatten)
    }
  }

  // implement
  class Service(repo: Repository) {

    def friendsOrders(userId: UserId): Either[Error, List[Item]] = {
      for {
        friendsIds <- repo.getFriends(userId)
        orderIds   <- friendsIds.traverse(id => repo.getOrder(id))
        itemsIds   <- orderIds.traverse(id => repo.getItems(id))
      } yield itemsIds.flatten

//      for {
//        friendsIds <- repo.getFriends(userId)
//        orderIds <- friendsIds.map(id => repo.getOrder(id)).sequence
//        itemsIds <- orderIds.map(id => repo.getItems(id)).sequence
//      } yield itemsIds.flatten
    }
  }

}

object MonadTransformers {

  // API
  type Error
  type UserId
  type OrderId
  type Item

  trait Repository {

    def getFriends(userId: UserId):  IO[Either[Error, List[UserId]]] = ???
    def getOrder(userId:   UserId):  IO[Either[Error, OrderId]]      = ???
    def getItems(orderId:  OrderId): IO[Either[Error, List[Item]]]   = ???
  }

  // implement
  class Service(repo: Repository) {

    def friendsOrders(userId: UserId): IO[Either[Error, List[Item]]] = {
      val res = for {
        friendsIds <- EitherT(repo.getFriends(userId))
        orderIds   <- friendsIds.traverse(id => EitherT(repo.getOrder(id)))
        itemsIds   <- orderIds.traverse(id => EitherT(repo.getItems(id)))
      } yield itemsIds.flatten
      res.value
    }
  }
}

object Generic {

  // API
  type Error
  type UserId
  type OrderId
  type Item

  trait Repository[F[_]] {

    def getFriends(userId: UserId): F[List[UserId]] = ???

    def getOrder(userId: UserId): F[OrderId] = ???

    def getItems(orderId: OrderId): F[List[Item]] = ???
  }

  // implement
  class Service[F[_]: Monad](repo: Repository[F]) {

    def friendsOrders(userId: UserId): F[List[Item]] = ???
  }

//  new Repository of IO
//  new Repository of Either
//  new Repository of EitherT
//  new Repository of IO Either

}
