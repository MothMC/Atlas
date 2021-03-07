package org.moth.atlas.events.join;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.moth.atlas.cache.LocalServerCache;
import org.moth.atlas.database.mongo.MongoPlayer;
import org.moth.atlas.database.redis.RedisManager;
import org.moth.atlas.events.AtlasListener;
import org.moth.atlas.player.AtlasPlayer;

import java.util.UUID;

public class AtlasJoinEvent implements AtlasListener {

    @EventHandler
    public void handleAtlasJoin(AsyncPlayerPreLoginEvent event) {
        acceptCache(RedisManager.getJedis().get("atlas-" + event.getUniqueId().toString()), event.getUniqueId(),
                    event.getName());
    }

    private void acceptCache(String param, UUID uuid, String name) {
        if(param == null) {
            System.out.println("init mongo player");
            MongoPlayer.builder()
                        .name(name)
                        .UUID(uuid)
                        .build()
                        .initPlayer();
        } else
            LocalServerCache.addPlayer(RedisManager.getGson().fromJson(param, AtlasPlayer.class));
    }

}
