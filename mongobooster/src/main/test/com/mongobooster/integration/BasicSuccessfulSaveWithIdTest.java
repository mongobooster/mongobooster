package com.mongobooster.integration;

import junit.framework.Assert;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.mongobooster.annotation.Document;
import com.mongobooster.annotation.Field;
import com.mongobooster.annotation.Id;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class BasicSuccessfulSaveWithIdTest extends AbstractMongoBoosterIntegrationTest {

    @Document
    public class AnObjectWithId {

        @Id
        private String someId;

        @Field
        private String aField;

        /**
         * @return the someId
         */
        public String getSomeId() {
            return someId;
        }

        /**
         * @param someId
         *            the someId to set
         */
        public void setSomeId(String someId) {
            this.someId = someId;
        }

        /**
         * @return the aField
         */
        public String getaField() {
            return aField;
        }

        /**
         * @param aField
         *            the aField to set
         */
        public void setaField(String aField) {
            this.aField = aField;
        }

    }

    @Test
    public void testBasicSuccessfulSaveWithId() {

        AnObjectWithId anObjectWithId = new AnObjectWithId();
        anObjectWithId.setSomeId("51de74103004bb0cb4af9c8c");
        anObjectWithId.setaField("a field :)");

        mongoBooster.save(anObjectWithId);

        DBObject dbObject = new BasicDBObject();
        dbObject.put("aField", "a field :)");
        dbObject.put("_id", new ObjectId(anObjectWithId.getSomeId()));
        DBObject result = mongoClient.getDB(dbName).getCollection("anobjectwithid").findOne(dbObject);

        Assert.assertNotNull(result);
    }
}
