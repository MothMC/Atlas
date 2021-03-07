package org.moth.atlas.database.mongo;

import dev.morphia.query.Query;
import dev.morphia.query.experimental.filters.Filters;
import dev.morphia.query.experimental.updates.UpdateOperators;
import org.moth.atlas.cache.LocalServerCache;
import org.moth.atlas.rank.Rank;

import java.util.concurrent.CompletableFuture;

public class MongoRank {

    public void initRanks() {
        CompletableFuture.runAsync(() -> LocalServerCache.setRankList(MongoManager.getAtlasDatastore().find(Rank.class).find().toList()));
    }

    public Rank searchRankByName(String name) {
        return LocalServerCache.getRankList().stream().filter(rank -> rank.getRankName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public void saveRank(Rank rank) {
        CompletableFuture.runAsync(() ->
                MongoManager.getAtlasDatastore().save(rank)
        );
    }

    public void updateField(String rankName, String field, Object value) {
        CompletableFuture.runAsync(() -> {
            Query<Rank> query = MongoManager.getAtlasDatastore().find(Rank.class).filter(Filters.eq("rankName", rankName));
            query.update(UpdateOperators.set(field, value)).execute();
        });
    }

}
