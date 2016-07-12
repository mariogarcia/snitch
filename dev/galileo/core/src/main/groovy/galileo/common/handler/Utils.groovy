package galileo.common.handler

import static ratpack.jackson.Jackson.json

import java.util.function.Consumer
import java.util.function.Predicate

import rx.functions.Action1
import rx.functions.Func1
import rx.Observable

import ratpack.handling.Context

/**
 * Common utilities for Ratpack handlers
 *
 * @since 0.1.0
 */
final class Utils {

  private Utils() { }

  /**
   * Represents an action that handlers exceptions of a given type
   *
   * @since 0.1.0
   */
  static class ErrorHandler<T extends Throwable> {

    final Class<T> type
    final Action1<T> action

    /**
     * Error handler constructor
     *
     * @param type the of exception
     * @param action handler of exceptions of the given type
     * @since 0.1.0
     */
    ErrorHandler(final Class<T> type, final Action1<T> action) {
      this.type = type
      this.action = action
    }

    /**
     * When invoking this method the error will be handled
     *
     * @param th exception to be handled
     * @since 0.1.0
     */
    void handle(T th) {
      action.call(th)
    }
  }

  /**
   * Renders the inner value of a {@link Optional} type
   *
   * @param ctx Ratpack context
   * @return an rxjava {@link Action1} returning an {@link Optional}
   * of type T
   * @since 0.1.0
   */
  static <T> Action1<Optional<T>> render(final Context ctx) {
    return { final Optional<T> subject ->
      ctx.getResponse().status(200)

      if (subject.isPresent()) {
        ctx.render(json(subject.get()))
      } else {
        ctx.render("")
      }
    } as Action1<Optional<T>>
  }

  /**
   * Registers a given action to handle a given type of error
   *
   * @param errorType Type of error to handle
   * @param errorHandler instance or lambda of type {@link Action1}
   * to handle the error
   * @return an instance of {@link ErrorHandler}
   * @since 0.1.0
   */
  static <T extends Throwable> ErrorHandler<T> handleError(Class<T> errorType, Action1<T> errorHandler) {
    return new Utils.ErrorHandler(errorType, errorHandler)
  }

  /**
   * Registers a variable number of {@link ErrorHandler}
   * instances. If there is no handler register for a given type of
   * error, then there is a default handler that produces an {@link
   * Observable} error.
   *
   * @param errors a variable number of {@link ErrorHandler}
   * @return a {@link Func1} that dispatches errors to handlers
   * capable of handling that specific type of errors
   * @see Utils#handleError
   */
  @SafeVarargs
  @SuppressWarnings("unchecked")
  static <T> Func1<Throwable, ? extends Observable<T>> handleErrors(final ErrorHandler... errors) {
    return { Throwable th ->
      Optional<ErrorHandler> handler =
        Arrays
        .stream(errors)
        .filter({ ErrorHandler h -> h.type.equals(th.getClass()) } as Predicate<ErrorHandler>)
        .findFirst()

        if (handler.isPresent()) {
          handler.ifPresent({ ErrorHandler h -> h.handle(th) } as Consumer<ErrorHandler>)
          return Observable.empty()
        } else {
          return Observable.error(th)
        }
    } as Func1<Throwable, ? extends Observable<T>>
  }
}
