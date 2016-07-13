package galileo.hello

import rx.Observable

/**
 * Greetings service
 *
 * @since 0.1.0
 */
interface HelloService {

  /**
   * Return a new greetings message
   *
   * @return an observable with a greetings message
   * @since 0.1.0
   */
  Map getNextTweet()

}
