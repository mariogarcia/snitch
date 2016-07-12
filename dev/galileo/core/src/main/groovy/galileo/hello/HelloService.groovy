package galileo.hello

import rx.Observable

/**
 * Greetings service
 *
 * @since 0.1.0
 */
interface HelloService {

  /**
   * It should return a customized greetings message for the name of
   * the person passed as parameter
   *
   * @param name the name of the person we want to greet
   * @return an observable with a greetings message
   * @since 0.1.0
   */
  Observable<String> getMessage(String name)

}
