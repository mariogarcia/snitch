package galileo.twitter

import com.google.inject.AbstractModule
import com.google.inject.Scopes

/**
 * Module to configure greetings services
 *
 * @since 0.1.0
 */
class TwitterModule  extends AbstractModule {

  @Override
  void configure() {
    bind(TwitterService).to(TwitterServiceImpl)
    bind(TwitterSSEHandler).in(Scopes.SINGLETON)
    bind(TwitterPollingHandler).in(Scopes.SINGLETON)
  }
}
