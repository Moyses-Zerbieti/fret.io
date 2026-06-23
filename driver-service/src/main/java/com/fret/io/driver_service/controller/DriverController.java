package com.fret.io.driver_service.controller;

import com.fret.io.driver_service.dto.CompleteDriverRegistrationRequest;
import com.fret.io.driver_service.dto.UpdateDriverNameRequest;
import com.fret.io.driver_service.dto.UpdateDriverPhoneRequest;
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

    @PatchMapping("/name/change")
    public ResponseEntity<Void> changeNameDriver
            (@RequestHeader("X-User-Id") UUID userId,
             @Valid @RequestBody UpdateDriverNameRequest request){

        driverService.updateDriverName(userId, request);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/phone/change")
    public ResponseEntity<Void> changePhoneDriver
            (@RequestHeader("X-User-Id") UUID userId,
             @Valid @RequestBody UpdateDriverPhoneRequest request){

        driverService.updateDriverPhone(userId, request);

        return ResponseEntity.ok().build();
    }
}
