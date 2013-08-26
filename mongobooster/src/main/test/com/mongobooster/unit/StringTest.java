package com.mongobooster.unit;

import junit.framework.Assert;

import org.junit.Test;

import com.mongobooster.annotation.Document;
import com.mongobooster.annotation.Field;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class StringTest extends AbstractMongoBoosterUnitTest {

    @Document
    public static class SomeObjectWithString {

        @Field
        private String text;

        /**
         * @return the text
         */
        public String getText() {
            return text;
        }

        /**
         * @param text
         *            the text to set
         */
        public void setText(String text) {
            this.text = text;
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
            if (!(obj instanceof SomeObjectWithString)) {
                return false;
            }
            SomeObjectWithString other = (SomeObjectWithString) obj;
            if (text == null) {
                if (other.text != null) {
                    return false;
                }
            } else if (!text.equals(other.text)) {
                return false;
            }
            return true;
        }

    }

    @Test
    public void test() {
        DBObject dbObject = new BasicDBObject("text", "A stupid text");

        SomeObjectWithString someObjectWithString = new SomeObjectWithString();
        someObjectWithString.setText("A stupid text");

        Assert.assertEquals(dbObject, mapper.map(someObjectWithString, SomeObjectWithString.class));
        Assert.assertEquals(someObjectWithString, mapper.map(dbObject, SomeObjectWithString.class));
    }
}
