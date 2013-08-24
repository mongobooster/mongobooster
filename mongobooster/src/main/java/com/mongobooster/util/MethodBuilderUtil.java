package com.mongobooster.util;

/**
 * Util class for building getter and setter methods.
 * 
 * @author Dieter De Hen
 * 
 */
public class MethodBuilderUtil {

    private MethodBuilderUtil() {

    }

    /**
     * Build a getter method.
     * 
     * @param field
     * @param clazz
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public static java.lang.reflect.Method buildGetterMethod(java.lang.reflect.Field field, Class<?> clazz)
            throws NoSuchMethodException, SecurityException {
        StringBuilder methodNameBuilder = new StringBuilder();
        methodNameBuilder.append("get");
        methodNameBuilder.append(field.getName());
        String methodName = methodNameBuilder.toString();
        java.lang.reflect.Method[] methods = clazz.getMethods();
        for (java.lang.reflect.Method method : methods) {
            if (method.getName().equalsIgnoreCase(methodName)) {
                return method;
            }
        }
        throw new NoSuchMethodException();
    }

    /**
     * Build a setter method.
     * 
     * @param field
     * @param clazz
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public static java.lang.reflect.Method buildSetterMethod(java.lang.reflect.Field field, Class<?> clazz)
            throws NoSuchMethodException {
        StringBuilder methodNameBuilder = new StringBuilder();
        methodNameBuilder.append("set");
        methodNameBuilder.append(field.getName());
        String methodName = methodNameBuilder.toString();
        java.lang.reflect.Method[] methods = clazz.getMethods();
        for (java.lang.reflect.Method method : methods) {
            if (method.getName().equalsIgnoreCase(methodName)) {
                return method;
            }
        }
        throw new NoSuchMethodException();
    }
}
