package soho.twitter

import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._

import scala.util.parsing.json._

object TweetWordCount {
  def main(args:Array[String]) {
    val Array(zkQuorum, group, topics, numThreads) = args

    val conf = new SparkConf().setAppName("wordCount")
    val ssc = new StreamingContext(conf, Seconds(2))
    val topic = List("twitter-" + topics).map((_,2)).toMap
    val lines = KafkaUtils.createStream(ssc, zkQuorum, group, topic).map(_._2)

    ssc.checkpoint("/home/dev/tweets/")

    val wordCount = lines
      .map(tweet => getContentFrom(tweet))
      .flatMap(word => word.split(" "))
      .map(word => (word, 1))
      .reduceByKeyAndWindow(_ + _, _ - _, Minutes(10), Seconds(2), 2)

    wordCount.saveAsTextFiles("/home/dev/tweets/tweet-words")

    ssc.start()
    ssc.awaitTermination()
  }

  def getContentFrom(tweet: String) :String = {
    return JSON
      .parseFull(tweet)
      .get
      .asInstanceOf[Map[String, Any]] ("text")
      .asInstanceOf[String]
  }
}
