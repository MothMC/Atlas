package org.moth.atlas.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import dev.morphia.query.experimental.updates.UpdateOperators;
import org.bukkit.entity.Player;
import org.moth.atlas.cache.LocalServerCache;
import org.moth.atlas.database.mongo.MongoPlayer;
import org.moth.atlas.database.mongo.MongoRank;
import org.moth.atlas.player.AtlasPlayer;
import org.moth.atlas.rank.Rank;

import java.util.HashSet;

@CommandAlias("rank")
@CommandPermission("rank.use")
public class RankCommand extends BaseCommand {

    //todo propagate through redis cache

    @Subcommand("create")
    public void onCreateRank(Player player, String name, String displayName, Boolean isDefaultRank) {
        MongoRank mongoRank = new MongoRank();

        Rank rank = mongoRank.searchRankByName(name);

        if(rank != null) {
            player.sendMessage("Rank already exists.");
            return;
        }

        rank = Rank.builder()
                        .rankName(name)
                        .rankFormat(displayName)
                        .defaultRank(isDefaultRank)
                        .build();

        LocalServerCache.getRankList().add(rank);
        mongoRank.saveRank(rank);
    }

    @Subcommand("permission add")
    public void onPermissionAssign(Player player, String rankName, String permission) {
        MongoRank mongoRank = new MongoRank();

        Rank rank = mongoRank.searchRankByName(rankName);

        if(rank == null) {
            player.sendMessage("Rank not exists.");
            return;
        }

        if(rank.getRankPermissions() == null) {
            rank.setRankPermissions(new HashSet<>());
        }

        rank.getRankPermissions().add(permission);
        mongoRank.updateField(rankName, "rankPermissions", rank.getRankPermissions());
    }

    @Subcommand("permission remove")
    public void onPermissionRemove(Player player, String rankName, String permission) {
        MongoRank mongoRank = new MongoRank();

        Rank rank = mongoRank.searchRankByName(rankName);

        if(rank == null) {
            player.sendMessage("Rank not exists.");
            return;
        }

        //todo checks
        rank.getRankPermissions().remove(permission);
        mongoRank.updateField(rankName, "rankPermissions", rank.getRankPermissions());
    }

    @Subcommand("inherit")
    public void onRankInherit(Player player, String inheritTo, String inheritFrom) {
        MongoRank mongoRank = new MongoRank();

        Rank inheritToRank = mongoRank.searchRankByName(inheritTo);
        Rank inheritFromRank = mongoRank.searchRankByName(inheritFrom);

        if(inheritToRank == null || inheritFromRank == null) {
            player.sendMessage("Rank does not exist.");
            return;
        }

        inheritFromRank.getRankPermissions().forEach(permission -> inheritToRank.getRankPermissions().add(permission));
        mongoRank.updateField(inheritTo, "rankPermissions", inheritToRank.getRankPermissions());
    }

    @Subcommand("deinherit")
    public void onRankDeInherit(Player player, String inheritTo, String inheritFrom) {
        MongoRank mongoRank = new MongoRank();

        Rank inheritToRank = mongoRank.searchRankByName(inheritTo);
        Rank inheritFromRank = mongoRank.searchRankByName(inheritFrom);

        if(inheritToRank == null || inheritFromRank == null) {
            player.sendMessage("Rank does not exist.");
            return;
        }

        inheritFromRank.getRankPermissions().forEach(permission -> inheritToRank.getRankPermissions().remove(permission));
        mongoRank.updateField(inheritToRank.getRankName(), "rankPermissions", inheritToRank.getRankPermissions());
    }

    @Subcommand("get")
    public void onRetrieveRank(Player player, String name) {
        MongoPlayer.builder()
                .name(name)
                .build()
                .searchPlayerByName().thenAccept(queryResult -> {
                    AtlasPlayer atlasPlayer = queryResult.first();

                    if(atlasPlayer == null) {
                        player.sendMessage("Error, not player found.");
                        return;
                    }

                    player.sendMessage("Rank: " + atlasPlayer.getRank().getRankFormat());
                });
    }

    @Subcommand("set")
    public void onRetrieveRank(Player player, String playerName, String rankName) {
        Rank rank = LocalServerCache.getRankByName(rankName);

        if(rank == null)
            return;

        MongoPlayer mongoPlayer = MongoPlayer.builder()
                .name(playerName)
                .build();

        mongoPlayer.searchPlayerByName().thenAccept(queryResult -> {
            AtlasPlayer atlasPlayer = queryResult.first();

            if(atlasPlayer == null) {
                player.sendMessage("Error, player not found.");
                return;
            }

            mongoPlayer.updateField(queryResult,"rank", rank);
        });
    }

}
