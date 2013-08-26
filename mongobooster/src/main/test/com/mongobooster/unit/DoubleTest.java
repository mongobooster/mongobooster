package com.mongobooster.unit;

import junit.framework.Assert;

import org.junit.Test;

import com.mongobooster.annotation.Document;
import com.mongobooster.annotation.Field;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class DoubleTest extends AbstractMongoBoosterUnitTest {

    @Document
    public static class SomeObjectWithDouble {

        @Field
        private Double number;

        /**
         * @return the number
         */
        public Double getNumber() {
            return number;
        }

        /**
         * @param number
         *            the number to set
         */
        public void setNumber(Double number) {
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
            if (!(obj instanceof SomeObjectWithDouble)) {
                return false;
            }
            SomeObjectWithDouble other = (SomeObjectWithDouble) obj;
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
        DBObject dbObject = new BasicDBObject("number", 3449223.323456);

        SomeObjectWithDouble someObjectWithDouble = new SomeObjectWithDouble();
        someObjectWithDouble.setNumber(3449223.323456);

        Assert.assertEquals(dbObject, mapper.map(someObjectWithDouble, SomeObjectWithDouble.class));
        Assert.assertEquals(someObjectWithDouble, mapper.map(dbObject, SomeObjectWithDouble.class));
    }
}
