package com.mongobooster;

import com.mongodb.WriteResult;

public interface MongoBooster {

    <T> T findOne(String id, Class<T> clazz);

    <T> WriteResult save(T instance);

}
