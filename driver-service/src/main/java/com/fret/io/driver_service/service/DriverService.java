package com.fret.io.driver_service.service;

import com.fret.io.driver_service.exception.CnhAlreadyRegisteredException;
import com.fret.io.driver_service.validation.validator.CnhValidator;
import com.fret.io.driver_service.dto.CompleteDriverRegistrationRequest;
import com.fret.io.driver_service.dto.DriverResponse;
import com.fret.io.driver_service.dto.UpdateDriverRequest;
import com.fret.io.driver_service.exception.DriverNotFoundException;
import com.fret.io.driver_service.exception.DriverRegistrationAlreadyCompleteException;
import com.fret.io.driver_service.model.Driver;
import com.fret.io.driver_service.repository.DriverRepository;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
        driver.setCnhNumber(CnhValidator.validateAndNormalize(request.getCnhNumber()));
        driver.setCnhCategory(request.getCnhCategory());
        driver.setCnhExpiresAt(request.getCnhExpiresAt());

        driverRepository.save(driver);
    }

    @Transactional
    public void updateDriver(UUID userId, UpdateDriverRequest request){
        Driver driver = driverRepository.findByUserId(userId)
                .orElseThrow(()-> new DriverNotFoundException(userId));

        if (!StringUtils.hasText(request.getFullName())
                && !StringUtils.hasText(request.getPhone())
                && !StringUtils.hasText(request.getCnhNumber())) {
            throw new ValidationException("Informe ao menos um campo para atualização dos dados");
        }
        if (StringUtils.hasText(request.getFullName())){
            driver.setFullName(request.getFullName().trim());
        }
        if (StringUtils.hasText(request.getPhone())) {
            driver.setPhoneNumber(request.getPhone().trim());
        }
        if (StringUtils.hasText(request.getCnhNumber())){

            String cnh = CnhValidator.validateAndNormalize(request.getCnhNumber());
            driverRepository.findByCnhNumber(cnh)
                    .filter(found -> !found.getId().equals(driver.getId()))
                    .ifPresent(found -> {
                        throw new CnhAlreadyRegisteredException();
                    });
        driver.setCnhNumber(cnh);
        }
    }

    public DriverResponse findDriver (String cpf, UUID userId){
        Driver driver = driverRepository.findByCpfAndUserId(cpf, userId)
                .orElseThrow(()-> new DriverNotFoundException(userId));

        DriverResponse response = new DriverResponse();

        response.setFullName(driver.getFullName());
        response.setCpf(driver.getCpf());
        response.setEmail(driver.getEmail());
        response.setPhoneNumber(driver.getPhoneNumber());
        response.setCnhNumber(driver.getCnhNumber());
        response.setCnhCategory(driver.getCnhCategory());
        response.setCnhExpiresAt(driver.getCnhExpiresAt());
        response.setAvgRating(driver.getAvgRating());

        return response;
    }
}


