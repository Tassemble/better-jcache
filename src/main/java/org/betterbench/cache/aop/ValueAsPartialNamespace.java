package org.betterbench.cache.aop;

import java.lang.annotation.*;

/**
 * 将一个值当做命名空间一部分
 * @author CHQ
 * @date 15/11/12
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ValueAsPartialNamespace {


}
