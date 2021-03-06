package com.mongobooster.unit;

import junit.framework.Assert;

import org.junit.Test;

import com.mongobooster.annotation.Document;
import com.mongobooster.annotation.Field;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class InheritanceTest extends AbstractMongoBoosterUnitTest {

    public static abstract class Animal {

        @Field
        private int age;

        /**
         * @return the age
         */
        public int getAge() {
            return age;
        }

        /**
         * @param age
         *            the age to set
         */
        public void setAge(int age) {
            this.age = age;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + age;
            return result;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof Animal)) {
                return false;
            }
            Animal other = (Animal) obj;
            if (age != other.age) {
                return false;
            }
            return true;
        }

    }

    public static abstract class Fish extends Animal {

        @Field
        private int finCount;

        /**
         * @return the finCount
         */
        public int getFinCount() {
            return finCount;
        }

        /**
         * @param finCount
         *            the finCount to set
         */
        public void setFinCount(int finCount) {
            this.finCount = finCount;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + finCount;
            return result;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj)) {
                return false;
            }
            if (!(obj instanceof Fish)) {
                return false;
            }
            Fish other = (Fish) obj;
            if (finCount != other.finCount) {
                return false;
            }
            return true;
        }

    }

    @Document
    public static class Salmon extends Fish {

        @Field
        private String someProperty;

        /**
         * @return the someProperty
         */
        public String getSomeProperty() {
            return someProperty;
        }

        /**
         * @param someProperty
         *            the someProperty to set
         */
        public void setSomeProperty(String someProperty) {
            this.someProperty = someProperty;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + ((someProperty == null) ? 0 : someProperty.hashCode());
            return result;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj)) {
                return false;
            }
            if (!(obj instanceof Salmon)) {
                return false;
            }
            Salmon other = (Salmon) obj;
            if (someProperty == null) {
                if (other.someProperty != null) {
                    return false;
                }
            } else if (!someProperty.equals(other.someProperty)) {
                return false;
            }
            return true;
        }

    }

    @Test
    public void test() {
        DBObject dbObject = new BasicDBObject();
        dbObject.put("age", 3);
        dbObject.put("finCount", 2);
        dbObject.put("someProperty", "someValue");

        Salmon salmon = new Salmon();
        salmon.setAge(3);
        salmon.setFinCount(2);
        salmon.setSomeProperty("someValue");

        Assert.assertEquals(dbObject, mapper.map(salmon, Salmon.class));
        Assert.assertEquals(salmon, mapper.map(dbObject, Salmon.class));
    }

}
