package galileo.common.handler

import static ratpack.jackson.Jackson.json
import static java.util.stream.Collectors.toList

import ratpack.registry.Registry
import ratpack.handling.Handler
import ratpack.handling.Context

import helios.Validator
import helios.ValidatorError

/**
 * Base class to create validator {@link Handler} instances using
 * {@link Validator}
 *
 * @param <T> The type of the validated object
 * @since 0.1.0
 */
class ValidatorHandler<T> implements Handler {

  private final Class<T> clazz
  private final Validator<T> validator

  /**
   * Constructor
   *
   * @param clazz The type of the object we are validating
   * @param validator the validator used to validate
   * @since 0.1.0
   */
  public ValidatorHandler(final Class<T> clazz, final Validator<T> validator) {
    this.clazz = clazz
    this.validator = validator
  }

  /**
   * {@inheritDoc}
   */
  @Override
  void handle(final Context ctx) {
    final T ACTION = ctx.get(clazz)
    final List<ValidatorError> ERRORS = validator.validate(ACTION)

    if (ERRORS.isEmpty()) {
      ctx.next(Registry.single(clazz, ACTION))
    } else {
      respond(ERRORS, ctx)
    }
  }

  private static void respond(final List<ValidatorError> errors, final Context context) {
    final List<Error> ERRORS = errors
    .stream()
    .map { ValidatorError err -> [property: err.property, error: err.keyI18n] }
    .collect(toList())

    context.getResponse().status(400)
    context.render(json(ERRORS))
  }

  /**
   * Creates a new instance of {@link ValidatorHandler} for a given
   * class and a given {@link Validator}
   *
   * @param clazz The type of the object we are validating
   * @param validator the validator used to validate
   * @return an instance of {@link ValidatorHandler}
   * @since 0.1.0
   */
  static <T> ValidatorHandler<T> createValidator(final Class<T> clazz, final Validator<T> validator) {
    return new ValidatorHandler<T>(clazz, validator)
  }
}
