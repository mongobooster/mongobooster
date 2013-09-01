package com.mongobooster.unit;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.mongobooster.annotation.Document;
import com.mongobooster.annotation.Field;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class ListTest extends AbstractMongoBoosterUnitTest {

    @Document
    public static class School {

        @Field(type = ArrayList.class)
        private List<String> students;

        /**
         * @return the students
         */
        public List<String> getStudents() {
            return students;
        }

        /**
         * @param students
         *            the students to set
         */
        public void setStudents(List<String> students) {
            this.students = students;
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
        //
        BasicDBList dbList = new BasicDBList();
        dbList.add("John Doe");
        dbList.add("Fons De Spons");
        dbList.add("Bert Het Hert");

        DBObject dbObject = new BasicDBObject("students", dbList);

        School school = new School();
        List<String> students = new ArrayList<String>();
        students.add("John Doe");
        students.add("Fons De Spons");
        students.add("Bert Het Hert");

        school.setStudents(students);

        Assert.assertEquals(dbObject, mapper.map(school, School.class));
        Assert.assertEquals(school, mapper.map(dbObject, School.class));
    }
}
