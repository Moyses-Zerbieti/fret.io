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
import com.fret.io.driver_service.model.Vehicle;
import com.fret.io.driver_service.repository.DriverRepository;
import com.fret.io.driver_service.repository.VehicleRepository;
import com.fret.io.driver_service.validation.validator.PlateValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;

    public VehicleService(VehicleRepository vehicleRepository, DriverRepository driverRepository) {
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
    }

    @Transactional
    public void registerVehicle(UUID userId, VehicleRequest request){
        Driver driver = driverRepository.findByUserId(userId)
                .orElseThrow(()-> new DriverNotFoundException(userId));

        String normalizedPlate = PlateValidator.validateAndNormalize(request.getPlate());

        if(vehicleRepository.existsByPlate(normalizedPlate)){
            throw new PlateAlreadyExistsException();
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setDriverId(driver);
        vehicle.setPlate(normalizedPlate);
        vehicle.setTypeVehicle(request.getTypeVehicle());
        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setVehicleYear(request.getVehicleYear());
        vehicle.setCapacityKg(request.getCapacityKg());
        vehicle.setCapacityM3(request.getCapacityM3());
        vehicle.setStatusVehicle(StatusVehicle.DISPONIVEL);

        vehicleRepository.save(vehicle);
    }

    public List<VehicleResponse> listAllVehicleByDriver(UUID userId){
        Driver driver = driverRepository.findByUserId(userId)
                .orElseThrow(()-> new DriverNotFoundException(userId));

        List<Vehicle> listVehicles = vehicleRepository.findAllByDriverId_id(driver.getId());

        List<VehicleResponse> vehicleResponses = new ArrayList<>();

        for (Vehicle vehicleFound : listVehicles){

            VehicleResponse vehicleResponse = new VehicleResponse();

            vehicleResponse.setId(vehicleFound.getId());
            vehicleResponse.setPlate(vehicleFound.getPlate());
            vehicleResponse.setTypeVehicle(vehicleFound.getTypeVehicle());
            vehicleResponse.setBrand(vehicleFound.getBrand());
            vehicleResponse.setModel(vehicleFound.getModel());
            vehicleResponse.setVehicleYear(vehicleFound.getVehicleYear());
            vehicleResponse.setCapacityKg(vehicleFound.getCapacityKg());
            vehicleResponse.setCapacityM3(vehicleFound.getCapacityM3());
            vehicleResponse.setStatusVehicle(vehicleFound.getStatusVehicle());
            vehicleResponse.setCreatedAt(vehicleFound.getCreatedAt());

           vehicleResponses.add(vehicleResponse);
        }

        return vehicleResponses;

    }

    @Transactional
    public void UpdateStatusVehicle(UUID vehicleId, UUID userId, UpdateStatusVehicleRequest request){
        Driver driver = driverRepository.findByUserId(userId)
                .orElseThrow(()-> new DriverNotFoundException(userId));

        Vehicle vehicle = vehicleRepository.findByIdAndDriverId_id(vehicleId, driver.getId())
                .orElseThrow(()-> new VehicleNotFoundException(vehicleId));

        vehicle.setStatusVehicle(request.getStatusVehicle());

        vehicleRepository.save(vehicle);
    }

    public VehicleResponseByPlate findVehicleByPlate(String plate, UUID userId){
        Driver driver = driverRepository.findByUserId(userId)
                .orElseThrow(()-> new DriverNotFoundException(userId));

        String normalizedPlate = PlateValidator.validateAndNormalize(plate);

        Vehicle vehicle = vehicleRepository.findByPlateAndDriverId_Id(normalizedPlate,driver.getId())
                .orElseThrow(()->new VehicleNotFoundByPlateException(normalizedPlate));

        VehicleResponseByPlate response = new VehicleResponseByPlate();

        response.setPlate(vehicle.getPlate());
        response.setTypeVehicle(vehicle.getTypeVehicle());
        response.setBrand(vehicle.getBrand());
        response.setModel(vehicle.getModel());
        response.setVehicleYear(vehicle.getVehicleYear());
        response.setCapacityKg(vehicle.getCapacityKg());
        response.setCapacityM3(vehicle.getCapacityM3());
        response.setStatusVehicle(vehicle.getStatusVehicle());

        return response;
    }

}