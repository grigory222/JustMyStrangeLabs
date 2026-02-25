package org.itmo.controller;

import org.itmo.aspect.CacheStatisticsAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST контроллер для управления L2 Cache и его статистикой
 */
@RestController
@RequestMapping("/api/cache")
public class CacheManagementController {

    @Autowired
    private CacheStatisticsAspect cacheStatisticsAspect;

    /**
     * Получить статистику L2 Cache
     * GET /api/cache/statistics
     */
    @GetMapping("/statistics")
    public Map<String, Object> getCacheStatistics() {
        CacheStatisticsAspect.CacheStats stats = cacheStatisticsAspect.getCacheStatistics();
        
        Map<String, Object> response = new HashMap<>();
        response.put("hits", stats.hits);
        response.put("misses", stats.misses);
        response.put("puts", stats.puts);
        response.put("totalRequests", stats.totalRequests);
        response.put("hitRatio", String.format("%.2f%%", stats.hitRatio));
        response.put("loggingEnabled", stats.loggingEnabled);
        
        return response;
    }

    /**
     * Включить логирование статистики кэша
     * POST /api/cache/logging/enable
     */
    @PostMapping("/logging/enable")
    public Map<String, Object> enableLogging() {
        cacheStatisticsAspect.enableLogging();
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "enabled");
        response.put("message", "Логирование статистики L2 Cache включено");
        
        return response;
    }

    /**
     * Отключить логирование статистики кэша
     * POST /api/cache/logging/disable
     */
    @PostMapping("/logging/disable")
    public Map<String, Object> disableLogging() {
        cacheStatisticsAspect.disableLogging();
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "disabled");
        response.put("message", "Логирование статистики L2 Cache отключено");
        
        return response;
    }

    /**
     * Проверить статус логирования
     * GET /api/cache/logging/status
     */
    @GetMapping("/logging/status")
    public Map<String, Object> getLoggingStatus() {
        boolean enabled = cacheStatisticsAspect.isLoggingEnabled();
        
        Map<String, Object> response = new HashMap<>();
        response.put("loggingEnabled", enabled);
        response.put("status", enabled ? "enabled" : "disabled");
        
        return response;
    }

    /**
     * Сбросить статистику кэша
     * POST /api/cache/statistics/clear
     */
    @PostMapping("/statistics/clear")
    public Map<String, Object> clearStatistics() {
        cacheStatisticsAspect.clearStatistics();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Статистика кэша сброшена");
        
        return response;
    }
}
