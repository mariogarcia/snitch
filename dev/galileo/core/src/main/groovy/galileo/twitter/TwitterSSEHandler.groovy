package galileo.twitter

import static galileo.common.handler.Utils.render
import static ratpack.sse.ServerSentEvents.serverSentEvents
import static ratpack.stream.Streams.periodically

import rx.Observable
import ratpack.sse.Event
import ratpack.sse.ServerSentEvents
import org.reactivestreams.Publisher

import java.time.Duration
import javax.inject.Inject

import ratpack.handling.Handler
import ratpack.handling.Context

class TwitterSSEHandler implements Handler {

  static final Integer FREQUENCY_MS = 5000

  @Inject
  TwitterService service

  @Override
  public void handle(final Context ctx) {
    ServerSentEvents sse = createSSE(ctx)

    Observable
    .just(sse)
    .subscribe(ctx.&render)
  }

  ServerSentEvents createSSE(final Context ctx) {
    Publisher<Map> stream = periodically(ctx, Duration.ofMillis(FREQUENCY_MS), { i -> service.getNextTweet("twitter-PrayForNice") })
    ServerSentEvents events = serverSentEvents(stream, this.&createEvent)

    return events
  }

  Event createEvent(Event e) {
    return e
    .id({ Map event -> event?.id?.toString()})
    .event("counter")
    .data { Map event -> event?.data?.toString()}
  }
}
