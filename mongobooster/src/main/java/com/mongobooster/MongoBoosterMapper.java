package com.mongobooster;

import com.mongodb.DBObject;

public interface MongoBoosterMapper {

    /**
     * Map from a {@link DBObject} to an instance of the generic type.
     * 
     * @param dbObject
     * @return
     */
    <T> T map(DBObject dbObject, Class<T> clazz);

    /**
     * Map from an instance of the generic type to a {@link DBObject}.
     * 
     * @param instance
     * @return
     */
    <T> DBObject map(T instance, Class<?> clazz);
}
