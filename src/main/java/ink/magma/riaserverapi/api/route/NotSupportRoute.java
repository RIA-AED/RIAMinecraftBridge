package ink.magma.riaserverapi.api.route;

import ink.magma.riaserverapi.api.route.response.MessageOnlyResponse;
import ink.magma.riaserverapi.api.transformer.JsonTransformer;
import spark.Route;
import spark.RouteGroup;
import spark.Spark;

public class NotSupportRoute {

    public static final Route ErrorResponse = (req, res) -> {
        res.status(404);
        return new JsonTransformer().render(
                new MessageOnlyResponse("不存在的接口，这可能是因为服务端相关功能故障或不支持。可能的解决方法：1.检查您调用的服务端是否正确；2.检查您的接口路径是否正确；3.联系此服务器技术运维人员排查插件是否故障。")
        );
    };

    public static RouteGroup routes() {
        return () -> {
            Spark.get("*", ErrorResponse);
            Spark.post("*", ErrorResponse);
            Spark.put("*", ErrorResponse);
            Spark.delete("*", ErrorResponse);
            Spark.head("*", ErrorResponse);
            Spark.trace("*", ErrorResponse);
            Spark.connect("*", ErrorResponse);
            Spark.options("*", ErrorResponse);
            Spark.patch("*", ErrorResponse);
        };
    }
}
