package com.tds.parking.model;

import java.time.OffsetDateTime;

public class ParkingSpace {

    private final int spaceNumber;
    private boolean available;
    private String vehicleReg;
    private VehicleType vehicleType;
    private OffsetDateTime timeIn;

    public ParkingSpace(int spaceNumber) {
        this.spaceNumber = spaceNumber;
        available = true;
    }

    public int getSpaceNumber() {return spaceNumber;}
    public boolean isAvailable() {return available;}
    public String getVehicleReg() {return vehicleReg;}
    public VehicleType getVehicleType() {return vehicleType;}
    public OffsetDateTime getTimeIn() {return timeIn;}

    public void park(String vehicleReg, VehicleType vehicleType, OffsetDateTime minutesSpent) {
        this.vehicleReg = vehicleReg;
        this.vehicleType = vehicleType;
        this.timeIn = minutesSpent;
        this.available = false;
    }

    public void endParking() {
        this.vehicleReg = null;
        this.vehicleType = null;
        this.timeIn = null;
        this.available = true;
    }
}
