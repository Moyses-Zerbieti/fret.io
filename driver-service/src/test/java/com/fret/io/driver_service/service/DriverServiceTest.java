package com.fret.io.driver_service.service;

import com.fret.io.driver_service.dto.CompleteDriverRegistrationRequest;
import com.fret.io.driver_service.dto.UpdateDriverRequest;
import com.fret.io.driver_service.exception.CnhAlreadyRegisteredException;
import com.fret.io.driver_service.exception.DriverNotFoundException;
import com.fret.io.driver_service.exception.DriverRegistrationAlreadyCompleteException;
import com.fret.io.driver_service.model.CnhCategory;
import com.fret.io.driver_service.model.Driver;
import com.fret.io.driver_service.repository.DriverRepository;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DriverServiceTest {

    @InjectMocks
    DriverService driverService;

    @Mock
    DriverRepository repository;

    private Driver createUserDriver (){
      Driver driver = new Driver();
      driver.setId(UUID.randomUUID());
      driver.setUserId(UUID.randomUUID());
      driver.setEmail("test@gmail.com");
      driver.setCpf("12345678901");
      driver.setAvgRating(BigDecimal.ZERO);

      return driver;
    }

    private CompleteDriverRegistrationRequest completeRegistrationRequest(){
        CompleteDriverRegistrationRequest request = new CompleteDriverRegistrationRequest();
        request.setFullName("Moyses Zerbieti");
        request.setPhoneNumber("+55 11987654321");
        request.setCnhNumber("12345678901");
        request.setCnhCategory(CnhCategory.CATEGORIA_A);
        request.setCnhExpiresAt(LocalDate.of(2030,12,30));

        return request;
    }

    private UpdateDriverRequest createUpdateRequest(){
        UpdateDriverRequest request = new UpdateDriverRequest();
        request.setFullName("Moyses Zerbieti Martins");
        request.setPhone("+55 119876543210");
        request.setCnhNumber("12345678911");

        return request;
    }

    @Test
    void shouldCompleteRegistrationSuccessfullyTest(){
        Driver driver = createUserDriver();

        when(repository.findByUserId(driver.getUserId()))
                .thenReturn(Optional.of(driver));

        CompleteDriverRegistrationRequest request = completeRegistrationRequest();

        driverService.completeRegistration(driver.getUserId(), completeRegistrationRequest());

        assertEquals(request.getFullName(), driver.getFullName());
        assertEquals(request.getPhoneNumber(), driver.getPhoneNumber());
        assertEquals(request.getCnhNumber(), driver.getCnhNumber());
        assertEquals(request.getCnhCategory(), driver.getCnhCategory());
        assertEquals(request.getCnhExpiresAt(), driver.getCnhExpiresAt());

        verify(repository).save(driver);
    }

    @Test
    void shouldThrowDriverNotFoundExceptionTest(){
        Driver driver = createUserDriver();

        when(repository.findByUserId(driver.getUserId()))
                .thenReturn(Optional.empty());

        assertThrows(DriverNotFoundException.class, () ->{
            driverService.completeRegistration(driver.getUserId(), completeRegistrationRequest());
        });

        verify(repository).findByUserId(driver.getUserId());
        verify(repository, never()).save(Mockito.any());
    }

    @Test
    void shouldThrowDriverRegistrationAlreadyCompleteTest(){
        Driver driver = createUserDriver();
        driver.setCnhNumber("12345678901");

        when(repository.findByUserId(driver.getUserId()))
                .thenReturn(Optional.of(driver));

        assertThrows(DriverRegistrationAlreadyCompleteException.class, () ->{
            driverService.completeRegistration(driver.getUserId(), completeRegistrationRequest());
        });

        verify(repository, never()).save(Mockito.any());
    }

    @Test
    void shouldUpdateDriverTest(){
        Driver driver = createUserDriver();

        when(repository.findByUserId(driver.getUserId()))
                .thenReturn(Optional.of(driver));

        UpdateDriverRequest request = createUpdateRequest();

        when(repository.findByCnhNumber(request.getCnhNumber()))
                .thenReturn(Optional.of(driver));

        driverService.updateDriver(driver.getUserId(), request);

        assertEquals(request.getFullName(), driver.getFullName());
        assertEquals(request.getPhone(), driver.getPhoneNumber());
        assertEquals(request.getCnhNumber(), driver.getCnhNumber());

        verify(repository).findByUserId(driver.getUserId());
        verify(repository).findByCnhNumber(request.getCnhNumber());

    }

    @Test
    void shouldThrowDriverNotFoundException(){
        Driver driver = createUserDriver();

        when(repository.findByUserId(driver.getUserId()))
                .thenReturn(Optional.empty());

        UpdateDriverRequest request = createUpdateRequest();

        assertThrows(DriverNotFoundException.class, ()->{
            driverService.updateDriver(driver.getUserId(), request);
        });

        verify(repository).findByUserId(driver.getUserId());
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateRequestIsEmptyTest(){
        Driver driver = createUserDriver();

        when(repository.findByUserId(driver.getUserId()))
                .thenReturn(Optional.of(driver));

        UpdateDriverRequest request = new UpdateDriverRequest();

        assertThrows(ValidationException.class, ()->{
            driverService.updateDriver(driver.getUserId(), request);
        });

        verify(repository).findByUserId(driver.getUserId());
    }

    @Test
    void shouldThrowCnhAlreadyRegisteredException(){
        Driver driverToUpdate = createUserDriver();
        driverToUpdate.setCnhNumber("12345678901");

        Driver existingDriver = createUserDriver();
        existingDriver.setCnhNumber("10987654321");

        when(repository.findByUserId(driverToUpdate.getUserId()))
                .thenReturn(Optional.of(driverToUpdate));

        UpdateDriverRequest request = createUpdateRequest();
        request.setCnhNumber(existingDriver.getCnhNumber());

        when(repository.findByCnhNumber(request.getCnhNumber()))
                .thenReturn(Optional.of(existingDriver));

        assertThrows(CnhAlreadyRegisteredException.class, ()->{
            driverService.updateDriver(driverToUpdate.getUserId(),request);
        });

        verify(repository).findByUserId(driverToUpdate.getUserId());
        verify(repository).findByCnhNumber(request.getCnhNumber());

    }

}
