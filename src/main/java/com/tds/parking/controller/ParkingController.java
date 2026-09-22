package com.tds.parking.controller;

import com.tds.parking.dto.*;
import com.tds.parking.service.ParkingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parking")
public class ParkingController {

    private final ParkingService service;

    public ParkingController(ParkingService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ParkingStatusResponse> getStatus() {
        return ResponseEntity.ok(service.getStatus());
    }

    @PostMapping
    public ResponseEntity<ParkVehicleResponse> parkVehicle(@Valid @RequestBody ParkVehicleRequest request) {
        return ResponseEntity.ok(service.parkVehicle(request));
    }

    @PostMapping("/bill")
    public ResponseEntity<ExitVehicleResponse>  exitVehicle(@Valid @RequestBody ExitVehicleRequest request) {
        return ResponseEntity.ok(service.exitVehicle(request));
    }
}
