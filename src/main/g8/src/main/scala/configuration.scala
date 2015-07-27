package $name$

import ohnosequences.awstools.s3.ObjectAddress
import ohnosequences.compota.MapInstructions
import ohnosequences.compota.aws._
import ohnosequences.compota.aws.deployment.AnyMetadata
import ohnosequences.compota.aws.queues.{S3InMemoryReducible, DynamoDBQueue}
import ohnosequences.compota.environment.Env
import ohnosequences.compota.monoid.intMonoid
import ohnosequences.compota.serialization.{stringSerializer, intSerializer}

import scala.concurrent.duration._
import scala.util.{Success, Try}

import com.amazonaws.auth.AWSCredentialsProvider

object testInputQueue extends DynamoDBQueue[String](
  name = "testInputQueue",
  serializer = stringSerializer
)

object testOutputQueue extends DynamoDBQueue[Int](
  name = "testOutputQueue",
  serializer = intSerializer
) with S3InMemoryReducible {
  override val destination: Option[ObjectAddress] = testCompotaConfiguration.resultsDestination(testOutputQueue)
  override val monoid = intMonoid
}

object testInstructions extends MapInstructions[String, Int] {
  override type Context = Unit
  override def prepare(env: Env): Try[Context] = Success(())
  override def apply(env: Env, context: Context, input: String): Try[Int] = {
    Success(input.length)
  }
}

object testCompotaConfiguration extends AwsCompotaConfiguration {
  override def localAwsCredentialsProvider: AWSCredentialsProvider = $credentialsProvider$
  override def metadata: AnyMetadata = ohnosequences.compota.generated.metadata.metadata
  override def notificationEmail: String = "$email$"
  override def keyName: String = "$sshKeyPair$"
  override def timeout: Duration = Duration(1, HOURS)
}

object testNisperoConfiguration extends AwsNisperoConfiguration {
  override def name: String = "test"
  override def compotaConfiguration: AwsCompotaConfiguration = testCompotaConfiguration
}

object testNispero extends AwsNispero (
  inputQueue = testInputQueue,
  outputQueue = testOutputQueue,
  instructions = testInstructions,
  configuration = testNisperoConfiguration
)


object testCompota extends AwsCompota[Unit] (
  nisperos = List(testNispero),
  configuration = testCompotaConfiguration
) {

  override def prepareUnDeployActions(env: AwsEnvironment): Try[Unit] = {
    Success(())
  }

  override def addTasks(env: AwsEnvironment): Try[Unit] = {
    testInputQueue.create(env.createDynamoDBContext).flatMap { queueOp =>
      queueOp.writer.flatMap { writer =>
        writer.writeMessages(".", List("test", "longTest", "tst"))
      }
    }
  }
}