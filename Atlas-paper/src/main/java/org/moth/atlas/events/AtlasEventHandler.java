package org.moth.atlas.events;

import org.moth.atlas.Atlas;

public class AtlasEventHandler {

    public static void registerEvent(AtlasListener listener) {
        Atlas.getInstance().getServer().getPluginManager().registerEvents(listener, Atlas.getInstance());
    }

}
