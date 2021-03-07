package org.moth.atlas.database.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

import lombok.Getter;

public class MongoManager {

    @Getter
    private MongoClient mongoClient;

    @Getter
    private static Datastore atlasDatastore = null;

    public MongoManager() {

        try {
            this.mongoClient = MongoClients.create("mongodb://localhost:27017/");

            Datastore datastore = Morphia.createDatastore(mongoClient, "atlas");
            datastore.getMapper().mapPackage("org.moth.atlas.player");
            datastore.getMapper().mapPackage("org.moth.atlas.rank");
            datastore.ensureIndexes();

            atlasDatastore = datastore;
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }
}
