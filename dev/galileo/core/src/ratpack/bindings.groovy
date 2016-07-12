import static ratpack.groovy.Groovy.ratpack

import galileo.hello.HelloModule

/**
 *    _    _         _ _
 *   | |__(_)_ _  __| (_)_ _  __ _ ___
 *   | '_ \ | ' \/ _` | | ' \/ _` (_-<
 *   |_.__/_|_||_\__,_|_|_||_\__, /__/
 *                           |___/
 *
 * This configuration file ONLY contains module bindings, such as
 * Guice modules or any other ratpack module
 */
ratpack {
  bindings {
    module HelloModule
  }
}
