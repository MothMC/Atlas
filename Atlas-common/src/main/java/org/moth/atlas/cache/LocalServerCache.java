package org.moth.atlas.cache;

import lombok.Getter;
import lombok.Setter;
import org.moth.atlas.player.AtlasPlayer;
import org.moth.atlas.rank.Rank;

import java.util.*;

public class LocalServerCache {

    @Getter
    private static Set<AtlasPlayer> cachedPlayers = new HashSet<>();

    @Getter
    @Setter
    private static List<Rank> rankList = new ArrayList<>();

    public static void addPlayer(AtlasPlayer atlasPlayer) {
        cachedPlayers.add(atlasPlayer);
    }

    public static void removePlayer(AtlasPlayer atlasPlayer) {
        cachedPlayers.remove(atlasPlayer);
    }

    public static AtlasPlayer getPlayer(UUID uuid) {
        return cachedPlayers.stream().filter(atlasPlayer -> atlasPlayer.getUuid().equals(uuid.toString())).findAny().orElse(null);
    }

    public static Rank defaultAssignedRank() {
        return rankList.stream().filter(Rank::isDefaultRank).findFirst().orElse(null);
    }

    public static boolean doesRankExist(String name) {
        return rankList.stream().anyMatch(rank -> rank.getRankName().equalsIgnoreCase(name));
    }

    public static Rank getRankByName(String name) {
        return rankList.stream().filter(rank -> rank.getRankName().equalsIgnoreCase(name)).findAny().orElse(null);
    }




}
