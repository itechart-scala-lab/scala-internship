package typesystem

import java.util.UUID

object Variance extends App {

  def function(number: Number): Number = {
    val number: Double = 2.0
    number
  }

  val number: Long = 1L
  function(number)

  //Covariance (+). T[+A]
  //B <: A class B extends A
  //T[B] <: T[A]

  //Invariance. T[A]
  //B <: A
  //T[B] no relation T[A]

  //Contravariance (-)
  //B <: A
  //T[A] <: T[B]

  trait Animal

  trait Mammal extends Animal

  trait Reptile extends Animal

  class Dog(val name: String = "dog") extends Mammal

  class Cat(val name: String = "cat") extends Mammal

  class Lizard(val name: String = "lizard") extends Reptile

  class Alligator(val name: String = "alligator") extends Reptile

  class Supergator(override val name: String = "supergator") extends Alligator

  sealed trait Cage

  case class MammalCage[+T <: Mammal](mammal: T) extends Cage

  case class ReptileCage[T <: Reptile](reptile: T) extends Cage

  //such cage can not be created since animal is in covariant position, that is not allowed
  //case class ReptileContravariantCage[-T <: Reptile](reptile: T) extends Cage //This code won't be compiled

  //This method is able take any animal from cage. Mammals are less dangerous then other species,
  //so can be taken out of the cage without any special equipment. That's why mammals are in covariant cage
  def takeMammalFromCage(cage: MammalCage[Mammal]): Mammal = cage.mammal

  val cat: Mammal = takeMammalFromCage(MammalCage[Cat](new Cat()))
  val dog: Mammal = takeMammalFromCage(MammalCage[Dog](new Dog()))

  //Alligator is a dangerous species, every kind of an alligator needs a special approach to be taken out of the cage.
  //That's why alligator is in invariant cage.
  def takeAlligatorFromCage(cage: ReptileCage[Alligator]): Alligator = cage.reptile

  val alligator: Alligator = takeAlligatorFromCage(ReptileCage[Alligator](new Alligator()))
//    val supergator: Supergator = takeAlligatorFromCage(ReptileCage[Supergator](new Supergator()))

  def takeSupergatorFromCage(cage: ReptileCage[Supergator]): Supergator = cage.reptile
  val supergator: Supergator = takeSupergatorFromCage(ReptileCage[Supergator](new Supergator()))

  type Prescription = String

  case class ReptileVet[-T <: Reptile](name: String) {
    def heal(reptile: T): Prescription = s"Everything will be ok"
  }

  def healAlligator(vet: ReptileVet[Alligator], alligator: Alligator): Prescription = {
    vet.heal(alligator)
  }

  val alligatorPrescription = healAlligator(ReptileVet[Reptile]("John Smith"), new Alligator())
//  val supergatorPrescription = healAlligator(ReptileVet[Reptile], new Supergator())

  //Real world Example
  trait Event

  case class UserCreatedEvent(username: String, userId: String) extends Event

  case class OrderCreatedEvent(orderId: String, orderList: List[Int]) extends Event

  //Sends event to message queue
  class EventConsumer[-T <: Event]() {
    //serialize message and send to queue
    def consume(event: T): Boolean = ???
  }

  class OrderService(eventConsumer: EventConsumer[OrderCreatedEvent]) {
    def createOrder(orderList: List[Int]): Boolean =
      eventConsumer.consume(OrderCreatedEvent(UUID.randomUUID().toString, orderList))
  }

  class UserService(eventConsumer: EventConsumer[UserCreatedEvent]) {
    def createUser(name: String): Boolean = eventConsumer.consume(UserCreatedEvent(name, UUID.randomUUID().toString))
  }

  //Of course EventConsumer[Event] can be specified, that will allow to get rid of contravariance in EventConsumer, but also
  //this approach allow us to send absolutely any events from OrderServiceNew, even event that user was created
  //Never repeat this at home !!!
  class OrderServiceNew(eventConsumer: EventConsumer[Event]) {
    //    def createOrder(orderList: List[Int]): Boolean = eventConsumer.consume(OrderCreatedEvent(UUID.randomUUID().toString, orderList))
    def createOrder(orderList: List[Int]): Boolean =
      eventConsumer.consume(UserCreatedEvent(UUID.randomUUID().toString, UUID.randomUUID().toString))
  }

  val eventConsumer = new EventConsumer[Event]()

  val orderService = new OrderService(eventConsumer)
  orderService.createOrder(List(1, 2, 3))

  val userService = new UserService(eventConsumer)
  userService.createUser("John Smith")

  //Restrictions. Contravariant

  //1. Contravariant T can not be used as a field

//  class ContravariantCage[-T](val value: T) //this won't be compiled due to contravariant value in covariant position
  //This is impossible due to the following
  //alligator should not be put into a cage which is intended for a cat
//  val cage: ContravariantCage[Cat] = new ContravariantCage[Animal](new Alligator())

  //2. Contravariant T can not be returned

//  trait AnimalGenerator[-T <: Animal] {
//    def generate(count: Int): List[T]
//  }
//  class AlligatorGenerator extends AnimalGenerator[Alligator] {
//    def generate(count: Int): List[Alligator] = (1 to count).map(_ => new Alligator()).toList
//  }
//  val supergatorGenerator: AnimalGenerator[Supergator] = new AlligatorGenerator()
//  val supergators: List[Supergator] = supergatorGenerator.generate(12)

  //Restrictions. Covariant

  //1. Covariant T can not be accepted as a parameter
//  trait Container[+T] {
//    def add(value: T): Container[T]
//  }
//
//  case class DogContainer(list: List[Dog] = Nil) extends Container[Dog] {
//    override def add(value: Dog): DogContainer = DogContainer(value :: list)
//  }
//  val animalContainer: Container[Animal] = DogContainer()
//  animalContainer.add(new Alligator())

  //2. Covariant T can not be a var field
//  case class Container[+T](var value: T)
//  val container: Container[Animal] = Container[Cat](new Cat())
//  container.value = new Alligator()

  //Conclusions:
  //Method arguments are in contravariant position
  //Method return types are in covariant position

  //Generic framework how to define which variance should be used is the following:
  //if a generic type contains or produces elements of type T it should be +T
  //if a generic type acts on or consumes elements of type T it should be -T
}
