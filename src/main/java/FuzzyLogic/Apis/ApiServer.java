package FuzzyLogic.Apis;

import static spark.Spark.*;
import com.google.gson.Gson;
import java.util.Map;

public class ApiServer {

    static class RuleRequest {
        String rule;  // rule text in natural language format
        Double weight; // optional: override weight (0.0-1.0)
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

                // If weight is provided in request body, override parsed weight
                if (body.weight != null) {
                    if (body.weight < 0.0 || body.weight > 1.0) {
                        res.status(400);
                        return Map.of("error", "Weight must be between 0.0 and 1.0");
                    }
                    doc.weight = body.weight;
                }

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

                // If weight is provided in request body, override parsed weight
                if (body.weight != null) {
                    if (body.weight < 0.0 || body.weight > 1.0) {
                        res.status(400);
                        return Map.of("error", "Weight must be between 0.0 and 1.0");
                    }
                    updated.weight = body.weight;
                }

                return repo.update(id, updated);
            } catch (Exception e) {
                res.status(400);
                return Map.of("error", e.getMessage());
            }
        }, gson::toJson);

        // GET ALL (optionally filter by enabled status)
        get("/rules", "application/json",
                (req, res) -> {
                    String enabledParam = req.queryParams("enabled");
                    if (enabledParam != null) {
                        boolean enabled = Boolean.parseBoolean(enabledParam);
                        return repo.findByEnabled(enabled);
                    }
                    return repo.findAll();
                },
                gson::toJson
        );

        // GET ACTIVE RULES ONLY (enabled=true)
        get("/rules/active", "application/json",
                (req, res) -> repo.findByEnabled(true),
                gson::toJson
        );

        // ENABLE RULE
        patch("/rules/:id/enable", "application/json",
                (req, res) -> {
                    String id = req.params("id");
                    repo.enable(id);
                    return Map.of("status", "enabled", "message", "Rule has been enabled");
                },
                gson::toJson
        );

        // DISABLE RULE
        patch("/rules/:id/disable", "application/json",
                (req, res) -> {
                    String id = req.params("id");
                    repo.disable(id);
                    return Map.of("status", "disabled", "message", "Rule has been disabled");
                },
                gson::toJson
        );


        System.out.println("Spark Java server running: http://localhost:8080");
    }
}
