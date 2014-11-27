package testCompota
//package $name$

import ohnosequences.awstools.s3.ObjectAddress
import ohnosequences.compota.aws._
import ohnosequences.compota.local.LocalCompota
import ohnosequences.compota.monoid.stringMonoid
import ohnosequences.compota.{Instructions, Compota, Nispero, MapInstructions}
import ohnosequences.compota.logging.Logger
import ohnosequences.compota.queues.local.{BlockingMonoidQueue, BlockingQueue}

import scala.util.Success

object q1 extends BlockingQueue[Int]("q1", 10)
object q2 extends BlockingMonoidQueue[String]("q2", 10, stringMonoid)

object instr extends MapInstructions[Int, String] {
  override def apply(logger: Logger, context: Int, input: Int) = Success((1000 / input).toString)

  override def prepare(logger: Logger) = Success(0)

  override type Context = Int
}

object configuration extends AwsCompotaConfigurationAux(
	generated.metadata.testCompota)
object nisperoConfiguration extends AwsNisperoConfigurationAux("printer", configuration)

object nispero1 extends AwsNispero("printer", q1, q2, instr, nisperoConfiguration)



object TestCompota extends AwsCompota(List(nispero1), List(q2), configuration) {
  override def addTasks(): Unit = {
    q1.getWriter.foreach{_.write("1", List(123, 0))}
  }
}
