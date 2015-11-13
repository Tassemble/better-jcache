package org.betterbench.cache.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.betterbench.cache.aop.dto.AnnotationDataDto;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author CHQ
 * @date Oct 18, 2013
 * @since 1.0
 */
public class AnnotationDataBuilder {

    public static AnnotationDataDto builderAnnotationData(ProceedingJoinPoint joinPoint,
                                                          Class<? extends Annotation> annotationType) throws Exception {

        AnnotationDataDto data = new AnnotationDataDto();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Method targetMethod = joinPoint.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
        if (annotationType.isAssignableFrom(AssignedCache.class)) {

            String namespace = targetMethod.getAnnotation(AssignedCache.class).namespace();
            boolean shareCacheSpace = targetMethod.getAnnotation(AssignedCache.class).shareCacheSpace();
            // String key = targetMethod.getAnnotation(AssignedCache.class).assignedKey();
            int expiration = targetMethod.getAnnotation(AssignedCache.class).expiration();
            Map<String, String> map = getCacheKey(targetMethod, joinPoint.getArgs(), annotationType);
            // int expiration = getCacheExpiration(targetMethod, joinPoint.getArgs(), annotationType);
            if (expiration <= 0) {
                expiration = (int) CacheConstant.DEFAULT_CACHE_TIME;
            }
            data.setNamespace(namespace + map.get("partialSpace"));
            data.setAssignedKey(map.get("key"));
            data.setExpiration(expiration);
            data.setShareCacheSpace(shareCacheSpace);

        } else if (annotationType.isAssignableFrom(InvalidateAssignedCache.class)) {

            String namespace = targetMethod.getAnnotation(InvalidateAssignedCache.class).namespace();
            boolean shareCacheSpace = targetMethod.getAnnotation(InvalidateAssignedCache.class).shareCacheSpace();
            // String key = targetMethod.getAnnotation(InvalidateAssignedCache.class).assignedKey();
            Map<String, String> map = getCacheKey(targetMethod, joinPoint.getArgs(), annotationType);
            data.setAssignedKey(map.get("key"));
            data.setNamespace(namespace + map.get("partialSpace"));
            data.setShareCacheSpace(shareCacheSpace);
        }

        return data;
    }

    public static Map<String, String> getCacheKey(Method method, Object[] args, Class<? extends Annotation> annotationType)
                                                                                                              throws Exception {

        return CacheKeyBuilder.getCacheKey(method, args, annotationType);
    }

    public static void main(String[] args) throws  Exception {
        AnnotationDataBuilder builder = new AnnotationDataBuilder();
        System.out.println(CacheKeyBuilder.getCacheKey(builder.getClass().getMethod("cacheTest", String.class,
                                                                                    Integer.class), new Object[] {
                                                               new String("sjdfiosjdf"), new Integer(23) },
                                                       AssignedCache.class));

    }

}
