package com.mongobooster;

import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.mongodb.MongoClient;

@RunWith(JUnit4.class)
public abstract class AbstractMongoBoosterTest {

    protected final String dbName = "mongobooster-test";
    protected MongoClient mongoClient;
    protected MongoBooster mongoBooster;

    @Before
    public void init() throws UnknownHostException {
        mongoClient = new MongoClient("localhost", 27017);

        mongoBooster = new MongoBoosterImpl(mongoClient.getDB(dbName));
    }

    @After
    public void cleanUp() {
        // mongoClient.dropDatabase(dbName);
        mongoClient.close();
    }

}
