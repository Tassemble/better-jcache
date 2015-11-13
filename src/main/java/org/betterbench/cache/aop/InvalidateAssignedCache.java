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
public @interface InvalidateAssignedCache {

    /**
     * aop cache的命名空间，用于更新时删除某一命令空间下的所有cache
     */
    String namespace() default CacheConstant.UNDEFINE_NAMESPACE;

    /**
     * 优先使用assignedKey， 与命令空间配合使用的key，如果没有设置， 则会使用@ParameterCacheKey或者@PropertyCacheKey中指定的key
     */
    String assignedKey() default CacheConstant.UNDEFINE_ASSIGNED_KEY;

    /**
     * 是否使用共享的cache空间，默认使用产品独立的空间
     * 
     * @return
     */
    boolean shareCacheSpace() default false;
}
