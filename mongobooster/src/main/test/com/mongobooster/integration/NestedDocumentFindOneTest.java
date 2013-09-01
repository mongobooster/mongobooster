package com.mongobooster.integration;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.mongobooster.annotation.Document;
import com.mongobooster.annotation.Field;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class NestedDocumentFindOneTest extends AbstractMongoBoosterIntegrationTest {

    @Document
    public static class Owner {
        @Field
        private String name;

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name
         *            the name to set
         */
        public void setName(String name) {
            this.name = name;
        }
    }

    @Document
    public static class Car {
        @Field
        private String firstName;

        @Field
        private String lastName;

        @Field(type = ArrayList.class)
        private List<Owner> owners;

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
         * @return the owners
         */
        public List<Owner> getOwners() {
            return owners;
        }

        /**
         * @param owners
         *            the owners to set
         */
        public void setOwners(List<Owner> owners) {
            this.owners = owners;
        }
    }

    @Test
    public void testNestedDocumentFindOne() {
        DBObject dbObject = new BasicDBObject();
        dbObject.put("color", "blue");
        dbObject.put("pk", "150");

        BasicDBList owners = new BasicDBList();
        owners.add(new BasicDBObject("name", "Dieter"));
        owners.add(new BasicDBObject("name", "Bert"));
        owners.add(new BasicDBObject("name", "Mark"));
        dbObject.put("owners", owners);

        mongoClient.getDB(dbName).getCollection("car").save(dbObject);
        String objectId = ((ObjectId) dbObject.get("_id")).toString();

        Car car = mongoBooster.findOne(objectId, Car.class);
    }
}
