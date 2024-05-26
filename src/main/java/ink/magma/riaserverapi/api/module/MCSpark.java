package ink.magma.riaserverapi.api.module;

import ink.magma.riaserverapi.api.route.NotSupportRoute;
import ink.magma.riaserverapi.api.transformer.JsonTransformer;
import me.lucko.spark.api.SparkProvider;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.misc.DoubleAverageInfo;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import me.lucko.spark.api.statistic.types.GenericStatistic;
import spark.RouteGroup;
import spark.Spark;

import java.util.Map;

public class MCSpark implements Module {
    private me.lucko.spark.api.Spark luckoSpark;

    public String name() {
        return "spark";
    }

    public RouteGroup routes() {
        return () -> {
            Spark.get("/tps", (req, res) -> {
                DoubleStatistic<StatisticWindow.TicksPerSecond> tps = luckoSpark.tps();

                if (tps == null) {
                    res.status(500);
                    return Map.of("message", "SparkAPI 获取失败!");
                }

                return Map.of(
                        "5s", tps.poll(StatisticWindow.TicksPerSecond.SECONDS_5),
                        "10s", tps.poll(StatisticWindow.TicksPerSecond.SECONDS_10),
                        "1m", tps.poll(StatisticWindow.TicksPerSecond.MINUTES_1),
                        "5m", tps.poll(StatisticWindow.TicksPerSecond.MINUTES_5),
                        "15m", tps.poll(StatisticWindow.TicksPerSecond.MINUTES_15)
                );
            }, new JsonTransformer());

            Spark.get("/mspt", (req, res) -> {
                GenericStatistic<DoubleAverageInfo, StatisticWindow.MillisPerTick> mspt = luckoSpark.mspt();
                if (mspt == null) {
                    res.status(500);
                    return Map.of("message", "SparkAPI 获取失败!");
                }

                return Map.of(
                        "10s", mspt.poll(StatisticWindow.MillisPerTick.SECONDS_10).mean(),
                        "1m", mspt.poll(StatisticWindow.MillisPerTick.MINUTES_1).mean()
                );
            }, new JsonTransformer());

            Spark.get("/cpu", (req, res) -> Map.of(
                    "system_10s", luckoSpark.cpuSystem().poll(StatisticWindow.CpuUsage.SECONDS_10),
                    "system_1m", luckoSpark.cpuSystem().poll(StatisticWindow.CpuUsage.MINUTES_1),
                    "system_15m", luckoSpark.cpuSystem().poll(StatisticWindow.CpuUsage.MINUTES_15),
                    "process_10s", luckoSpark.cpuProcess().poll(StatisticWindow.CpuUsage.SECONDS_10),
                    "process_1m", luckoSpark.cpuProcess().poll(StatisticWindow.CpuUsage.MINUTES_1),
                    "process_15m", luckoSpark.cpuProcess().poll(StatisticWindow.CpuUsage.MINUTES_15)
            ), new JsonTransformer());
        };
    }

    public void register() {
        // 尝试获取 SparkAPI
        try {
            luckoSpark = SparkProvider.get();
            Spark.path(name(), routes());
        } catch (IllegalStateException e) {
            Spark.path(name(), NotSupportRoute.routes());
        }
    }
}
