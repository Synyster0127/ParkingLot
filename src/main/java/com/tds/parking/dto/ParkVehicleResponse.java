package com.tds.parking.dto;

import java.time.OffsetDateTime;

public record ParkVehicleResponse (String vehicleReg, Integer spaceNumber, OffsetDateTime timeIn) {
}
