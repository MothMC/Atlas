package org.moth.atlas.database.mongo;

import dev.morphia.query.Query;
import dev.morphia.query.experimental.filters.Filters;
import dev.morphia.query.experimental.updates.UpdateOperators;
import lombok.Builder;
import org.moth.atlas.cache.LocalServerCache;
import org.moth.atlas.database.redis.RedisManager;
import org.moth.atlas.player.AtlasPlayer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Builder
public class MongoPlayer {

    private UUID UUID;
    private String name;

    public void initPlayer() {
        searchPlayer().thenAcceptAsync(atlasQuery -> {

            AtlasPlayer atlasPlayer = atlasQuery.first();

            if(atlasPlayer != null) {

                if(!atlasPlayer.getUsernameHistory().containsKey(name)) {
                    atlasPlayer.getUsernameHistory().put(name, System.currentTimeMillis());
                    updateField(atlasQuery, "usernameHistory", atlasPlayer.getUsernameHistory());
                }

                LocalServerCache.addPlayer(atlasPlayer);
                RedisManager.getJedis().setex("atlas-" + UUID.toString(), 300, RedisManager.getGson().toJson(atlasPlayer));
            } else {
                atlasPlayer = new AtlasPlayer();
                atlasPlayer.setUuid(UUID.toString());
                atlasPlayer.setName(name);
                atlasPlayer.setRank(LocalServerCache.defaultAssignedRank());
                atlasPlayer.getUsernameHistory().put(name, System.currentTimeMillis());
                atlasPlayer.setCredits(0);

                atlasPlayer.setJoinDate(System.currentTimeMillis());

                System.out.println("saved");
                insertPlayer(atlasPlayer);
            }
        });
    }

    public CompletableFuture<Query<AtlasPlayer>> searchPlayer() {
        return CompletableFuture.supplyAsync(() -> MongoManager.getAtlasDatastore().find(AtlasPlayer.class)
                .filter(Filters.eq("uuid", UUID.toString())));
    }

    public CompletableFuture<Query<AtlasPlayer>> searchPlayerByName() {
        return CompletableFuture.supplyAsync(() -> MongoManager.getAtlasDatastore().find(AtlasPlayer.class)
                .filter(Filters.eq("name", name)));
    }

    public void insertPlayer(AtlasPlayer player) {
        CompletableFuture.runAsync(() ->
            MongoManager.getAtlasDatastore().save(player)
        );
    }

    public void updateField(Query<AtlasPlayer> queryResult, String field, Object value) {
        CompletableFuture.runAsync(() -> queryResult.update(UpdateOperators.set(field, value)).execute());
    }

}
