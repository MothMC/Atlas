package org.moth.atlas.rank;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity("ranks")
@Getter
@Setter
@Builder
public class Rank {
    @Id
    private String rankName;

    private Set<String> rankPermissions = new HashSet<>();
    private String rankFormat;
    private boolean defaultRank;
}
