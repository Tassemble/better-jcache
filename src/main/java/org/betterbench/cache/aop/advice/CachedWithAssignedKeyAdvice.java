package org.betterbench.cache.aop.advice;


import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.betterbench.cache.aop.AnnotationDataBuilder;
import org.betterbench.cache.aop.AssignedCache;
import org.betterbench.cache.aop.CacheConstant;
import org.betterbench.cache.aop.dto.AnnotationDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author CHQ
 * @date Oct 18, 2013
 * @since 1.0
 */

@Aspect
public class CachedWithAssignedKeyAdvice {

    static final Logger LOG = LoggerFactory.getLogger(CachedWithAssignedKeyAdvice.class);

    protected MemcachedClient memcachedClient;


    public CachedWithAssignedKeyAdvice(MemcachedClient memcachedClient) {
        this.memcachedClient = memcachedClient;
    }


    @Pointcut("@annotation(com.netease.edu.cache.aop.AssignedCache)")
    public void cacheWithKey() {

    }

    public MemcachedClient getMemcachedClient(boolean share) {
        return memcachedClient;
    }


    public MemcachedClient getMemcachedClient() {
        return memcachedClient;
    }

    public void setMemcachedClient(MemcachedClient memcachedClient) {
        this.memcachedClient = memcachedClient;
    }

    @Around("cacheWithKey()")
    protected Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        if (!isCacheEnable()) {
            LOG.info("assign key cache is disabled");
            return joinPoint.proceed();
        }
        AnnotationDataDto data = null;
        boolean shareCacheSpace = false;

        try {
            data = AnnotationDataBuilder.builderAnnotationData(joinPoint, AssignedCache.class);
            shareCacheSpace = data.isShareCacheSpace();

            if (LOG.isDebugEnabled()) {
                LOG.debug("key:" + data.getAssignedKey());
                LOG.debug("expiration:" + data.getExpiration());
            }

            if (StringUtils.isBlank(data.getAssignedKey())) {
                throw new Exception("no key find");
            }

            String versionKey = data.getNamespace() + CacheConstant.NAMESPACE_VERSION_KEY;
            Object versionResult = getMemcachedClient(shareCacheSpace).get(versionKey);

            Long version = null;
            if (versionResult == null) {
                OperationFuture<Boolean> future = getMemcachedClient(shareCacheSpace).add(versionKey,
                        CacheConstant.MAX_KEY_CACHE_TIME,
                        CacheConstant.INIT_VERSION_NUM);
                Boolean result = future.get();
                if (result == false) {
                    throw new Exception("初始化namespace失败");
                }
                version = Long.valueOf(CacheConstant.INIT_VERSION_NUM);
            } else {
                version = Long.valueOf(versionResult.toString());
            }

            StringBuilder sb = new StringBuilder(data.getNamespace()).append(CacheConstant.SEPARATOR).append(version);
            sb.append(CacheConstant.SEPARATOR).append(data.getAssignedKey());

            data.setKey(sb.toString());
            Object o = getMemcachedClient(shareCacheSpace).get(data.getKey());
            if (o != null) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("using aop cache, key:" + data.getKey());
                }
                return o;
            }
        } catch (Throwable ex) {
            LOG.warn(String.format("Get Cache on method %s aborted due to an error.", joinPoint.toShortString()), ex);
            return joinPoint.proceed();
        }

        Object result = joinPoint.proceed();

        if (result == null) {
            return null;
        }
        try {
            getMemcachedClient(shareCacheSpace).set(data.getKey(), data.getExpiration(), result);
        } catch (Throwable ex) {
            LOG.warn(String.format("Caching on method %s aborted due to an error.", joinPoint.toShortString()), ex);
            return result;
        }

        return result;
    }

    public boolean isCacheEnable() {
        return true;
    }

}
