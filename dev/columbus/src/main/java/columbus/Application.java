package columbus;

import static spark.Spark.post;
import static spark.Spark.port;
import static spark.Spark.delete;

import spark.Request;
import spark.Response;

import columbus.common.Producer;
import columbus.common.ProducerManager;
import columbus.common.DefaultProducerManager;
import columbus.twitter.TwitterHashtagProducer;

public class Application {
    static final ProducerManager manager = new DefaultProducerManager();

    public static void main(String args[]) throws Exception {
        port(6060);

        post("/twitter/:hash", Application::addProducer);
        delete("/twitter/:id", Application::deleteProducer);
    }

    public static String addProducer(final Request req, final Response res) {
        String hashtag = req.params("hash");
        System.out.println("Creating producer for tag: #" + hashtag);

        Producer producer = manager.startProducer(new TwitterHashtagProducer("twitter-" + hashtag,"#" + hashtag));
        res.status(201);

        return producer.getId();
    }

    public static String deleteProducer(final Request req, final Response res) {
        String id = req.params("id");
        System.out.println("Stopping producer with id: " + id);

        manager.stopProducerById(id);
        res.status(204);

        return "";
    }
}
