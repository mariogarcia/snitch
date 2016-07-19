package soho.twitter

//import collection.mutable._

import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._

import scala.util.parsing.json._

import snitch.common._
import soho.kafka._

object TweetWordCount {
  def main(args:Array[String]) {
    val Array(zkQuorum, group, topicName, numThreads) = args

    val conf = new SparkConf().setAppName("wordCount")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(2))
    val topic = List(("twitter-" + topicName, 2)).toMap
    val lines = KafkaUtils.createStream(ssc, zkQuorum, group, topic).map(_._2)

    val kafkaProps: java.util.Map[String,Object] = Props.loadAsMap("/kafka.properties")
    val kafkaSink = sc.broadcast(KafkaSink(kafkaProps))

    ssc.checkpoint("~/tweets")

    val wordCount = lines
      .map(tweet => getContentFrom(tweet))
      .flatMap(word => word.split(" "))
      .map(word => (word, 1))
      .reduceByKeyAndWindow(_ + _, _ - _, Minutes(10), Seconds(10), 2)
      .filter(_._2 >= 10)
      .filter(tuple => tuple._1.length() >= 4 && tuple._1.contains("@"))

    wordCount.foreachRDD { rdd =>
      rdd.foreach { tuple =>
        val (word, count) = tuple
        kafkaSink.value.send(topic = "statistics-" + topicName,
                             value = "{\"" + word + "\": " + count + "}")
      }
    }

    ssc.start()
    ssc.awaitTermination()
  }

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
