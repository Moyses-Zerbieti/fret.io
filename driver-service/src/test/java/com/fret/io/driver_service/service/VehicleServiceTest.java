package com.fret.io.driver_service.service;

import com.fret.io.driver_service.dto.VehicleRequest;
import com.fret.io.driver_service.exception.DriverNotFoundException;
import com.fret.io.driver_service.exception.PlateAlreadyExistsException;
import com.fret.io.driver_service.model.Driver;
import com.fret.io.driver_service.model.StatusVehicle;
import com.fret.io.driver_service.model.TypeVehicle;
import com.fret.io.driver_service.model.Vehicle;
import com.fret.io.driver_service.repository.DriverRepository;
import com.fret.io.driver_service.repository.VehicleRepository;
import com.fret.io.driver_service.validation.validator.PlateValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @InjectMocks
    VehicleService service;
    @Mock
    DriverRepository driverRepository;
    @Mock
    VehicleRepository vehicleRepository;

    private Driver createUserDriver(){
        Driver driver = new Driver();
        driver.setId(UUID.randomUUID());
        driver.setUserId(UUID.randomUUID());
        driver.setEmail("test@gmail.com");
        driver.setCpf("12345678901");
        driver.setAvgRating(BigDecimal.ZERO);

        return driver;
    }

    private VehicleRequest createVehicleRequest(){
        VehicleRequest request = new VehicleRequest();
        request.setPlate("abc1234");
        request.setTypeVehicle(TypeVehicle.CAMINHAO_BAU);
        request.setBrand("Volvo");
        request.setModel("Vm 260");
        request.setVehicleYear(2020);
        request.setCapacityKg(new BigDecimal(3000));
        request.setCapacityM3(new BigDecimal(20));

        return request;
    }

    @Test
    void shouldRegisterNewVehicleSuccessfullyTest(){
        Driver driver = createUserDriver();

        when(driverRepository.findByUserId(driver.getUserId()))
                .thenReturn(Optional.of(driver));

        VehicleRequest request = createVehicleRequest();

        String normalizePlate = PlateValidator.validateAndNormalize(request.getPlate());

        when(vehicleRepository.existsByPlate(normalizePlate))
                .thenReturn(false);

        ArgumentCaptor<Vehicle> vehicleCaptor =
                ArgumentCaptor.forClass(Vehicle.class);

        service.registerVehicle(driver.getUserId(), request);

        verify(driverRepository).findByUserId(driver.getUserId());
        verify(vehicleRepository).existsByPlate(normalizePlate);
        verify(vehicleRepository).save(vehicleCaptor.capture());

        Vehicle savedVehicle = vehicleCaptor.getValue();

        assertEquals(driver, savedVehicle.getDriverId());
        assertEquals(normalizePlate, savedVehicle.getPlate());
        assertEquals(request.getTypeVehicle(), savedVehicle.getTypeVehicle());
        assertEquals(request.getBrand(), savedVehicle.getBrand());
        assertEquals(request.getModel(), savedVehicle.getModel());
        assertEquals(request.getVehicleYear(), savedVehicle.getVehicleYear());
        assertEquals(request.getCapacityKg(), savedVehicle.getCapacityKg());
        assertEquals(request.getCapacityM3(), savedVehicle.getCapacityM3());
        assertEquals(StatusVehicle.DISPONIVEL, savedVehicle.getStatusVehicle());
    }

    @Test
    void shouldThrowDriverNotFoundExceptionTest(){
        Driver driver = createUserDriver();

        VehicleRequest request = createVehicleRequest();

        when(driverRepository.findByUserId(driver.getUserId()))
                .thenReturn(Optional.empty());

        assertThrows(DriverNotFoundException.class, ()->{
            service.registerVehicle(driver.getUserId(), request);
        });

        verify(driverRepository).findByUserId(driver.getUserId());
        verify(vehicleRepository, never()).existsByPlate(Mockito.any());
        verify(vehicleRepository, never()).save(Mockito.any());
    }

    @Test
    void shouldThrowPlateAlreadyExistsExceptionTest(){
        Driver driver = createUserDriver();

        when(driverRepository.findByUserId(driver.getUserId()))
                .thenReturn(Optional.of(driver));

        VehicleRequest request = createVehicleRequest();
        String normalizePlate = PlateValidator.validateAndNormalize(request.getPlate());

        when(vehicleRepository.existsByPlate(normalizePlate))
                .thenReturn(true);

        assertThrows(PlateAlreadyExistsException.class, ()->{
            service.registerVehicle(driver.getUserId(), request);
        });

        verify(driverRepository).findByUserId(driver.getUserId());
        verify(vehicleRepository).existsByPlate(normalizePlate);
        verify(vehicleRepository, never()).save(Mockito.any());
    }

}
