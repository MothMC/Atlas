package org.moth.atlas;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.moth.atlas.command.RankCommand;
import org.moth.atlas.database.mongo.MongoManager;
import org.moth.atlas.database.mongo.MongoRank;
import org.moth.atlas.database.redis.RedisManager;
import org.moth.atlas.events.AtlasEventHandler;
import org.moth.atlas.events.join.AtlasJoinEvent;

public class Atlas extends JavaPlugin {

    @Getter
    private static Atlas instance;

    @Getter
    private static MongoManager mongoManager;

    @Getter
    private static RedisManager redisManager;

    @Override
    public void onEnable() {
        instance = this;
        mongoManager = new MongoManager();
        redisManager = new RedisManager();

        MongoRank mongoRank = new MongoRank();
        mongoRank.initRanks();

        AtlasEventHandler.registerEvent(new AtlasJoinEvent());

        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new RankCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
