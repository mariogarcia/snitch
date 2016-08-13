package soho.kafka

import org.apache.spark._
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka.KafkaUtils

import org.apache.spark.streaming.api.java.JavaStreamingContext
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream

import collection.JavaConversions._

import snitch.common._

/**
 *
 * @constructor
 * @param zkQuorum
 * @param groupId
 * @param topics
 */
class KafkaStreamingClient(conf: SparkConf) {
  val sparkContext = new SparkContext(confq)


  /**
   * @return
   */
  def createStreamProcessor(zkQuorum: String, groupId: String): StreamProcessor = {
    return new StreamProcessor(sparkContext, zkQuorum, groupId)
  }
}

class StreamProcessor(sparkContext: SparkContext, zkQuorum: String, groupId: String) {

  def process(topic: String, fn: (JavaPairReceiverInputDStream[String,String],Broadcast[KafkaSink]) => Unit): Unit = {
    val sparkStreamingContext = new StreamingContext(sparkContext, Seconds(2))
    val jssc                  = new JavaStreamingContext(sparkStreamingContext)
    val kafkaProps: java.util.Map[String,Object] = Props.loadAsMap("/kafka.properties")
    val kafkaSink             = sparkContext.broadcast(KafkaSink(kafkaProps))
    val jmap                  = mapAsJavaMap(Map(topic -> 2)).asInstanceOf[java.util.Map[String,Integer]]
    val stream: JavaPairReceiverInputDStream[String,String] = KafkaUtils.createStream(jssc, "zkQuorum", "groupId", jmap)

    fn(stream, kafkaSink)

    jssc.start()
    jssc.awaitTermination()
  }
}
