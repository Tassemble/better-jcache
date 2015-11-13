package org.betterbench.cache.aop;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.betterbench.commons.clazz.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CHQ
 * @date Oct 18, 2013
 * @since 1.0
 */
public class CacheKeyBuilder {
	static final Logger LOG = LoggerFactory.getLogger(CacheKeyBuilder.class);
	
	public static String getNamespace(Method method, Object[] parameters, Class<? extends Annotation> annotation)
			throws Exception {
		Annotation cacheAnnotation = AnnotationUtils.getAnnotation(method, annotation);
//		Annotation cacheAnnotation = method.getAnnotation(annotation);
		Method propertyMethod = annotation.getClass().getMethod(CacheConstant.namespace);
		//get namespace
		//Method propertyMethod = ReflectionUtils.getPropertyMethod(assignedCache.getClass(), "namespace");
		String namespace = (String)propertyMethod.invoke(cacheAnnotation);
		
		
		return namespace;
	}
	
	
	public static Map<String, String> getCacheKey(Method method, Object[] parameters, Class<? extends Annotation> annotation)
			throws Exception {
		
		Annotation cacheAnnotation = method.getAnnotation(annotation);

		//get key
		Method propertyMethod = cacheAnnotation.getClass().getMethod(CacheConstant.assignedKey);
		String key = (String)propertyMethod.invoke(cacheAnnotation);
		
		return innerGetKeyByParameters(method, parameters, key);
	}
	
	

	private static Map<String, String> innerGetKeyByParameters(Method method, Object[] parameters, String preservedKey) throws Exception,
			IllegalAccessException, InvocationTargetException {
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		Class[] parameterTypes = method.getParameterTypes();

		
		StringBuilder keyBuider = new StringBuilder();
		int i = 0;
        StringBuilder spaceBuider = new StringBuilder("");
		if (!ArrayUtils.isEmpty(parameterAnnotations)) {
			for (Annotation[] annotations : parameterAnnotations) {
				Class parameterType = parameterTypes[i];
				Object object = parameters[i++];
				for (Annotation annotation : annotations) {
					if (annotation instanceof ValueAsCacheKey) {
						if (object instanceof List) {
							throw new Exception("can't support List as key");
						}
						keyBuider.append(String.valueOf(object));
					} else if (annotation instanceof PropertyAsCacheKey) {
						if (object instanceof List) {
							throw new Exception("can't support List as key");
						}
						PropertyAsCacheKey propertyCacheKey = (PropertyAsCacheKey) annotation;
						String[] properties = propertyCacheKey.value();

						for (String pro : properties) {
							Method propertyMethod = ReflectionUtils.getPropertyMethod(parameterType, pro);
							Object value = propertyMethod.invoke(object);
							keyBuider.append(String.valueOf(value));
						}
					}  else if (annotation instanceof ValueAsPartialNamespace) {
                        if (object instanceof List) {
                            throw new Exception("can't support List as key");
                        }
                        spaceBuider.append(String.valueOf(object));
                    } else if (annotation instanceof PropertyAsPartialNamespace) {
                        if (object instanceof List) {
                            throw new Exception("can't support List as key");
                        }
                        PropertyAsPartialNamespace propertyCacheKey = (PropertyAsPartialNamespace) annotation;
                        String[] properties = propertyCacheKey.value();

                        for (String pro : properties) {
                            Method propertyMethod = ReflectionUtils.getPropertyMethod(parameterType, pro);
                            Object value = propertyMethod.invoke(object);
                            spaceBuider.append(String.valueOf(value));
                        }
                    }
				}
			}
		}
		if (StringUtils.isBlank(keyBuider.toString())) {
//		    LOG.warn("no key is assigned in method:"
//                    + method.getName()
//                    + ", this will lead to invalidate all cache with same namespace");


            Map<String, String> map = new HashMap<String, String>();
            map.put("key", preservedKey);
            map.put("partialSpace", spaceBuider.toString());
            return map;
		}

        Map<String, String> map = new HashMap<String, String>();
        map.put("key", keyBuider.toString());
        map.put("partialSpace", spaceBuider.toString());
        return map;
	}
	
}
