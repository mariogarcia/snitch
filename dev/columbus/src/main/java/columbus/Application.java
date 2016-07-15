package columbus;

import static spark.Spark.post;
import static spark.Spark.port;
import static spark.Spark.delete;

import static columbus.twitter.Handlers.addProducer;
import static columbus.twitter.Handlers.deleteProducer;

import columbus.common.ProducerManager;
import columbus.common.DefaultProducerManager;

/**
 * @since 0.1.0
 */
public class Application {

    private static final ProducerManager manager = new DefaultProducerManager();

    /**
     * @since 0.1.0
     */
    public static void main(String args[]) throws Exception {
        port(6060);

        post("/twitter/:hash", (req, res) -> addProducer(req, res, manager));
        delete("/twitter/:id", (req, res) -> deleteProducer(req, res, manager));
    }
}
