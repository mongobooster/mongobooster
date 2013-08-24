package com.mongobooster.integration;

import junit.framework.Assert;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.mongobooster.annotation.Document;
import com.mongobooster.annotation.Field;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class BasicSuccessfulFindOneTest extends AbstractMongoBoosterIntegrationTest {

    @Document
    public static class Person {

        @Field
        private String firstName;

        @Field
        private String lastName;

        @Field
        private String address;

        /**
         * @return the firstName
         */
        public String getFirstName() {
            return firstName;
        }

        /**
         * @param firstName
         *            the firstName to set
         */
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        /**
         * @return the lastName
         */
        public String getLastName() {
            return lastName;
        }

        /**
         * @param lastName
         *            the lastName to set
         */
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        /**
         * @return the address
         */
        public String getAddress() {
            return address;
        }

        /**
         * @param address
         *            the address to set
         */
        public void setAddress(String address) {
            this.address = address;
        }

    }

    @Test
    public void testBasicSuccessfulFindOne() {
        DBObject dbObject = new BasicDBObject();
        dbObject.put("firstName", "Dieter");
        dbObject.put("lastName", "De Hen");
        dbObject.put("address", "Mongo lagoon 22");
        mongoClient.getDB(dbName).getCollection("person").save(dbObject);

        String objectId = ((ObjectId) dbObject.get("_id")).toString();

        Person person = mongoBooster.findOne(objectId, Person.class);

        Assert.assertNotNull(person);
        Assert.assertEquals("Dieter", person.getFirstName());
        Assert.assertEquals("De Hen", person.getLastName());
        Assert.assertEquals("Mongo lagoon 22", person.getAddress());
    }
}
