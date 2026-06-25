package com.fret.io.driver_service.service;

import com.fret.io.driver_service.dto.VehicleRequest;
import com.fret.io.driver_service.exception.DriverNotFoundException;
import com.fret.io.driver_service.exception.PlateAlreadyExistsException;
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

}
