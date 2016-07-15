package columbus.twitter;

import columbus.common.Producer;
import columbus.common.ProducerManager;
import columbus.twitter.HashtagProducer;

import spark.Request;
import spark.Response;

/**
 *
 */
public class Handlers {

    static final String EMPTY = "";
    static final String HASH = "#";
    static final String PARAM_HASH = "hash";
    static final String PARAM_ID = "id";
    static final String TOPIC_PREFIX = "twitter-";

    /**
     * @param req
     * @param res
     * @param manager
     * @reurn
     * @since 0.1.0
     */
    public static String addProducer(final Request req, final Response res, final ProducerManager manager) {
        String hashtag = req.params(PARAM_HASH);
        Producer producer = new HashtagProducer(TOPIC_PREFIX + hashtag, HASH + hashtag);

        res.status(201);
        return manager.startProducer(producer);
    }

    /**
     * @param req
     * @param res
     * @param manager
     * @reurn
     * @since 0.1.0
     */
    public static String deleteProducer(final Request req, final Response res, final ProducerManager manager) {
        String id = req.params(PARAM_ID);
        manager.stopProducerById(id);

        res.status(204);
        return EMPTY;
    }
}
