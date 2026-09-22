package com.tds.parking.dto;

import jakarta.validation.constraints.NotBlank;

public record ExitVehicleRequest(
        @NotBlank(message = "Vehicle registration is required")
        String vehicleReg
) {}