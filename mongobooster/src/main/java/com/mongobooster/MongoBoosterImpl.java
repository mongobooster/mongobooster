package com.mongobooster;

import org.bson.types.ObjectId;

import com.mongobooster.annotation.Document;
import com.mongobooster.annotation.Field;
import com.mongobooster.annotation.Id;
import com.mongobooster.exception.MongoBoosterMappingException;
import com.mongobooster.util.MethodBuilderUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

/**
 * A MongoBooster implementation.
 * 
 * @author Dieter De Hen
 * 
 */
public class MongoBoosterImpl implements MongoBooster {

    private final DB db;
    private final MongoBoosterMapper mapper;

    public MongoBoosterImpl(DB db) {
        this.db = db;
        this.mapper = new MongoBoosterMapperImpl();
    }

    public MongoBoosterImpl(DB db, MongoBoosterMapper mongoBoosterMapper) {
        this.db = db;
        this.mapper = mongoBoosterMapper;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.mongobooster.manager.MongoBooster#findOne(java.lang.String,
     * java.lang.Class)
     */
    public <T> T findOne(String id, Class<T> clazz) {
        return mapper.map(
                db.getCollection(clazz.getSimpleName().toLowerCase()).findOne(
                        new BasicDBObject("_id", new ObjectId(id))), clazz);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.mongobooster.manager.MongoBooster#save(java.lang.Object)
     */
    public <T> WriteResult save(T instance) {
        DBObject dbObject = mapper.map(instance, instance.getClass());
        WriteResult writeResult = db.getCollection(instance.getClass().getSimpleName().toLowerCase()).save(dbObject);
        fillId(instance, (ObjectId) dbObject.get("_id"));
        createIndexes(instance);
        return writeResult;
    }

    private <T> void fillId(T instance, ObjectId id) {
        Class<?> clazz = instance.getClass();
        try {
            if (clazz.isAnnotationPresent(Document.class)) {
                for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Id.class)) {
                        MethodBuilderUtil.buildSetterMethod(field, clazz).invoke(instance, id.toString());
                        return;
                    }
                }
            }
        } catch (Throwable t) {
            throw new MongoBoosterMappingException(t);
        }
    }

    private <T> void createIndexes(T instance) {
        Class<?> clazz = instance.getClass();
        try {
            if (clazz.isAnnotationPresent(Document.class)) {
                for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Field.class)) {
                        if (field.getAnnotation(Field.class).indexed()) {
                            db.getCollection(clazz.getSimpleName().toLowerCase()).ensureIndex(field.getName());
                        }
                    }
                }
            }
        } catch (Throwable t) {
            throw new MongoBoosterMappingException(t);
        }
    }
}
