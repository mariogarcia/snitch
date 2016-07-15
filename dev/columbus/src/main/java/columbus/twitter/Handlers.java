package columbus.twitter;

import columbus.common.Producer;
import columbus.common.ProducerManager;
import columbus.twitter.HashtagProducer;

import spark.Request;
import spark.Response;

import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.common.collect.ImmutableMap;

/**
 *
 */
public class Handlers {

    static final String EMPTY = "";
    static final String HASH = "#";
    static final String PARAM_HASH = "hash";
    static final String PARAM_ID = "id";
    static final String TOPIC_PREFIX = "twitter-";
    static final Map<String,String> EMPTY_MAP = ImmutableMap.of();

    /**
     * @param req
     * @param res
     * @param manager
     * @reurn
     * @since 0.1.0
     */
    public static Map<String,String> addProducer(final Request req, final Response res, final ProducerManager manager) {
        res.status(201);

        return Optional
            .ofNullable(req.params(PARAM_HASH))
            .map(Handlers::createProducerFromHashTag)
            .map(manager::startProducer)
            .map(Handlers::createMapFromId)
            .orElse(EMPTY_MAP);
    }

    private static Producer createProducerFromHashTag(String hashtag) {
        return new HashtagProducer(TOPIC_PREFIX + hashtag, HASH + hashtag);
    }

    private static Map<String,String> createMapFromId(String id) {
        return ImmutableMap.of(PARAM_ID, id);
    }

    /**
     * @param req
     * @param res
     * @param manager
     * @reurn
     * @since 0.1.0
     */
    public static String deleteProducer(final Request req, final Response res, final ProducerManager manager) {
        res.status(204);

        Optional
            .ofNullable(req.params(PARAM_ID))
            .ifPresent(manager::stopProducerById);

        return EMPTY;
    }

    /**
     * @param body
     * @return
     * @since 0.1.0
     */
    public static String toJSON(Object body) {
        return new Gson().toJson(body);
    }
}
