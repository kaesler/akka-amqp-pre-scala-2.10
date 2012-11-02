import sbt._

object dependencies {

  def AmqpClient = "com.rabbitmq" % "amqp-client" % "2.8.7"
  def AkkaActor = "com.typesafe.akka" % "akka-actor" % "2.0.3"
  val scalatest = "org.scalatest" % "scalatest_2.9.0" % "2.0.M4"
  def JUnit = "junit" % "junit" % "4.10" % "test"
  def AkkaTestKit = "com.typesafe.akka" % "akka-testkit" % "2.0.3" % "test"
  def Mockito = "org.mockito" % "mockito-all" % "1.9.0" % "test"
}
