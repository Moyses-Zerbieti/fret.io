package com.fret.io.driver_service.controller;

import com.fret.io.driver_service.dto.VehicleRequest;
import com.fret.io.driver_service.dto.VehicleResponse;
import com.fret.io.driver_service.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/register/vehicle")
    public ResponseEntity<Void> registerVehicle
            (@RequestHeader("X-User-Id")UUID userId,
             @Valid @RequestBody VehicleRequest request){

        vehicleService.registerVehicle(userId,request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<VehicleResponse> findVehicle
            (@RequestHeader("X-User-Id") UUID userId,
             @PathVariable UUID vehicleId){
        VehicleResponse vehicleFounded = vehicleService.findVehicle(vehicleId, userId);

        return ResponseEntity.status(HttpStatus.OK).body(vehicleFounded);
    }

}
