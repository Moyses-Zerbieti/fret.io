package com.fret.io.driver_service.repository;

import com.fret.io.driver_service.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID>{

    boolean existsByPlate(String plate);

    List<Vehicle> findAllByDriverId_id(UUID driverId);

    Optional<Vehicle> findByIdAndDriverId_id(UUID vehicleId, UUID driverId);

    Optional<Vehicle> findById(UUID vehicle);

}
