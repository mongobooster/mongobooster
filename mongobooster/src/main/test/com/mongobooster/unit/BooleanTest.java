package com.mongobooster.unit;

import junit.framework.Assert;

import org.junit.Test;

import com.mongobooster.annotation.Document;
import com.mongobooster.annotation.Field;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class BooleanTest extends AbstractMongoBoosterUnitTest {

    @Document
    public static class SomeObjectWithBoolean {

        @Field
        private Boolean condition;

        /**
         * @return the condition
         */
        public Boolean getCondition() {
            return condition;
        }

        /**
         * @param condition
         *            the condition to set
         */
        public void setCondition(Boolean condition) {
            this.condition = condition;
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
            if (!(obj instanceof SomeObjectWithBoolean)) {
                return false;
            }
            SomeObjectWithBoolean other = (SomeObjectWithBoolean) obj;
            if (condition == null) {
                if (other.condition != null) {
                    return false;
                }
            } else if (!condition.equals(other.condition)) {
                return false;
            }
            return true;
        }

    }

    @Test
    public void test() {
        DBObject dbObject = new BasicDBObject("condition", true);

        SomeObjectWithBoolean someObjectWithBoolean = new SomeObjectWithBoolean();
        someObjectWithBoolean.setCondition(true);

        Assert.assertEquals(dbObject, mapper.map(someObjectWithBoolean, SomeObjectWithBoolean.class));
        Assert.assertEquals(someObjectWithBoolean, mapper.map(dbObject, SomeObjectWithBoolean.class));
    }
}
