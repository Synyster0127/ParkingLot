package com.tds.parking.dto;

import java.time.OffsetDateTime;

public record ExitVehicleResponse(
        String billId,
        String vehicleReg,
        Double vehicleCharge,
        OffsetDateTime timeIn,
        OffsetDateTime timeOut
) {}
