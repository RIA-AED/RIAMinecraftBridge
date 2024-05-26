package ink.magma.riaserverapi.api.module;

import ink.magma.riaserverapi.api.transformer.JsonTransformer;
import ink.magma.riaserverapi.platform.adopter.AdopterProvider;
import spark.RouteGroup;
import spark.Spark;

public class Player implements Module {
    @Override
    public String name() {
        return "player";
    }

    @Override
    public RouteGroup routes() {
        return () -> {
            Spark.get("/online", (req, res) -> AdopterProvider.get().getPlayerList(), new JsonTransformer());
        };
    }
}
