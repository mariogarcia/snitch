import static ratpack.groovy.Groovy.ratpack

import static galileo.common.handler.ValidatorHandler.createValidator
import static galileo.common.handler.BindingHandler.createBinding

import galileo.hello.HelloHandler

/**
 *             _                        _
 *    _  _ _ _| |  _ __  __ _ _ __ _ __(_)_ _  __ _ ___
 *   | || | '_| | | '  \/ _` | '_ \ '_ \ | ' \/ _` (_-<
 *    \_,_|_| |_| |_|_|_\__,_| .__/ .__/_|_||_\__, /__/
 *                           |_|  |_|         |___/
 *
 * This configuration file ONLY contains handler mappings. It should
 * be used like an url mapping file.
 */
ratpack {
  handlers {

    prefix("greetings") {
      get(HelloHandler)
    }

  }
}
