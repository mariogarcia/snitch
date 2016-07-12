package galileo.hello

import groovy.transform.ToString

/**
 * Relevant data coming from the user's request. This data is likely
 * to be validated
 *
 * @since 0.1.0
 */
@ToString
class HelloRequest {

  /**
   * Name of the person we want to say hi
   *
   * @since 0.1.0
   */
  String name
}
