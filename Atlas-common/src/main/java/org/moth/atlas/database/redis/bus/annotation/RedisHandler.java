package org.moth.atlas.database.redis.bus.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RedisHandler {
    String[] value();
}