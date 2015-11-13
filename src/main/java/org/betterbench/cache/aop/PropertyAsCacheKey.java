package org.betterbench.cache.aop;

import java.lang.annotation.*;

/**
 * 用于获取对象中的属性值当做memcached的key,可以和PropertyAsCacheKey联合使用
 * @author CHQ
 * @date Oct 15, 2013
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface PropertyAsCacheKey {

	public abstract String[] value();
	
}
