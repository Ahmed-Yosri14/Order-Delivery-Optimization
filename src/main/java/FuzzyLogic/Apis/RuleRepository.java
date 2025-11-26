package FuzzyLogic.Apis;

import com.google.gson.Gson;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

public class RuleRepository {

    private final MongoCollection<Document> collection;

    public RuleRepository() {
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = client.getDatabase("fuzzydb");
        collection = db.getCollection("rules");
    }

    public RuleDocument save(RuleDocument rule) {
        Gson gson = new Gson();
        Document doc = Document.parse(gson.toJson(rule));
        collection.insertOne(doc);
        rule.id = doc.getObjectId("_id").toHexString();
        return rule;
    }

    public List<RuleDocument> findAll() {
        List<RuleDocument> out = new ArrayList<>();
        Gson gson = new Gson();
        for (Document doc : collection.find()) {
            RuleDocument rule = gson.fromJson(doc.toJson(), RuleDocument.class);
            rule.id = doc.getObjectId("_id").toHexString();
            out.add(rule);
        }
        return out;
    }

    public RuleDocument update(String id, RuleDocument doc) {
        Gson gson = new Gson();

        Document newDoc = Document.parse(gson.toJson(doc));

        collection.replaceOne(
                new Document("_id", new ObjectId(id)),
                newDoc
        );

        doc.id = id;
        return doc;
    }


    public void delete(String id) {
        collection.deleteOne(new Document("_id", new ObjectId(id)));
    }

    public void disable(String id) {
        collection.updateOne(
                new Document("_id", new ObjectId(id)),
                new Document("$set", new Document("enabled", false))
        );
    }

    public void enable(String id) {
        collection.updateOne(
                new Document("_id", new ObjectId(id)),
                new Document("$set", new Document("enabled", true))
        );
    }

    public List<RuleDocument> findByEnabled(boolean enabled) {
        List<RuleDocument> out = new ArrayList<>();
        Gson gson = new Gson();
        for (Document doc : collection.find(new Document("enabled", enabled))) {
            RuleDocument rule = gson.fromJson(doc.toJson(), RuleDocument.class);
            rule.id = doc.getObjectId("_id").toHexString();
            out.add(rule);
        }
        return out;
    }

    public void hardDelete(String id) {
        collection.deleteOne(new Document("_id", new ObjectId(id)));
    }
}
