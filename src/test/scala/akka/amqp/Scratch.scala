package akka.amqp

import java.util.concurrent.CountDownLatch
import akka.actor.{ ActorSystem, Actor, Props }
import akka.serialization.SerializationExtension

object Scratch extends App {

  val system = ActorSystem("amqp")
  val connection = new DurableConnection(ConnectionProperties(system))

  val nrOfMessages = 2000
  val latch = new CountDownLatch(nrOfMessages)

  val serialization = SerializationExtension(system)

  val deliveryHandler = connection.connectionProperties.system.actorOf(Props(new Actor {
    protected def receive = {
      case delivery @ Delivery(payload, routingKey, deliveryTag, isRedeliver, properties, sender) ⇒
        serialization.deserialize(payload, classOf[String]) match {
        case Left(t) => println("Error: deserialization failed")
        case Right(ref) => println(ref.asInstanceOf[String])
      }
      delivery.acknowledge()
      latch.countDown()
    }
  }))
  val consumer = connection.newConsumer(ActiveQueue("test"), deliveryHandler, false)
  consumer.awaitStart()

  val publisher = connection.newStashingPublisher(DefaultExchange)

  for (i ← 1 to nrOfMessages) {
    val msg = "Message[%s]".format(i)
    val buf = msg.getBytes
    publisher.publish(Message(msg, "test"))
  }

  latch.await()
  connection.dispose()
}