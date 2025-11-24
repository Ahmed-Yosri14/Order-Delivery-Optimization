package FuzzyLogic.Apis;

import static spark.Spark.*;
import com.google.gson.Gson;

import java.util.Map;

public class ApiServer {

    public static void main(String[] args) {

        port(8080);

        Gson gson = new Gson();
        RuleParser parser = new RuleParser();
        RuleRepository repo = new RuleRepository();

        post("/rules", (req, res) -> {
            try {
                RuleDocument doc = parser.parse(req.body());
                return gson.toJson(repo.save(doc));
            } catch (Exception e) {
                res.status(400);
                return gson.toJson(Map.of("error", e.getMessage()));
            }
        });
        put("/rules/:id", (req, res) -> {
            try {
                String id = req.params("id");

                RuleDocument updatedDoc = parser.parse(req.body());
                updatedDoc.id = id;

                RuleDocument result = repo.update(id, updatedDoc);

                return gson.toJson(result);

            } catch (Exception e) {
                res.status(400);
                return gson.toJson(Map.of("error", e.getMessage()));
            }
        });

        get("/rules", (req, res) -> gson.toJson(repo.findAll()));

        delete("/rules/:id", (req, res) -> {
            repo.delete(req.params("id"));
            return "Deleted";
        });

        System.out.println("Spark Java server running: http://localhost:8080");
    }
}
