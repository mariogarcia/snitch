package soho.kafka

import org.apache.spark._
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka.KafkaUtils

import org.apache.spark.streaming.api.java.JavaStreamingContext
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream

import snitch.common._

/**
 *
 * @constructor
 * @param zkQuorum
 * @param groupId
 * @param topics
 */
class KafkaStreamingClient(zkQuorum: String, groupId: String, topics: java.util.Map[String,Integer]) {
  val sparkContext = new SparkContext(new SparkConf().setAppName(""))
  val sparkStreamingContext = new StreamingContext(sparkContext, Seconds(2))

  /**
   * @return
   */
  def createInputStream: JavaPairReceiverInputDStream[String,String] = {
    val jssc = new JavaStreamingContext(sparkStreamingContext)

    return KafkaUtils.createStream(jssc, zkQuorum, groupId, topics)
  }

  /**
   * @return
   */
  def createSink(): Broadcast[KafkaSink] = {
    val kafkaProps: java.util.Map[String,Object] = Props.loadAsMap("/kafka.properties")
    val kafkaSink = sparkContext.broadcast(KafkaSink(kafkaProps))

    return kafkaSink
  }
}
