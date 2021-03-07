package org.moth.atlas.database.redis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import org.moth.atlas.database.redis.bus.RedisBus;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Setter
public class RedisManager {

    private final JedisPool jedisPool;

    @Getter
    private static Gson gson = new GsonBuilder().create();

    private RedisBus redisBus;

    @Getter
    private static Jedis jedis;

    public RedisManager() {
        this.jedisPool = new JedisPool(new JedisPoolConfig(), "localhost", 6379);;

        try(Jedis resJedis = jedisPool.getResource()) {
            this.redisBus = new RedisBus(jedisPool);
            jedis = resJedis;
        } catch(JedisException e) {
            Logger.getLogger("redis.clients").log(Level.SEVERE, "Unable to find a valid redis bus.");
        }
    }

    public void clear() {
        redisBus.getListeners().clear();
        redisBus.getRegisteredChannels().clear();
    }

}