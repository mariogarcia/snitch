package galileo.twitter

import static ratpack.jackson.Jackson.json

import rx.Observable

import ratpack.handling.Handler
import ratpack.handling.Context

import java.time.Duration
import javax.inject.Inject

class TwitterPollingHandler implements Handler {

  @Inject
  TwitterService service

  public void handle(Context ctx) {
    String topic = ctx.allPathTokens.topic
    println "Getting tweets from topic $topic"

    Observable
    .just(service.getNextTweet("twitter-$topic"))
    .subscribe { map ->
      ctx.render(json(map))
    }
  }
}
