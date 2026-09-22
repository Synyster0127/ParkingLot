package com.tds.parking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ParkVehicleRequest(
        @NotBlank(message = "Vehicle registration is required")
        String vehicleReg,

        @NotNull(message = "Vehicle type is required")
        Integer vehicleType
) {}