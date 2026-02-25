package org.itmo.aspect;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Aspect
@Component
public class CacheStatisticsAspect {

    private static final Logger logger = LoggerFactory.getLogger(CacheStatisticsAspect.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final AtomicBoolean loggingEnabled = new AtomicBoolean(true);

    public void enableLogging() {
        loggingEnabled.set(true);
        logger.info("✓ Логирование статистики L2 Cache включено");
    }

    public void disableLogging() {
        loggingEnabled.set(false);
        logger.info("✗ Логирование статистики L2 Cache отключено");
    }

    public boolean isLoggingEnabled() {
        return loggingEnabled.get();
    }

    @Around("execution(* org.itmo.repository..*(..))")
    public Object logCacheStatistics(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!loggingEnabled.get()) {
            return joinPoint.proceed();
        }

        Statistics statsBefore = getStatistics();
        long hitsBefore = statsBefore != null ? statsBefore.getSecondLevelCacheHitCount() : 0;
        long missesBefore = statsBefore != null ? statsBefore.getSecondLevelCacheMissCount() : 0;
        long putsBefore = statsBefore != null ? statsBefore.getSecondLevelCachePutCount() : 0;

        Object result = joinPoint.proceed();

        Statistics statsAfter = getStatistics();
        if (statsAfter != null) {
            long hitsAfter = statsAfter.getSecondLevelCacheHitCount();
            long missesAfter = statsAfter.getSecondLevelCacheMissCount();
            long putsAfter = statsAfter.getSecondLevelCachePutCount();

            long hitsDelta = hitsAfter - hitsBefore;
            long missesDelta = missesAfter - missesBefore;
            long putsDelta = putsAfter - putsBefore;

            if (hitsDelta > 0 || missesDelta > 0 || putsDelta > 0) {
                String methodName = joinPoint.getSignature().toShortString();
                logger.info("L2 Cache | {} | Hits: +{} Miss: +{} Puts: +{} | Total Hits: {} Miss: {} Puts: {}",
                        methodName, hitsDelta, missesDelta, putsDelta,
                        hitsAfter, missesAfter, putsAfter);
            }
        }

        return result;
    }

    private Statistics getStatistics() {
        try {
            Session session = entityManager.unwrap(Session.class);
            return session.getSessionFactory().getStatistics();
        } catch (Exception e) {
            logger.warn("Не удалось получить статистику Hibernate: {}", e.getMessage());
            return null;
        }
    }


    // для CacheManagementController
    public CacheStats getCacheStatistics() {
        Statistics stats = getStatistics();
        if (stats == null) {
            return new CacheStats(0, 0, 0, 0, 0.0, loggingEnabled.get());
        }

        long hits = stats.getSecondLevelCacheHitCount();
        long misses = stats.getSecondLevelCacheMissCount();
        long puts = stats.getSecondLevelCachePutCount();
        long total = hits + misses;
        double hitRatio = total > 0 ? (double) hits / total * 100 : 0.0;

        return new CacheStats(hits, misses, puts, total, hitRatio, loggingEnabled.get());
    }

    public void clearStatistics() {
        Statistics stats = getStatistics();
        if (stats != null) {
            stats.clear();
            logger.info("Статистика L2 Cache сброшена");
        }
    }

    public static class CacheStats {
        public final long hits;
        public final long misses;
        public final long puts;
        public final long totalRequests;
        public final double hitRatio;
        public final boolean loggingEnabled;

        public CacheStats(long hits, long misses, long puts, long totalRequests, double hitRatio, boolean loggingEnabled) {
            this.hits = hits;
            this.misses = misses;
            this.puts = puts;
            this.totalRequests = totalRequests;
            this.hitRatio = hitRatio;
            this.loggingEnabled = loggingEnabled;
        }
    }
}
