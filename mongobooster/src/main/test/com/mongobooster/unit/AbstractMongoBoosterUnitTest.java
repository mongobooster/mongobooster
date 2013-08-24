package com.mongobooster.unit;

import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.mongobooster.MongoBoosterMapper;
import com.mongobooster.MongoBoosterMapperImpl;

/**
 * Abstract class to define a MongoBooster unit test.
 * 
 * @author Dieter De Hen
 * 
 */
@RunWith(JUnit4.class)
public abstract class AbstractMongoBoosterUnitTest {

    protected MongoBoosterMapper mapper;

    @Before
    public void init() throws UnknownHostException {
        mapper = new MongoBoosterMapperImpl();
    }

    @After
    public void cleanUp() {
        // clean up
    }

}
