package org.betterbench.commons.clazz;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;

public class ReflectionUtils {

    public static Method getPropertyMethod(Class clz, String propertyName) {
        Method mth = null;
        propertyName = upperFirstChar(propertyName);
        try {

            mth = clz.getMethod("get" + propertyName);
        } catch (Exception e) {
            try {
                mth = clz.getMethod("is" + propertyName);
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        }
        return mth;

    }


    public static String upperFirstChar(String source) {
        if (StringUtils.isBlank(source)) {
            return "";
        }
        Character firstChar = source.charAt(0);
        if (Character.isLowerCase(firstChar)) {
            return Character.toUpperCase(firstChar) + source.substring(1);
        }
        return source;
    }


    
    

}
