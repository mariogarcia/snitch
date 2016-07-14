package soho.twitter

import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._

object TweetWordCount {
  def main(args:Array[String]) {
    val Array(zkQuorum, group, topics, numThreads) = args
    val conf = new SparkConf().setAppName("wordCount")

    val ssc = new StreamingContext(conf, Seconds(2))
    val topic = List("twitter-FelizJueves").map((_,2)).toMap
    val lines = KafkaUtils.createStream(ssc, zkQuorum, group, topic).map(_._2)

    ssc.checkpoint("/home/dev/tweets/")

    val words = lines.flatMap(_.split(" "))
    val counts = words
      .map(word => (word, 1))
      .reduceByKeyAndWindow(_ + _,
                            _ - _,
                            Minutes(10),
                            Seconds(2),
                            2)

    counts.saveAsTextFiles("/home/dev/tweets/tweet-words")

    ssc.start()
    ssc.awaitTermination()
  }
}
