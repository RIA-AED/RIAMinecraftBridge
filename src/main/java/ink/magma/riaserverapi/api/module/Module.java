package ink.magma.riaserverapi.api.module;

import spark.RouteGroup;
import spark.Spark;

public interface Module {
    /**
     * 模块名称
     */
    String name();

    /**
     * 模块路由
     */
    RouteGroup routes();

    /**
     * 注册模块, 包含路由相关的注册
     */
    default void register() {
        Spark.path(name(), routes());
    }
}
