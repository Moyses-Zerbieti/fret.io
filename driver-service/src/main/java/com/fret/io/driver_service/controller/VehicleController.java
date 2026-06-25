package com.fret.io.driver_service.controller;

import com.fret.io.driver_service.dto.VehicleRequest;
import com.fret.io.driver_service.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/register/vehicle")
    public ResponseEntity<Void> registerVehicle
            (@RequestHeader("X-User-id")UUID userId,
             @Valid @RequestBody VehicleRequest request){

        vehicleService.registerVehicle(userId,request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
