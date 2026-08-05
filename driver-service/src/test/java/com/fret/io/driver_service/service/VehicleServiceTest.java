package com.fret.io.driver_service.service;

import com.fret.io.driver_service.dto.UpdateStatusVehicleRequest;
import com.fret.io.driver_service.dto.VehicleRequest;
import com.fret.io.driver_service.dto.VehicleResponse;
import com.fret.io.driver_service.dto.VehicleResponseByPlate;
import com.fret.io.driver_service.exception.DriverNotFoundException;
import com.fret.io.driver_service.exception.PlateAlreadyExistsException;
import com.fret.io.driver_service.exception.VehicleNotFoundByPlateException;
import com.fret.io.driver_service.exception.VehicleNotFoundException;
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
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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

    private Vehicle createVehicle(Driver driver){
        Vehicle vehicle = new Vehicle();

        vehicle.setId(UUID.randomUUID());
        vehicle.setDriverId(driver);
        vehicle.setPlate("ABC1234");
        vehicle.setTypeVehicle(TypeVehicle.VAN);
        vehicle.setBrand("Mercedes");
        vehicle.setModel("Sprinter");
        vehicle.setVehicleYear(2022);
        vehicle.setCapacityKg(BigDecimal.valueOf(1500));
        vehicle.setCapacityM3(BigDecimal.valueOf(12.5));
        vehicle.setStatusVehicle(StatusVehicle.DISPONIVEL);
        vehicle.setCreatedAt(LocalDateTime.now());

        return vehicle;
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

    @Test
    void shouldReturnAllVehiclesByDriverUserIdTest(){
        Driver driver = createUserDriver();

        when(driverRepository.findByUserId(driver.getUserId()))
                .thenReturn(Optional.of(driver));

        Vehicle vehicle = createVehicle(driver);

        when(vehicleRepository.findAllByDriverId_id(driver.getId()))
                .thenReturn(List.of(vehicle));

        List<VehicleResponse> responses = service.listAllVehicleByDriver(driver.getUserId());

        verify(driverRepository).findByUserId(driver.getUserId());
        verify(vehicleRepository).findAllByDriverId_id(driver.getId());

        assertThat(responses).isNotNull();
    }

    @Test
    void shouldThrowDriverNotFoundExceptionWhenSearchAllVehicleByDriverTest(){
        Driver driver = createUserDriver();

        when(driverRepository.findByUserId(driver.getUserId()))
                .thenReturn(Optional.empty());

        assertThrows(DriverNotFoundException.class, ()->{
            service.listAllVehicleByDriver(driver.getUserId());
        });

        verify(driverRepository).findByUserId(driver.getUserId());
        verify(vehicleRepository, never()).findAllByDriverId_id(Mockito.any());
    }

    @Test
    void shouldReturnEmptyListWhenDriverHasNoVehiclesTest(){
        Driver driver = createUserDriver();

        when(driverRepository.findByUserId(driver.getUserId()))
                .thenReturn(Optional.of(driver));

        when(vehicleRepository.findAllByDriverId_id(driver.getId()))
                .thenReturn(Collections.emptyList());

        List<VehicleResponse> responses =
        service.listAllVehicleByDriver(driver.getUserId());

        verify(driverRepository).findByUserId(driver.getUserId());
        verify(vehicleRepository).findAllByDriverId_id(driver.getId());

        assertThat(responses).isNotNull();
    }

    @Test
    void shouldUpdateStatusVehicleTest(){
        Driver driver = createUserDriver();

        when(driverRepository.findByUserId(driver.getUserId()))
                .thenReturn(Optional.of(driver));

        Vehicle vehicle = createVehicle(driver);

        when(vehicleRepository.findByIdAndDriverId_id(vehicle.getId(), driver.getId()))
                .thenReturn(Optional.of(vehicle));

        UpdateStatusVehicleRequest request = new UpdateStatusVehicleRequest();
        request.setStatusVehicle(StatusVehicle.INATIVO);

        service.UpdateStatusVehicle(vehicle.getId(), driver.getUserId(),request);

        verify(driverRepository).findByUserId(driver.getUserId());
        verify(vehicleRepository).findByIdAndDriverId_id(vehicle.getId(), driver.getId());
        verify(vehicleRepository).save(vehicle);

        assertEquals(StatusVehicle.INATIVO, vehicle.getStatusVehicle());
    }

    @Test
    void shouldThrowDriverNotFoundExceptionWhenDriverHasNoExistsTest(){
        Driver driver = createUserDriver();

        when(driverRepository.findByUserId(driver.getUserId()))
                .thenReturn(Optional.empty());

        Vehicle vehicle = new Vehicle();

        UpdateStatusVehicleRequest request = new UpdateStatusVehicleRequest();

        assertThrows(DriverNotFoundException.class, ()->{
            service.UpdateStatusVehicle(vehicle.getId(), driver.getUserId(), request);
        });

        verify(driverRepository).findByUserId(driver.getUserId());
        verify(vehicleRepository, never()).findByIdAndDriverId_id(Mockito.any(), Mockito.any());
        verify(vehicleRepository, never()).save(Mockito.any());
    }

    @Test
    void shouldThrowVehicleNotFoundExceptionTest(){
        Driver driver = createUserDriver();

        when(driverRepository.findByUserId(driver.getUserId()))
                .thenReturn(Optional.of(driver));

        Vehicle vehicle = createVehicle(driver);

        UpdateStatusVehicleRequest request = new UpdateStatusVehicleRequest();
        request.setStatusVehicle(StatusVehicle.EM_MANUTENCAO);

        when(vehicleRepository.findByIdAndDriverId_id(vehicle.getId(), driver.getId()))
                .thenReturn(Optional.empty());


        assertThrows(VehicleNotFoundException.class, ()->{
            service.UpdateStatusVehicle(vehicle.getId(), driver.getUserId(), request);
        });

        verify(driverRepository).findByUserId(driver.getUserId());
        verify(vehicleRepository).findByIdAndDriverId_id(vehicle.getId(), driver.getId());
        verify(vehicleRepository, never()).save(Mockito.any());
    }

    @Test
    void shouldReturnVehicleByPlateTest(){
        Driver driver = createUserDriver();

        when(driverRepository.findByUserId(driver.getUserId()))
                .thenReturn(Optional.of(driver));

        Vehicle vehicle = createVehicle(driver);

        String normalizedPlate = PlateValidator.validateAndNormalize(vehicle.getPlate());

        when(vehicleRepository.findByPlateAndDriverId_Id(normalizedPlate, driver.getId()))
                .thenReturn(Optional.of(vehicle));

        VehicleResponseByPlate response = service.findVehicleByPlate(normalizedPlate, driver.getUserId());

        verify(driverRepository).findByUserId(driver.getUserId());
        verify(vehicleRepository).findByPlateAndDriverId_Id(normalizedPlate, driver.getId());

        assertThat(response).isNotNull();
        assertEquals(normalizedPlate, response.getPlate());
        assertEquals(vehicle.getTypeVehicle(), response.getTypeVehicle());
        assertEquals(vehicle.getBrand(), response.getBrand());
        assertEquals(vehicle.getModel(), response.getModel());
        assertEquals(vehicle.getVehicleYear(), response.getVehicleYear());
        assertEquals(vehicle.getCapacityKg(), response.getCapacityKg());
        assertEquals( vehicle.getCapacityM3(), response.getCapacityM3());
        assertEquals(vehicle.getStatusVehicle(), response.getStatusVehicle());
    }

    @Test
    void shouldReturnDriverNotFoundExceptionWhenDriverNotExistTest(){
        Driver driver = createUserDriver();

        when(driverRepository.findByUserId(driver.getUserId()))
                .thenReturn(Optional.empty());

        Vehicle vehicle = createVehicle(driver);

        String normalizedPlate = PlateValidator.validateAndNormalize(vehicle.getPlate());

        assertThrows(DriverNotFoundException.class, ()->{
            service.findVehicleByPlate(normalizedPlate,driver.getUserId());
        });

        verify(driverRepository).findByUserId(driver.getUserId());
        verify(vehicleRepository, never()).findByPlateAndDriverId_Id(normalizedPlate,driver.getId());
    }

    @Test
    void shouldThrowVehicleNotFoundByPlateExceptionTest(){
        Driver driver = createUserDriver();

        when(driverRepository.findByUserId(driver.getUserId()))
                .thenReturn(Optional.of(driver));

        Vehicle vehicle = createVehicle(driver);

        String normalizedPlate = PlateValidator.validateAndNormalize(vehicle.getPlate());

        when(vehicleRepository.findByPlateAndDriverId_Id(normalizedPlate, driver.getId()))
                .thenReturn(Optional.empty());

        assertThrows(VehicleNotFoundByPlateException.class, ()->{
            service.findVehicleByPlate(normalizedPlate, driver.getUserId());
        });

        verify(driverRepository).findByUserId(driver.getUserId());
        verify(vehicleRepository).findByPlateAndDriverId_Id(normalizedPlate, driver.getId());
    }
}
