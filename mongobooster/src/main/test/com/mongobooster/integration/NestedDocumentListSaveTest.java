package com.mongobooster.integration;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.mongobooster.annotation.Document;
import com.mongobooster.annotation.Field;
import com.mongobooster.annotation.Id;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class NestedDocumentListSaveTest extends AbstractMongoBoosterIntegrationTest {

    @Document
    public class Radar {

        @Field
        private String type;

        /**
         * @return the type
         */
        public String getType() {
            return type;
        }

        /**
         * @param type
         *            the type to set
         */
        public void setType(String type) {
            this.type = type;
        }

    }

    @Document
    public class Boat {

        @Id
        private String id;

        @Field
        private List<Radar> radars;

        /**
         * @return the id
         */
        public String getId() {
            return id;
        }

        /**
         * @param id
         *            the id to set
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * @return the radars
         */
        public List<Radar> getRadars() {
            return radars;
        }

        /**
         * @param radars
         *            the radars to set
         */
        public void setRadars(List<Radar> radars) {
            this.radars = radars;
        }

    }

    @Test
    public void testNestedDocumentListSave() {

        Radar radar1 = new Radar();
        radar1.setType("CX 298");

        Radar radar2 = new Radar();
        radar2.setType("HR 848+");

        Radar radar3 = new Radar();
        radar3.setType("Truevision 392");

        Radar radar4 = new Radar();
        radar4.setType("Capture 388");

        List<Radar> radars = new ArrayList<Radar>();
        radars.add(radar1);
        radars.add(radar2);
        radars.add(radar3);
        radars.add(radar4);

        Boat boat = new Boat();
        boat.setRadars(radars);

        mongoBooster.save(boat);

        DBObject dbObject = new BasicDBObject();
        dbObject.put("_id", new ObjectId(boat.getId()));
        DBObject result = mongoClient.getDB(dbName).getCollection("boat").findOne(dbObject);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.get("radars"));
        BasicDBList dbRadars = ((BasicDBList) result.get("radars"));
        Assert.assertEquals(4, dbRadars.size());
        boolean found1 = false;
        boolean found2 = false;
        boolean found3 = false;
        boolean found4 = false;
        for (Object dbRadar : dbRadars) {
            if ("CX 298".equals(((BasicDBObject) dbRadar).get("type"))) {
                Assert.assertFalse(found1);
                found1 = true;
            } else if ("HR 848+".equals(((BasicDBObject) dbRadar).get("type"))) {
                Assert.assertFalse(found2);
                found2 = true;
            } else if ("Truevision 392".equals(((BasicDBObject) dbRadar).get("type"))) {
                Assert.assertFalse(found3);
                found3 = true;
            } else if ("Capture 388".equals(((BasicDBObject) dbRadar).get("type"))) {
                Assert.assertFalse(found4);
                found4 = true;
            }
        }
        Assert.assertTrue(found1);
        Assert.assertTrue(found2);
        Assert.assertTrue(found3);
        Assert.assertTrue(found4);
    }
}
