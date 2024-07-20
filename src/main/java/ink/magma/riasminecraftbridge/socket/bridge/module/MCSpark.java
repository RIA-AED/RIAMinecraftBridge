package ink.magma.riasminecraftbridge.socket.bridge.module;

import ink.magma.riasminecraftbridge.platform.adopter.AdopterProvider;
import ink.magma.riasminecraftbridge.platform.adopter.PlatformAdopter;
import ink.magma.riasminecraftbridge.socket.bridge.module.define.Module;
import me.lucko.spark.api.SparkProvider;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.misc.DoubleAverageInfo;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import me.lucko.spark.api.statistic.types.GenericStatistic;

import java.util.HashMap;
import java.util.Map;

/**
 * 本模块实现了通过 API 获取服务器性能数据。
 */
public class MCSpark implements Module {
    private me.lucko.spark.api.Spark luckoSpark;

    public MCSpark() {
        PlatformAdopter platform = AdopterProvider.get();

        // 尝试获取 SparkAPI
        try {
            luckoSpark = SparkProvider.get();
        } catch (IllegalStateException e) {
            platform.logError("SparkAPI 获取失败!");
        }
    }

    public Map<String, Object> getTPS() {
        Map<String, Object> response = new HashMap<>();
        DoubleStatistic<StatisticWindow.TicksPerSecond> tps = luckoSpark.tps();

        if (tps == null) {
            response.put("success", false);
            response.put("message", "SparkAPI 获取失败!");
            return response;
        }

        response.put("5s", tps.poll(StatisticWindow.TicksPerSecond.SECONDS_5));
        response.put("10s", tps.poll(StatisticWindow.TicksPerSecond.SECONDS_10));
        response.put("1m", tps.poll(StatisticWindow.TicksPerSecond.MINUTES_1));
        response.put("5m", tps.poll(StatisticWindow.TicksPerSecond.MINUTES_5));
        response.put("15m", tps.poll(StatisticWindow.TicksPerSecond.MINUTES_15));
        return response;
    }

    public Map<String, Object> getMSPT() {
        Map<String, Object> response = new HashMap<>();
        GenericStatistic<DoubleAverageInfo, StatisticWindow.MillisPerTick> mspt = luckoSpark.mspt();

        if (mspt == null) {
            response.put("success", false);
            response.put("message", "SparkAPI 获取失败!");
            return response;
        }

        response.put("10s", mspt.poll(StatisticWindow.MillisPerTick.SECONDS_10).mean());
        response.put("1m", mspt.poll(StatisticWindow.MillisPerTick.MINUTES_1).mean());
        return response;
    }

    public Map<String, Object> getCPUUsage() {
        Map<String, Object> response = new HashMap<>();
        response.put("system_10s", luckoSpark.cpuSystem().poll(StatisticWindow.CpuUsage.SECONDS_10));
        response.put("system_1m", luckoSpark.cpuSystem().poll(StatisticWindow.CpuUsage.MINUTES_1));
        response.put("system_15m", luckoSpark.cpuSystem().poll(StatisticWindow.CpuUsage.MINUTES_15));
        response.put("process_10s", luckoSpark.cpuProcess().poll(StatisticWindow.CpuUsage.SECONDS_10));
        response.put("process_1m", luckoSpark.cpuProcess().poll(StatisticWindow.CpuUsage.MINUTES_1));
        response.put("process_15m", luckoSpark.cpuProcess().poll(StatisticWindow.CpuUsage.MINUTES_15));
        return response;
    }
}