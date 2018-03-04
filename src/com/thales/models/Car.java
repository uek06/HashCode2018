package com.thales.models;

import java.util.ArrayList;
import java.util.List;

public class Car {
    private int id;
    private Position position;
    private List<Integer> rideIds;
    private Ride currentRide;

    public Car(){
        this.position = new Position(0,0);
        this.rideIds = new ArrayList<>();
        this.currentRide = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public List<Integer> getRideIds() {
        return rideIds;
    }

    public void setRideIds(List<Integer> rideIds) {
        this.rideIds = rideIds;
    }

    public Ride getCurrentRide() {
        return currentRide;
    }

    public void setCurrentRide(Ride currentRide) {
        this.currentRide = currentRide;
    }
}
