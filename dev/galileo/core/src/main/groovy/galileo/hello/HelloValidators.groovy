package galileo.hello

import static helios.Helios.validate
import static helios.Validators.required

import helios.ValidatorError

/**
 * Class containing all validators related to the {@link HelloModule}
 *
 * @since 0.1.0
 */
class HelloValidators {

  /**
   * Validates a given {@link HelloRequest}
   *
   * @param request the {@link HelloRequest} we want to validate
   * @return a possible list of {@link ValidatorError}
   * @since 0.1.0
   */
  static List<ValidatorError> checkRequest(final HelloRequest request) {
    return validate("request",
                    request,
                    { HelloRequest req -> validate("name", req.name, required()) })
  }
}
