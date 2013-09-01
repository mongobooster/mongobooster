package com.mongobooster;

import java.util.Collection;

import org.bson.types.ObjectId;

import com.mongobooster.annotation.Document;
import com.mongobooster.annotation.Field;
import com.mongobooster.annotation.Id;
import com.mongobooster.exception.MongoBoosterMappingException;
import com.mongobooster.util.MethodBuilderUtil;
import com.mongobooster.util.Null;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * A mapper for mapping {@link DBObject}s to custom objects and vice versa.
 * 
 * @author Dieter De Hen
 * 
 */
public class MongoBoosterMapperImpl implements MongoBoosterMapper {

    /*
     * (non-Javadoc)
     * 
     * @see com.mongobooster.MongoBoosterMapper#map(com.mongodb.DBObject,
     * java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public <T> T map(DBObject dbObject, Class<T> clazz) {
        try {
            if (clazz.isAnnotationPresent(Document.class)) {
                T instance = clazz.newInstance();
                for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                    if (dbObject.containsField(field.getName())) {
                        if (field.isAnnotationPresent(Field.class)) {
                            Class<?> type = null;
                            if (!Null.class.equals(field.getAnnotation(Field.class).type())) {
                                type = field.getAnnotation(Field.class).type();
                            } else {
                                type = field.getType();
                            }

                            if (type.isAnnotationPresent(Document.class)) {
                                MethodBuilderUtil.buildSetterMethod(field, clazz).invoke(instance,
                                        map(dbObject.get(field.getName()), type));
                            } else if (Collection.class.isAssignableFrom(type)) {
                                BasicDBList dbList = (BasicDBList) dbObject.get(field.getName());
                                if (!dbList.isEmpty()) {
                                    Collection<? super Object> collection = (Collection<? super Object>) type
                                            .newInstance();

                                    if (((Class<?>) (((java.lang.reflect.ParameterizedType) field.getGenericType())
                                            .getActualTypeArguments()[0])).isAnnotationPresent(Document.class)) {
                                        Class<?> genericType = ((Class<?>) (((java.lang.reflect.ParameterizedType) field
                                                .getGenericType()).getActualTypeArguments()[0]));

                                        for (Object o : dbList) {
                                            collection.add(map((DBObject) o, genericType));
                                        }
                                    } else {
                                        for (Object o : dbList) {
                                            collection.add(o);
                                        }
                                    }
                                    MethodBuilderUtil.buildSetterMethod(field, clazz).invoke(instance, collection);
                                }
                            } else {
                                MethodBuilderUtil.buildSetterMethod(field, clazz).invoke(instance,
                                        dbObject.get(field.getName()));
                            }
                        } else if (field.isAnnotationPresent(Id.class)
                                && String.class.isAssignableFrom(field.getType())) {
                            MethodBuilderUtil.buildSetterMethod(field, clazz).invoke(instance,
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

    /*
     * (non-Javadoc)
     * 
     * @see com.mongobooster.MongoBoosterMapper#map(java.lang.Object,
     * java.lang.Class)
     */
    public <T> DBObject map(T instance, Class<?> clazz) {
        try {
            if (clazz.isAnnotationPresent(Document.class)) {
                DBObject dbObject = new BasicDBObject();
                for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Field.class)) {
                        Object child = MethodBuilderUtil.buildGetterMethod(field, clazz).invoke(instance);
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
                                dbObject.put(field.getName(),
                                        MethodBuilderUtil.buildGetterMethod(field, clazz).invoke(instance));
                            }
                        }
                    } else if (field.isAnnotationPresent(Id.class) && String.class.isAssignableFrom(field.getType())) {
                        Object id = MethodBuilderUtil.buildGetterMethod(field, clazz).invoke(instance);
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

}
