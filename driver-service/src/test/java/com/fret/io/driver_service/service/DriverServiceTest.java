package com.fret.io.driver_service.service;

import com.fret.io.driver_service.dto.CompleteDriverRegistrationRequest;
import com.fret.io.driver_service.exception.DriverNotFoundException;
import com.fret.io.driver_service.exception.DriverRegistrationAlreadyCompleteException;
import com.fret.io.driver_service.model.CnhCategory;
import com.fret.io.driver_service.model.Driver;
import com.fret.io.driver_service.repository.DriverRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    private CompleteDriverRegistrationRequest createRequest(){
        CompleteDriverRegistrationRequest request = new CompleteDriverRegistrationRequest();
        request.setFullName("Moyses Zerbieti");
        request.setPhoneNumber("+55 11987654321");
        request.setCnhNumber("12345678901");
        request.setCnhCategory(CnhCategory.CATEGORIA_A);
        request.setCnhExpiresAt(LocalDate.of(2030,12,30));

        return request;
    }

    @Test
    void shouldCompleteRegistrationSuccessfullyTest(){
        Driver driver = createUserDriver();

        when(repository.findByUserId(driver.getUserId()))
                .thenReturn(Optional.of(driver));

        CompleteDriverRegistrationRequest request = createRequest();

        driverService.completeRegistration(driver.getUserId(), createRequest());

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
                .thenThrow(DriverNotFoundException.class);

        assertThrows(DriverNotFoundException.class, () ->{
            driverService.completeRegistration(driver.getUserId(), createRequest());
        });

        verify(repository, never()).save(Mockito.any());
    }

    @Test
    void shouldThrowDriverRegistrationAlreadyCompleteTest(){
        Driver driver = createUserDriver();
        driver.setCnhNumber("12345678901");

        when(repository.findByUserId(driver.getUserId()))
                .thenReturn(Optional.of(driver));

        assertThrows(DriverRegistrationAlreadyCompleteException.class, () ->{
            driverService.completeRegistration(driver.getUserId(), createRequest());
        });

        verify(repository, never()).save(Mockito.any());
    }
}
