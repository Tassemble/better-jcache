package org.betterbench.cache.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author CHQ
 * @date Oct 15, 2013
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AssignedCache {

    /**
     * 如果不自定义更新，在进行更新的时候会删除undefined namespace下的所有cache， aop cache的命名空间，用于更新时删除某一命令空间下的所有cache
     */
    String namespace() default CacheConstant.UNDEFINE_NAMESPACE;

    /**
     * 与命令空间配合使用的key，如果不使用，则会使用@ParameterCacheKey中指定的key
     */
    String assignedKey() default CacheConstant.UNDEFINE_ASSIGNED_KEY;

    int expiration() default 0;

    /**
     * 是否使用共享的cache空间，默认使用产品独立的空间
     * 
     * @return
     */
    boolean shareCacheSpace() default false;
}
