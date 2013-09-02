package com.mongobooster.unit;

import junit.framework.Assert;

import org.junit.Test;

import com.mongobooster.annotation.Document;
import com.mongobooster.annotation.Field;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class InheritanceTest extends AbstractMongoBoosterUnitTest {

    @Document
    public static abstract class Vehicle {

        @Field
        private int maxPassengerCount;

        /**
         * @return the maxPassengerCount
         */
        public int getMaxPassengerCount() {
            return maxPassengerCount;
        }

        /**
         * @param maxPassengerCount
         *            the maxPassengerCount to set
         */
        public void setMaxPassengerCount(int maxPassengerCount) {
            this.maxPassengerCount = maxPassengerCount;
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
            result = prime * result + maxPassengerCount;
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
            if (!(obj instanceof Vehicle)) {
                return false;
            }
            Vehicle other = (Vehicle) obj;
            if (maxPassengerCount != other.maxPassengerCount) {
                return false;
            }
            return true;
        }

    }

    @Document
    public static class Car extends Vehicle {

        @Field
        private String engine;

        /**
         * @return the engine
         */
        public String getEngine() {
            return engine;
        }

        /**
         * @param engine
         *            the engine to set
         */
        public void setEngine(String engine) {
            this.engine = engine;
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
            result = prime * result + ((engine == null) ? 0 : engine.hashCode());
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
            if (!(obj instanceof Car)) {
                return false;
            }
            Car other = (Car) obj;
            if (engine == null) {
                if (other.engine != null) {
                    return false;
                }
            } else if (!engine.equals(other.engine)) {
                return false;
            }
            return true;
        }

    }

    @Document
    public static class Garage {

        @Field(type = Car.class)
        private Vehicle vehicle;

        /**
         * @return the vehicle
         */
        public Vehicle getVehicle() {
            return vehicle;
        }

        /**
         * @param vehicle
         *            the vehicle to set
         */
        public void setVehicle(Vehicle vehicle) {
            this.vehicle = vehicle;
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
            result = prime * result + ((vehicle == null) ? 0 : vehicle.hashCode());
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
            if (!(obj instanceof Garage)) {
                return false;
            }
            Garage other = (Garage) obj;
            if (vehicle == null) {
                if (other.vehicle != null) {
                    return false;
                }
            } else if (!vehicle.equals(other.vehicle)) {
                return false;
            }
            return true;
        }

    }

    @Test
    public void test() {
        DBObject dbCar = new BasicDBObject();
        dbCar.put("maxPassengerCount", 5);
        dbCar.put("engine", "V12");
        DBObject dbObject = new BasicDBObject("vehicle", dbCar);

        Car car = new Car();
        car.setMaxPassengerCount(5);
        car.setEngine("V12");

        Garage garage = new Garage();
        garage.setVehicle(car);

        Assert.assertEquals(dbObject, mapper.map(garage, Garage.class));
        Assert.assertEquals(garage, mapper.map(dbObject, Garage.class));
    }

}
