package com.tds.parking.model;

import com.tds.parking.exception.InvalidVehicleTypeException;

import java.math.BigDecimal;

public enum VehicleType {

    SMALL(1, new BigDecimal("0.10")),
    MEDIUM (2, new BigDecimal("0.20")),
    LARGE (3, new BigDecimal("0.40"));

    private final int code;
    private final BigDecimal price;

    VehicleType(int code, BigDecimal price) {
        this.code = code;
        this.price = price;
    }

    public int getCode() {
        return code;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public static VehicleType fromCode(int code) {
        for (VehicleType vehicleType : VehicleType.values()) {
            if (vehicleType.code == code) {
                return vehicleType;
            }
        }
        throw new InvalidVehicleTypeException("Invalid vehicle type: " + code + ". Must be 1 (Small), 2 (Medium), or 3 (Large).");
    }
}
