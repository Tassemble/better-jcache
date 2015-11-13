package org.betterbench.cache.aop.advice;


import net.spy.memcached.MemcachedClient;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.betterbench.cache.aop.AnnotationDataBuilder;
import org.betterbench.cache.aop.CacheConstant;
import org.betterbench.cache.aop.InvalidateAssignedCache;
import org.betterbench.cache.aop.dto.AnnotationDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * @author CHQ
 * @date Oct 18, 2013
 * @since 1.0
 */
@Aspect
public class InvalidateCacheAfterUpdateAdvice  {

    static final Logger       LOG = LoggerFactory.getLogger(InvalidateCacheAfterUpdateAdvice.class);

    protected MemcachedClient memcachedClient;


    public InvalidateCacheAfterUpdateAdvice(MemcachedClient memcachedClient) {
        this.memcachedClient = memcachedClient;
    }


    private MemcachedClient getMemcachedClient(boolean shareCacheSpace) {
        return memcachedClient;
    }

    @Pointcut("@annotation(org.betterbench.cache.aop.InvalidateAssignedCache)")
    public void invalidateCacheWithKey() {

    }

    @Around("invalidateCacheWithKey()")
    protected Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!isCacheEnable()) {
            return joinPoint.proceed();
        }

        try {

            AnnotationDataDto data = AnnotationDataBuilder.builderAnnotationData(joinPoint,
                    InvalidateAssignedCache.class);
            boolean shareCacheSpace = data.isShareCacheSpace();

            if (LOG.isDebugEnabled()) {
                LOG.debug("key:" + data.getAssignedKey());
                LOG.debug("expiration:" + data.getExpiration());
            }

            String versionKey = data.getNamespace() + CacheConstant.NAMESPACE_VERSION_KEY;
            if (StringUtils.isBlank(data.getAssignedKey()) || CacheConstant.UNDEFINE_ASSIGNED_KEY.equals(data.getAssignedKey())) {
                // delete namespace removed, just make versio+1
                if (LOG.isInfoEnabled()) {
                    LOG.info("increase version for aop cache namespace:" + data.getNamespace());
                }
                long result = getMemcachedClient(shareCacheSpace).incr(versionKey, 1);
                // LOG.info("result:" +result);
            } else {
                Object versionResult = getMemcachedClient(shareCacheSpace).get(versionKey);
                if (versionResult == null) {
                    // NO CACHED content
                } else {
                    Long version = Long.valueOf(versionResult.toString());
                    StringBuilder sb = new StringBuilder(data.getNamespace()).append(CacheConstant.SEPARATOR).append(version);
                    sb.append(CacheConstant.SEPARATOR).append(data.getAssignedKey());

                    if (LOG.isInfoEnabled()) {
                        LOG.info("delete cache key:" + sb.toString());
                    }
                    getMemcachedClient(shareCacheSpace).delete(sb.toString());
                }
            }
        } catch (Throwable ex) {
            LOG.warn(String.format("Caching on method %s aborted due to an error.", joinPoint.toShortString()), ex);
            return joinPoint.proceed();
        }

        return joinPoint.proceed();
    }



    public boolean isCacheEnable() {
        return true;
    }


}
