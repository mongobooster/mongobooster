package com.mongobooster.integration;

import junit.framework.Assert;

import org.junit.Test;

import com.mongobooster.annotation.Document;
import com.mongobooster.annotation.Field;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class NestedDocumentSaveTest extends AbstractMongoBoosterIntegrationTest {

    @Document
    public class Radar {

        @Field
        private String type2;

        /**
         * @return the type
         */
        public String getType2() {
            return type2;
        }

        /**
         * @param type
         *            the type to set
         */
        public void setType2(String type2) {
            this.type2 = type2;
        }

    }

    @Document
    public class Boat {

        @Field
        private String name;

        @Field
        private Radar radar;

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

        /**
         * @return the radar
         */
        public Radar getRadar() {
            return radar;
        }

        /**
         * @param radar
         *            the radar to set
         */
        public void setRadar(Radar radar) {
            this.radar = radar;
        }

    }

    @Test
    public void testNestedDocumentSave() {

        Radar radar = new Radar();
        radar.setType2("CX 298");

        Boat boat = new Boat();
        boat.setName("Seastar");
        boat.setRadar(radar);

        mongoBooster.save(boat);

        DBObject dbObject = new BasicDBObject();
        dbObject.put("name", "Seastar");
        DBObject result = mongoClient.getDB(dbName).getCollection("boat").findOne(dbObject);

        Assert.assertNotNull(result);
        Assert.assertEquals("Seastar", result.get("name"));
        Assert.assertNotNull(result.get("radar"));
        Assert.assertEquals("CX 298", ((DBObject) result.get("radar")).get("type2"));
        Assert.assertEquals(1, ((DBObject) result.get("radar")).keySet().size());
    }
}
