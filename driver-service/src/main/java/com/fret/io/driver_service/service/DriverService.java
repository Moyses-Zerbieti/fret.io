package com.fret.io.driver_service.service;

import com.fret.io.driver_service.dto.CompleteDriverRegistrationRequest;
import com.fret.io.driver_service.exception.DriverNotFoundException;
import com.fret.io.driver_service.exception.DriverRegistrationAlreadyCompleteException;
import com.fret.io.driver_service.model.Driver;
import com.fret.io.driver_service.repository.DriverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Transactional
    public void completeRegistration(UUID userId, CompleteDriverRegistrationRequest request){

        Driver driver = driverRepository.findByUserId(userId)
                .orElseThrow(()-> new DriverNotFoundException(userId));

        if (driver.getCnhNumber() != null){
            throw new DriverRegistrationAlreadyCompleteException();
        }
        driver.setFullName(request.getFullName());
        driver.setPhoneNumber(request.getPhoneNumber());
        driver.setCnhNumber(request.getCnhNumber());
        driver.setCnhCategory(request.getCnhCategory());
        driver.setCnhExpiresAt(request.getCnhExpiresAt());

        driverRepository.save(driver);
    }
}


