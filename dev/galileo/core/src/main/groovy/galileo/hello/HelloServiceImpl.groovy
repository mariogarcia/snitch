package galileo.hello

import rx.Observable

/**
 * Default implementation of the {@link HelloService} interface
 *
 * @since 0.1.0
 */
class HelloServiceImpl implements HelloService {

  /**
   * {@inheritDoc}
   */
  @Override
  Observable<String> getMessage(String name) {
    return Observable.just("Hello $name")
  }
}
