package com.fret.io.driver_service.service;

import com.fret.io.driver_service.dto.VehicleRequest;
import com.fret.io.driver_service.dto.VehicleResponse;
import com.fret.io.driver_service.exception.DriverNotFoundException;
import com.fret.io.driver_service.exception.PlateAlreadyExistsException;
import com.fret.io.driver_service.exception.VehicleNotFoundException;
import com.fret.io.driver_service.model.Driver;
import com.fret.io.driver_service.model.StatusVehicle;
import com.fret.io.driver_service.model.Vehicle;
import com.fret.io.driver_service.repository.DriverRepository;
import com.fret.io.driver_service.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;

    public VehicleService(VehicleRepository vehicleRepository, DriverRepository driverRepository) {
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
    }

    private String normalizePlate(String plate){
        return plate
                .replace("-", "")
                .trim()
                .toUpperCase();
    }

    public void registerVehicle(UUID userId, VehicleRequest request){
        Driver driver = driverRepository.findByUserId(userId)
                .orElseThrow(()-> new DriverNotFoundException(userId));

        String plate = normalizePlate(request.getPlate());

        if(vehicleRepository.existsByPlate(plate)){
            throw new PlateAlreadyExistsException();
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setDriverId(driver);
        vehicle.setPlate(plate);
        vehicle.setTypeVehicle(request.getTypeVehicle());
        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setVehicleYear(request.getVehicleYear());
        vehicle.setCapacityKg(request.getCapacityKg());
        vehicle.setCapacityM3(request.getCapacityM3());
        vehicle.setStatusVehicle(StatusVehicle.DISPONIVEL);

        vehicleRepository.save(vehicle);

    }

    public VehicleResponse findVehicle(UUID vehicleId, UUID userId){

        Driver driver = driverRepository.findByUserId(userId)
                .orElseThrow(()-> new DriverNotFoundException(userId));

        Vehicle vehicle = vehicleRepository.findByIdAndDriverId_id(vehicleId, driver.getId())
                .orElseThrow(()-> new VehicleNotFoundException(vehicleId));

        VehicleResponse response = new VehicleResponse();
        response.setId(vehicle.getId());
        response.setPlate(vehicle.getPlate());
        response.setTypeVehicle(vehicle.getTypeVehicle());
        response.setBrand(vehicle.getBrand());
        response.setModel(vehicle.getModel());
        response.setVehicleYear(vehicle.getVehicleYear());
        response.setCapacityKg(vehicle.getCapacityKg());
        response.setCapacityM3(vehicle.getCapacityM3());
        response.setStatusVehicle(vehicle.getStatusVehicle());
        response.setCreatedAt(vehicle.getCreatedAt());

        return response;
    }

}
