package galileo.hello

import static galileo.common.handler.Utils.render

import javax.inject.Inject

import ratpack.handling.Handler
import ratpack.handling.Context

class HelloHandler implements Handler {

  @Inject
  HelloService service

  @Override
  public void handle(final Context ctx) {
    HelloRequest request = ctx.get(HelloRequest)

    service
    .getMessage(request.name)
    .subscribe(ctx.&render)
  }
}
