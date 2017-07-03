package com.sefon.flume;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by sponge on 2017/7/3.
 */
public class WirelessInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory
            .getLogger(WirelessInterceptor.class);

    static final String STATION_ID = "station";
    static final String SCAN_OVER_TIME= "scantime";


    public void initialize() {

    }

    public Event intercept(Event event) {
        Map<String, String> headers = event.getHeaders();
        byte[] body = event.getBody();

        String id = WirelessEventParese.wirelessId(body);
        String scanOverTime = WirelessEventParese.wirelessTime(body);
        if ( ! headers.containsKey(STATION_ID)) {
            headers.put(STATION_ID, id);
        }
        if ( ! headers.containsKey(SCAN_OVER_TIME)) {
            headers.put(SCAN_OVER_TIME, scanOverTime);
        }
        logger.debug(String.format("WirelessInterceptor station=%s scan over time ", id, scanOverTime));

        return event;
    }

    public List<Event> intercept(List<Event> list) {
        for (Event event : list) {
            intercept(event);
        }
        return list;
    }

    public void close() {

    }

    public static class Builder implements Interceptor.Builder {

        @Override
        public Interceptor build() {
            logger.info("Creating WireLessInterceptor");
            return  new WirelessInterceptor();
        }

        @Override
        public void configure(Context context) {
        }
    }
}
