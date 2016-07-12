package galileo.common.rx

import static ratpack.rx.RxRatpack.observe

import rx.Observable
import rx.functions.Func1

import ratpack.exec.Blocking

/**
 * Class with some rxjava utility functions
 *
 * @since 0.1.0
 */
class Rx {

  private Rx() { }

  /**
   * A {@linkplain java.util.function.Supplier} which may throw an
   * exception.
   *
   * @param <R> the type of results supplied by this supplier
   */
  @FunctionalInterface
  interface CheckedSupplier<R> {

    /**
     * Gets a result.
     *
     * @return a result of type R
     * @throws Throwable if an error occurs
     */
    public R get() throws Throwable
  }

  /**
   * Represents a function that can throw a checked exception
   *
   * @param <A> the input type
   * @param <B> the output type
   * @since 0.1.0
   */
  @FunctionalInterface
  interface CheckedFunction<A,B> {

    /**
     * Transforms an object of type A to type B
     *
     * @param subject of type A
     * @return an object of type B
     * @since 0.1.0
     */
    B call(A subject) throws Throwable
  }

  /**
   * Wraps an exception produced inside an {@link Observable} within
   * the boundaries of a blocking thread.
   *
   * @since 0.1.0
   */
  static class BlockingException extends RuntimeException {
    BlockingException(final Throwable ex) {
      super(ex)
    }
  }

  /**
   * Executes a {@link CheckedSupplier} in the blocking thread pool
   *
   * @param supplier an instance of {@link CheckedSupplier}
   * @since 0.1.0
   */
  @SuppressWarnings(["AvoidCatchingThrowable", "CatchThrowable"])
  static <T> Observable<T> blocking(final CheckedSupplier<T> supplier) {
    return observe(Blocking.get({
                                  try {
                                    return supplier.get()
                                  } catch (final Throwable ex) {
                                    throw new BlockingException(ex)
                                  }
                                }))
  }

  /**
   * Executes a given {@link CheckedFunction} in the blocking thread
   * pool
   *
   * @param action the action you want to execute in the blocking
   * thread pool
   * @return a function that returns an {@link Observable}
   * @since 0.1.0
   */
  static <A,B> Func1<A, Observable<B>> blocking(final CheckedFunction<A,B> action) {
    return { A a -> blocking({ action.call(a) } as CheckedSupplier<B>) } as Func1<A, Observable<B>>
  }

  /**
   * Executes a given {@link Closure} in the blocking thread
   * pool. Useful when using a {@link
   * org.codehaus.groovy.runtime.MethodClosure}
   *
   * @param action the action you want to execute in the blocking
   * thread pool
   * @return a function that returns an {@link Observable}
   * @since 0.1.0
   */
  static <A,B> Func1<A, Observable<B>> blocking(final Closure<B> action) {
    return { A a -> blocking({ action.doCall(a) } as CheckedSupplier<B>) } as Func1<A, Observable<B>>
  }
}
