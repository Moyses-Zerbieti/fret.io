package com.fret.io.driver_service.controller;

import com.fret.io.driver_service.dto.CompleteDriverRegistrationRequest;
import com.fret.io.driver_service.dto.UpdateDriverRequest;
import com.fret.io.driver_service.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PutMapping("/complete-registration")
    public ResponseEntity <Void> completeRegistration
            (@RequestHeader("X-User-Id")UUID userId,
             @Valid @RequestBody CompleteDriverRegistrationRequest request){

        driverService.completeRegistration(userId, request);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/update")
    public ResponseEntity<Void> changeNameDriver
            (@RequestHeader("X-User-Id") UUID userId,
             @RequestBody UpdateDriverRequest request){

        driverService.updateDriver(userId, request);

        return ResponseEntity.ok().build();
    }
}
