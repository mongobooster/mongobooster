package com.mongobooster.unit;

import junit.framework.Assert;

import org.junit.Test;

import com.mongobooster.annotation.Document;
import com.mongobooster.annotation.Field;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class IntegerTest extends AbstractMongoBoosterUnitTest {

    @Document
    public static class SomeObjectWithInteger {

        @Field
        private Integer number;

        /**
         * @return the number
         */
        public Integer getNumber() {
            return number;
        }

        /**
         * @param number
         *            the number to set
         */
        public void setNumber(Integer number) {
            this.number = number;
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
            if (!(obj instanceof SomeObjectWithInteger)) {
                return false;
            }
            SomeObjectWithInteger other = (SomeObjectWithInteger) obj;
            if (number == null) {
                if (other.number != null) {
                    return false;
                }
            } else if (!number.equals(other.number)) {
                return false;
            }
            return true;
        }

    }

    @Test
    public void test() {
        DBObject dbObject = new BasicDBObject("number", 3920);

        SomeObjectWithInteger someObjectWithInteger = new SomeObjectWithInteger();
        someObjectWithInteger.setNumber(3920);

        Assert.assertEquals(dbObject, mapper.map(someObjectWithInteger, SomeObjectWithInteger.class));
        Assert.assertEquals(someObjectWithInteger, mapper.map(dbObject, SomeObjectWithInteger.class));
    }
}
