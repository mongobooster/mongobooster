package com.mongobooster.unit;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.mongobooster.annotation.Document;
import com.mongobooster.annotation.Field;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class SetTest extends AbstractMongoBoosterUnitTest {

    @Document
    public static class School {

        @Field(type = HashSet.class)
        private Set<String> students;

        /**
         * @return the students
         */
        public Set<String> getStudents() {
            return students;
        }

        /**
         * @param students
         *            the students to set
         */
        public void setStudents(Set<String> students) {
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

    @SuppressWarnings("unchecked")
    @Test
    public void test() {
        BasicDBList dbList = new BasicDBList();
        dbList.add("John Doe");
        dbList.add("Bart Simpson");
        dbList.add("Lisa Simpson");

        DBObject dbObject = new BasicDBObject("students", dbList);

        School school = new School();
        Set<String> students = new HashSet<String>();
        students.add("John Doe");
        students.add("Bart Simpson");
        students.add("Lisa Simpson");

        school.setStudents(students);

        DBObject mappedDBObject = mapper.map(school, School.class);
        BasicDBList b = (BasicDBList) mappedDBObject.get("students");
        Assert.assertTrue(b.contains("John Doe"));
        Assert.assertTrue(b.contains("Bart Simpson"));
        Assert.assertTrue(b.contains("Lisa Simpson"));

        School mappedSchool = mapper.map(dbObject, School.class);
        Assert.assertTrue(mappedSchool.getStudents().contains("John Doe"));
        Assert.assertTrue(mappedSchool.getStudents().contains("Bart Simpson"));
        Assert.assertTrue(mappedSchool.getStudents().contains("Lisa Simpson"));
    }
}
