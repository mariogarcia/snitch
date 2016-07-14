package galileo.twitter

import rx.Observable

/**
 * Greetings service
 *
 * @since 0.1.0
 */
interface TwitterService {

  /**
   * Return a new greetings message
   *
   * @param topic
   * @return an observable with a greetings message
   * @since 0.1.0
   */
  Map getNextTweet(String topic)

}
