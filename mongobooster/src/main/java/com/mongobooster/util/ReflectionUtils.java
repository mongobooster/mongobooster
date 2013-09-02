package com.mongobooster.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for building getter and setter methods.
 * 
 * @author Dieter De Hen
 * 
 */
public class ReflectionUtils {

    private ReflectionUtils() {

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

    /**
     * Get all fields of a class, including fields of super classes.
     * 
     * @param clazz
     * @return
     */
    public static List<java.lang.reflect.Field> getFields(Class<?> clazz) {
        List<java.lang.reflect.Field> fields = new ArrayList<java.lang.reflect.Field>();
        Class<?> c = clazz;
        do {
            for (java.lang.reflect.Field field : c.getDeclaredFields()) {
                fields.add(field);
            }
            c = c.getSuperclass();
        } while (c != Object.class);

        return fields;
    }
}
