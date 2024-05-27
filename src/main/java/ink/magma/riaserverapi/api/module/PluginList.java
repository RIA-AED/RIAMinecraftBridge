package ink.magma.riaserverapi.api.module;

import ink.magma.riaserverapi.api.transformer.JsonTransformer;
import ink.magma.riaserverapi.platform.adopter.AdopterProvider;
import spark.RouteGroup;
import spark.Spark;

public class PluginList implements Module {
    @Override
    public String name() {
        return "plugin";
    }

    @Override
    public RouteGroup routes() {
        return () -> {
            Spark.get("/list", (req, res) -> {
                return AdopterProvider.get().getPlugins();
            }, new JsonTransformer());
        };
    }
}
