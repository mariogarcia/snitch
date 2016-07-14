package columbus;

import static spark.Spark.post;
import static spark.Spark.port;
import static spark.Spark.delete;

import columbus.common.Producer;
import columbus.common.ProducerManager;
import columbus.common.DefaultProducerManager;
import columbus.twitter.TwitterHashtagProducer;

public class Application {
    static final ProducerManager manager = new DefaultProducerManager();

    public static void main(String args[]) throws Exception {
        port(5050);
        post("/twitter/:hash", (req, res) -> {
                String hashtag = req.params("hash");
                System.out.println("Creating producer for tag: #" + hashtag);

                Producer producer = manager.startProducer(new TwitterHashtagProducer("twitter-" + hashtag,"#" + hashtag));
                res.status(201);

                return producer;
        });

        delete("/twitter", (req, res) -> {
                manager.stop();
                res.status(204);
                return "";
        });
    }
}
