package com.mongobooster.unit;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.mongobooster.annotation.Document;
import com.mongobooster.annotation.Field;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class HashMapTest extends AbstractMongoBoosterUnitTest {

    @Document
    public static class School {

        @Field(type = HashMap.class)
        private Map<String, String> students;

        /**
         * @return the students
         */
        public Map<String, String> getStudents() {
            return students;
        }

        /**
         * @param students
         *            the students to set
         */
        public void setStudents(Map<String, String> students) {
            this.students = students;
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
            result = prime * result + ((students == null) ? 0 : students.hashCode());
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
            if (!(obj instanceof School)) {
                return false;
            }
            School other = (School) obj;
            if (students == null) {
                if (other.students != null) {
                    return false;
                }
            } else if (!students.equals(other.students)) {
                return false;
            }
            return true;
        }

    }

    @Test
    public void test() {
        BasicDBObject dbStudents = new BasicDBObject();
        dbStudents.put("s001", "John Doe");
        dbStudents.put("s002", "Bart Simpson");
        dbStudents.put("s003", "Lisa Simpson");

        DBObject dbObject = new BasicDBObject("students", dbStudents);

        School school = new School();
        Map<String, String> students = new HashMap<String, String>();
        students.put("s001", "John Doe");
        students.put("s002", "Bart Simpson");
        students.put("s003", "Lisa Simpson");

        school.setStudents(students);

        Assert.assertEquals(dbObject, mapper.map(school, School.class));
        Assert.assertEquals(school, mapper.map(dbObject, School.class));
    }
}
