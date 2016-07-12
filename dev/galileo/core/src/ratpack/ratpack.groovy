import static ratpack.groovy.Groovy.ratpack

import java.nio.file.Paths

/**
 *   ___      _ _ _
 *  / __|__ _| (_) |___ ___
 * | (_ / _` | | | / -_) _ \
 *  \___\__,_|_|_|_\___\___/
 *
 *
 * This is the main configuration entry point for Galileo.
 */
ratpack {

  /**
   * HANDLERS AND BINDINGS
   * =====================
   *
   * Handlers and bindings should be configured in their respective
   * configuration files
   */
  include Paths.get('handlers.groovy')
  include Paths.get('bindings.groovy')
}
