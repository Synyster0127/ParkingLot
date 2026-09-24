package com.tds.parking.service;

import com.tds.parking.config.ParkingConfig;
import com.tds.parking.dto.*;
import com.tds.parking.exception.ParkingFullException;
import com.tds.parking.exception.VehicleNotFoundException;
import com.tds.parking.model.ParkingSpace;
import com.tds.parking.model.VehicleType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ParkingService {

    private final List<ParkingSpace> spaces;

    public ParkingService() {
        this.spaces = new ArrayList<>();
        for (int i = 1; i <= ParkingConfig.TOTAL_SPACES; i++) {
            this.spaces.add(new ParkingSpace(i));
        }
    }

    public ParkingStatusResponse getStatus() {
        int availableSpaces = (int) spaces.stream().filter(ParkingSpace::isAvailable).count();
        int occupiedSpaces = spaces.size() - availableSpaces;
        return new ParkingStatusResponse(availableSpaces, occupiedSpaces);
    }

    public ParkVehicleResponse parkVehicle(ParkVehicleRequest request) {
        ParkingSpace availableSpace = spaces.stream()
                .filter(ParkingSpace::isAvailable)
                .findFirst()
                .orElseThrow(() -> new ParkingFullException("The parking lot is currently full"));

        OffsetDateTime timeIn = OffsetDateTime.now();
        availableSpace.park(request.vehicleReg(), VehicleType.fromCode(request.vehicleType()), timeIn);
        return new ParkVehicleResponse(availableSpace.getVehicleReg(), availableSpace.getSpaceNumber(), availableSpace.getTimeIn());
    }

    public ExitVehicleResponse exitVehicle(ExitVehicleRequest request) {
        ParkingSpace space = spaces.stream()
                .filter(s -> !s.isAvailable() && s.getVehicleReg().equalsIgnoreCase(request.vehicleReg()))
                .findFirst()
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle with registration " + request.vehicleReg() + " not found"));

        OffsetDateTime timeIn = space.getTimeIn();
        OffsetDateTime timeOut = OffsetDateTime.now();
        VehicleType type = space.getVehicleType();

        BigDecimal price = calculateCharge(type, timeIn, timeOut);
        String billId = UUID.randomUUID().toString();

        space.endParking();

        return new ExitVehicleResponse(billId, request.vehicleReg(), price.doubleValue(),  timeIn, timeOut);
    }

    public BigDecimal calculateCharge(VehicleType vehicleType, OffsetDateTime timeIn, OffsetDateTime timeOut) {
        long minutes = Duration.between(timeIn, timeOut).toMinutes();
        BigDecimal charge = vehicleType.getPrice().multiply(BigDecimal.valueOf(minutes));
        BigDecimal additionalCharge = BigDecimal.valueOf(minutes / 5);

        return charge.add(additionalCharge);
    }
}
