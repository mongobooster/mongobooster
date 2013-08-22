package com.mongobooster;

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

public class SaveTest extends AbstractMongoBoosterTest {

    @Test
    public void testBasicSuccessfulSave() {

        @Document
        class TestObject {

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

    @Test
    public void testBasicSuccessfulSaveWithId() {

        @Document
        class AnObjectWithId {

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

    @Test
    public void testBasicSuccessfulListSave() {

        @Document
        class TestObjectWithList {

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

    @Test
    public void testNestedDocumentSave() {

        @Document
        class Radar {

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
        class Boat {

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

    @Test
    public void testNestedDocumentListSave() {

        @Document
        class Radar {

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
        class Boat {

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
