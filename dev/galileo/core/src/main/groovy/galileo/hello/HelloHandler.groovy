package galileo.hello

import static galileo.common.handler.Utils.render
import static ratpack.sse.ServerSentEvents.serverSentEvents
import static ratpack.stream.Streams.periodically

import ratpack.sse.Event
import ratpack.sse.ServerSentEvents
import org.reactivestreams.Publisher

import java.time.Duration
import javax.inject.Inject

import ratpack.handling.Handler
import ratpack.handling.Context

class HelloHandler implements Handler {

  @Inject
  HelloService service

  @Override
  public void handle(final Context ctx) {
    Publisher<String> stream =
      periodically(ctx,
                   Duration.ofMillis(1000),
                   { Integer i -> i < 5 ? i.toString() : null } )

    ServerSentEvents events =
      serverSentEvents(stream,
                       { Event e -> e.id({ x -> "$x".toString()}).event("counter").data { i -> "event " + i} })

    service
    .getMessage()
    .map { message -> events }
    .subscribe(ctx.&render)
  }
}
