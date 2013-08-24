package com.mongobooster.integration;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.mongobooster.annotation.Document;
import com.mongobooster.annotation.Field;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class BasicSuccessfulListSaveTest extends AbstractMongoBoosterIntegrationTest {

    @Document
    public class TestObjectWithList {

        @Field
        private String testField1;

        @Field
        private List<String> testList;

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
         * @return the testList
         */
        public List<String> getTestList() {
            return testList;
        }

        /**
         * @param testList
         *            the testList to set
         */
        public void setTestList(List<String> testList) {
            this.testList = testList;
        }

    }

    @Test
    public void testBasicSuccessfulListSave() {

        TestObjectWithList testObjectWithList = new TestObjectWithList();
        List<String> l = new ArrayList<String>();
        l.add("value 1");
        l.add("another value 2");
        l.add("something else...");
        testObjectWithList.setTestList(l);
        testObjectWithList.setTestField1("a super interesting value!");

        mongoBooster.save(testObjectWithList);

        DBObject dbObject = new BasicDBObject();
        // TODO
        // dbObject.put("testObjectWithList", l);
        dbObject.put("testField1", "a super interesting value!");
        DBObject result = mongoClient.getDB(dbName).getCollection("testobjectwithlist").findOne(dbObject);

        Assert.assertNotNull(result);
    }
}
