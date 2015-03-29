package com.example;

import com.google.common.base.Charsets;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static com.example.TagInterceptor.Constants.*;

/**
 * Created by sponge on 15-3-29.
 */
public class TagInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory
            .getLogger(TagInterceptor.class);

    private final boolean preserveExisting;
    private final String key;
    private final Charset charset;

    /**
     *
     */
    private TagInterceptor(boolean preserveExisting, String key, Charset charset) {
        this.preserveExisting = preserveExisting;
        this.key = key;
        this.charset = charset;
    }

    @Override
    public void initialize() {
        // no-op
    }

    /**
     * Modifies events in-place.
     */
    @Override
    public Event intercept(Event event) {
        Map<String, String> headers = event.getHeaders();

        if (preserveExisting && headers.containsKey(key)) {
            return event;
        }

        String msg = new String(event.getBody(), charset);
        String ret = msg.substring(0,1);

        logger.debug(String.format("TagInterceptor ret=%s",ret));

        headers.put(key, ret);
        return event;
    }

    /**
     * Delegates to {@link #intercept(Event)} in a loop.
     * @param events
     * @return
     */
    @Override
    public List<Event> intercept(List<Event> events) {
        for (Event event : events) {
            intercept(event);
        }
        return events;
    }

    @Override
    public void close() {
        // no-op
    }

    /**
     * Builder which builds new instance of the StaticInterceptor.
     */
    public static class Builder implements Interceptor.Builder {

        private boolean preserveExisting;
        private String key;
        private Charset charset = Charsets.UTF_8;
        @Override
        public void configure(Context context) {
            preserveExisting = context.getBoolean(PRESERVE, PRESERVE_DEFAULT);
            key = context.getString(KEY, KEY_DEFAULT);
            // May throw IllegalArgumentException for unsupported charsets.
            charset = Charset.forName(context.getString(CHARSET_KEY, CHARSET_DEFAULT));

        }

        @Override
        public Interceptor build() {
            logger.info(String.format(
                    "Creating TagInterceptor: preserveExisting=%s,key=%s,charset=%s",
                    preserveExisting, key, charset));
            return new TagInterceptor(preserveExisting, key, charset);
        }


    }

    public static class Constants {

        public static final String KEY = "key";
        public static final String KEY_DEFAULT = "key";

        public static final String PRESERVE = "preserveExisting";
        public static final boolean PRESERVE_DEFAULT = true;

        public static final String CHARSET_KEY = "charset";
        public static final String CHARSET_DEFAULT = "UTF-8";
    }
}
