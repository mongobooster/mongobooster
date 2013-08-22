package com.mongobooster;

import java.util.Collection;

import org.bson.types.ObjectId;

import com.mongobooster.annotation.Document;
import com.mongobooster.annotation.Field;
import com.mongobooster.annotation.Id;
import com.mongobooster.exception.MongoBoosterMappingException;
import com.mongobooster.util.Null;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

/**
 * A MongoDB manager implementation.
 * 
 */
public class MongoBoosterImpl implements MongoBooster {

    private final DB db;

    public MongoBoosterImpl(DB db) {
        this.db = db;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.mongobooster.manager.MongoBooster#findOne(java.lang.String,
     * java.lang.Class)
     */
    public <T> T findOne(String id, Class<T> clazz) {
        return map(
                db.getCollection(clazz.getSimpleName().toLowerCase()).findOne(
                        new BasicDBObject("_id", new ObjectId(id))), clazz);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.mongobooster.manager.MongoBooster#save(java.lang.Object)
     */
    public <T> WriteResult save(T instance) {
        DBObject dbObject = map(instance, instance.getClass());
        WriteResult writeResult = db.getCollection(instance.getClass().getSimpleName().toLowerCase()).save(dbObject);
        fillId(instance, (ObjectId) dbObject.get("_id"));
        return writeResult;
    }

    /**
     * Map from a {@link DBObject} to an instance of the generic type.
     * 
     * @param dbObject
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T> T map(DBObject dbObject, Class<T> clazz) {
        try {
            if (clazz.isAnnotationPresent(Document.class)) {
                T instance = clazz.newInstance();
                for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                    if (dbObject.containsField(field.getName())) {
                        if (field.isAnnotationPresent(Field.class)) {
                            if (field.getType().isAnnotationPresent(Document.class)) {
                                buildSetterMethod(field, clazz).invoke(instance,
                                        map(dbObject.get(field.getName()), field.getType()));
                            } else if (Collection.class.isAssignableFrom(field.getType())
                                    && ((Class<?>) (((java.lang.reflect.ParameterizedType) field.getGenericType())
                                            .getActualTypeArguments()[0])).isAnnotationPresent(Document.class)) {
                                BasicDBList dbList = (BasicDBList) dbObject.get(field.getName());
                                if (!dbList.isEmpty()) {
                                    Class<?> genericType = ((Class<?>) (((java.lang.reflect.ParameterizedType) field
                                            .getGenericType()).getActualTypeArguments()[0]));

                                    Collection<? super Object> collection = null;
                                    if (!Null.class.equals(field.getAnnotation(Field.class).value())) {
                                        collection = (Collection<? super Object>) field.getAnnotation(Field.class)
                                                .value().newInstance();
                                    } else {
                                        collection = (Collection<? super Object>) field.getType().newInstance();
                                    }

                                    for (Object o : dbList) {
                                        collection.add(map((DBObject) o, genericType));
                                    }
                                    buildSetterMethod(field, clazz).invoke(instance, collection);
                                }
                            } else {
                                buildSetterMethod(field, clazz).invoke(instance, dbObject.get(field.getName()));
                            }
                        } else if (field.isAnnotationPresent(Id.class)
                                && String.class.isAssignableFrom(field.getType())) {
                            buildSetterMethod(field, clazz).invoke(instance,
                                    ((ObjectId) dbObject.get("_id")).toString());
                        }
                    }
                }
                return instance;
            }
        } catch (Throwable t) {
            throw new MongoBoosterMappingException(t);
        }
        throw new MongoBoosterMappingException();
    }

    /**
     * Map from an instance of the generic type to a {@link DBObject}.
     * 
     * @param instance
     * @return
     */
    private <T> DBObject map(T instance, Class<?> clazz) {
        try {
            if (clazz.isAnnotationPresent(Document.class)) {
                DBObject dbObject = new BasicDBObject();
                for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Field.class)) {
                        Object child = buildGetterMethod(field, clazz).invoke(instance);
                        if (field.getType().isAnnotationPresent(Document.class)) {
                            if (child != null) {
                                dbObject.put(field.getName(), map(child, field.getType()));
                            }
                        } else {
                            if (Collection.class.isAssignableFrom(field.getType())
                                    && ((Class<?>) (((java.lang.reflect.ParameterizedType) field.getGenericType())
                                            .getActualTypeArguments()[0])).isAnnotationPresent(Document.class)) {
                                Class<?> genericType = ((Class<?>) (((java.lang.reflect.ParameterizedType) field
                                        .getGenericType()).getActualTypeArguments()[0]));
                                BasicDBList dbList = new BasicDBList();
                                for (Object o : (Collection<?>) child) {
                                    dbList.add(map(o, genericType));
                                }
                                dbObject.put(field.getName(), dbList);
                            } else {
                                dbObject.put(field.getName(), buildGetterMethod(field, clazz).invoke(instance));
                            }
                        }
                    } else if (field.isAnnotationPresent(Id.class) && String.class.isAssignableFrom(field.getType())) {
                        Object id = buildGetterMethod(field, clazz).invoke(instance);
                        if (id != null) {
                            dbObject.put("_id", new ObjectId((String) id));
                        }
                    }
                }
                return dbObject;
            }
        } catch (Throwable t) {
            throw new MongoBoosterMappingException(t);
        }
        throw new MongoBoosterMappingException();
    }

    private <T> void fillId(T instance, ObjectId id) {
        Class<?> clazz = instance.getClass();
        try {
            if (clazz.isAnnotationPresent(Document.class)) {
                for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Id.class)) {
                        buildSetterMethod(field, clazz).invoke(instance, id.toString());
                        return;
                    }
                }
            }
        } catch (Throwable t) {
            throw new MongoBoosterMappingException(t);
        }
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
    private java.lang.reflect.Method buildGetterMethod(java.lang.reflect.Field field, Class<?> clazz)
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
    private java.lang.reflect.Method buildSetterMethod(java.lang.reflect.Field field, Class<?> clazz)
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
