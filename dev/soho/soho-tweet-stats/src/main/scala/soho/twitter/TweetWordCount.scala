package soho.twitter

import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._

import scala.util.parsing.json._

import snitch.common._
import soho.kafka._

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream

/**
 * @since 0.1.0
 */
object TweetWordCount {

  /**
   * @param args
   * @since 0.1.0
   */
  def main(args:Array[String]) {
    val Array(zkQuorum, group, topicName) = args
    val conf = new SparkConf().setAppName("wordCount")
    val client = new KafkaStreamingClient(conf)

    client
      .createStreamProcessor(zkQuorum, group)
      .process(topicName, storeTweets)
  }

  /**
   *
   * @param stream
   * @param sink
   * @since 0.1.0
   */
  def storeTweets(stream: JavaPairReceiverInputDStream[String,String], sink: Broadcast[KafkaSink]): Unit = {

    val lines = stream.map((a:(String,String)) => a._2)
//      .map(tweet => getContentFrom(tweet))
//      .flatMap(word => word.split(" "))
//      .map(word => (word, 1))
//      .reduceByKeyAndWindow(_ + _, _ - _, Minutes(10), Seconds(10), 2)
//      .filter(_._2 >= 10)
//      .filter(tuple => tuple._1.length() >= 4 && tuple._1.contains("@"))
//      .foreachRDD { rdd =>
//        rdd.foreach { tuple =>
//          val (word, count) = tuple
//          sink.value.send(topic = "statistics-" + topicName,
//                          value = "{\"" + word + "\": " + count + "}")
//        }
//    }
  }

  /**
   * @param tweet
   * @return
   */
  def getContentFrom(tweet: String) :String = {
    JSON.globalNumberParser    = { in: String => 0 }
    JSON.perThreadNumberParser = { in: String => 0 }

    return JSON
      .parseFull(tweet)
      .get
      .asInstanceOf[Map[String, Any]] ("text")
      .asInstanceOf[String]
  }
}
