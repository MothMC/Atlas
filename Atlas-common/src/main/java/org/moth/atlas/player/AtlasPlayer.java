package org.moth.atlas.player;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import lombok.Getter;
import lombok.Setter;
import org.moth.atlas.rank.Rank;

import java.util.HashMap;
import java.util.UUID;

@Entity("player")
@Getter
@Setter
public class AtlasPlayer {

    @Id
    private String uuid;

    private String name;

    private HashMap<String, Long> usernameHistory = new HashMap<>();

    @Property
    private Rank rank;

    private int credits;
    private long joinDate;

    public UUID fromString() {
        return UUID.fromString(uuid);
    }

}
