package galileo.hello

import com.google.inject.AbstractModule
import com.google.inject.Scopes

/**
 * Module to configure greetings services
 *
 * @since 0.1.0
 */
class HelloModule  extends AbstractModule {

  @Override
  void configure() {
    bind(HelloService).to(HelloServiceImpl)
    bind(HelloHandler).in(Scopes.SINGLETON)
  }
}
