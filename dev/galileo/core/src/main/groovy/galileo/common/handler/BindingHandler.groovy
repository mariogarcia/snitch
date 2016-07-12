package galileo.common.handler

import static ratpack.jackson.Jackson.json

import ratpack.func.Action
import ratpack.registry.Registry
import ratpack.handling.Handler
import ratpack.handling.Context

/**
 * Base for creating binding handlers. An instance of a {@link
 * BindingHandler} will parse JSON from the request and create an
 * instance of an object.
 *
 * @param <T> the type of the object we want to convert the json to
 * @since 0.1.0
 */
class BindingHandler<T> implements Handler {

  private final Class<T> clazz

  /**
   * Constructor receiving the type of object we will get once the
   * JSON coming from the request has been parsed successfully
   *
   * @param clazz the type of the object we want to get from the
   * parsed json
   * @since 0.1.0
   */
  BindingHandler(final Class<T> clazz) {
    this.clazz = clazz
  }

  /**
   * {@inheritDoc}
   */
  @Override
  void handle(final Context ctx) {
    ctx.parse(clazz).then(addToNext(ctx))
  }

  private static <T> Action<? super T> addToNext(final Context ctx) {
    return { T action -> ctx.next(Registry.single(action)) } as Action<? super T>
  }

  /**
   * Creates an instance of {@link BindingHandler}
   *
   * @param clazz the type of class you want an instance from
   * @return an instance of {@link BindingHandler}
   * @since 0.1.0
   */
  static <T> BindingHandler<T> createBinding(final Class<T> clazz) {
    return new BindingHandler<T>(clazz)
  }
}
