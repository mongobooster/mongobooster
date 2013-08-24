package com.mongobooster.integration;

import junit.framework.Assert;

import org.junit.Test;

import com.mongobooster.annotation.Document;
import com.mongobooster.annotation.Field;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class BasicSuccessfulSaveTest extends AbstractMongoBoosterIntegrationTest {

    @Document
    public class TestObject {

        @Field
        private String testField1;

        @Field
        private String anotherFieldToTest;

        /**
         * @return the testField1
         */
        public String getTestField1() {
            return testField1;
        }

        /**
         * @param testField1
         *            the testField1 to set
         */
        public void setTestField1(String testField1) {
            this.testField1 = testField1;
        }

        /**
         * @return the anotherFieldToTest
         */
        public String getAnotherFieldToTest() {
            return anotherFieldToTest;
        }

        /**
         * @param anotherFieldToTest
         *            the anotherFieldToTest to set
         */
        public void setAnotherFieldToTest(String anotherFieldToTest) {
            this.anotherFieldToTest = anotherFieldToTest;
        }

    }

    @Test
    public void testBasicSuccessfulSave() {

        TestObject testObject = new TestObject();
        testObject.setAnotherFieldToTest("a value");
        testObject.setTestField1("a super interesting value!");

        mongoBooster.save(testObject);

        DBObject dbObject = new BasicDBObject();
        dbObject.put("anotherFieldToTest", "a value");
        dbObject.put("testField1", "a super interesting value!");
        DBObject result = mongoClient.getDB(dbName).getCollection("testobject").findOne(dbObject);

        Assert.assertNotNull(result);
    }
}
