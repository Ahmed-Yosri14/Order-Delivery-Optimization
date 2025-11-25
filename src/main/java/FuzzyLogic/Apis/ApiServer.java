package FuzzyLogic.Apis;

import static spark.Spark.*;
import com.google.gson.Gson;
import java.util.Map;

public class ApiServer {

    static class RuleRequest {
        String rule;  // only field for POST/PUT
    }

    public static void main(String[] args) {

        port(8080);

        Gson gson = new Gson();
        RuleParser parser = new RuleParser();
        RuleRepository repo = new RuleRepository();


        // CREATE RULE
        post("/rules", "application/json", (req, res) -> {
            try {
                RuleRequest body = gson.fromJson(req.body(), RuleRequest.class);
                RuleDocument doc = parser.parse(body.rule);
                return repo.save(doc);
            } catch (Exception e) {
                res.status(400);
                return Map.of("error", e.getMessage());
            }
        }, gson::toJson);

        // UPDATE
        put("/rules/:id", "application/json", (req, res) -> {
            try {
                String id = req.params("id");
                RuleRequest body = gson.fromJson(req.body(), RuleRequest.class);

                RuleDocument updated = parser.parse(body.rule);
                updated.id = id;

                return repo.update(id, updated);
            } catch (Exception e) {
                res.status(400);
                return Map.of("error", e.getMessage());
            }
        }, gson::toJson);

        // GET ALL
        get("/rules", "application/json",
                (req, res) -> repo.findAll(),
                gson::toJson
        );

        // DELETE
        delete("/rules/:id", "application/json",
                (req, res) -> {
                    repo.delete(req.params("id"));
                    return Map.of("status", "deleted");
                },
                gson::toJson
        );

        System.out.println("Spark Java server running: http://localhost:8080");
    }
}
