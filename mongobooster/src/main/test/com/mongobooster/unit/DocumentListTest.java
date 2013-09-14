package com.mongobooster.unit;

import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.mongobooster.annotation.Document;
import com.mongobooster.annotation.Field;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class DocumentListTest extends AbstractMongoBoosterUnitTest {

    @Document
    public static class Student {

        @Field
        private String name;

        public Student() {
            super();
        }

        public Student(String name) {
            super();
            this.name = name;
        }

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

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
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
            if (!(obj instanceof Student)) {
                return false;
            }
            Student other = (Student) obj;
            if (name == null) {
                if (other.name != null) {
                    return false;
                }
            } else if (!name.equals(other.name)) {
                return false;
            }
            return true;
        }

    }

    @Document
    public static class School {

        @Field(type = LinkedList.class)
        private List<Student> students;

        /**
         * @return the students
         */
        public List<Student> getStudents() {
            return students;
        }

        /**
         * @param students
         *            the students to set
         */
        public void setStudents(List<Student> students) {
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
        BasicDBList dbList = new BasicDBList();
        dbList.add(new BasicDBObject("name", "John Doe"));
        dbList.add(new BasicDBObject("name", "Fons De Spons"));
        dbList.add(new BasicDBObject("name", "Bert Het Hert"));

        DBObject dbObject = new BasicDBObject("students", dbList);

        School school = new School();
        List<Student> students = new LinkedList<Student>();
        students.add(new Student("John Doe"));
        students.add(new Student("Fons De Spons"));
        students.add(new Student("Bert Het Hert"));

        school.setStudents(students);

        Assert.assertEquals(dbObject, mapper.map(school, School.class));
        Assert.assertEquals(school, mapper.map(dbObject, School.class));
    }
}
