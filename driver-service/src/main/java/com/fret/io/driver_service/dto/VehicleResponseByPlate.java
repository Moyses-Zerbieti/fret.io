package com.fret.io.driver_service.dto;

import com.fret.io.driver_service.model.StatusVehicle;
import com.fret.io.driver_service.model.TypeVehicle;

import java.math.BigDecimal;

public class VehicleResponseByPlate {

    private String plate;
    private TypeVehicle typeVehicle;
    private String brand;
    private String model;
    private Integer vehicleYear;
    private BigDecimal capacityKg;
    private BigDecimal capacityM3;
    private StatusVehicle statusVehicle;

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public TypeVehicle getTypeVehicle() {
        return typeVehicle;
    }

    public void setTypeVehicle(TypeVehicle typeVehicle) {
        this.typeVehicle = typeVehicle;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(Integer vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    public BigDecimal getCapacityKg() {
        return capacityKg;
    }

    public void setCapacityKg(BigDecimal capacityKg) {
        this.capacityKg = capacityKg;
    }

    public BigDecimal getCapacityM3() {
        return capacityM3;
    }

    public void setCapacityM3(BigDecimal capacityM3) {
        this.capacityM3 = capacityM3;
    }

    public StatusVehicle getStatusVehicle() {
        return statusVehicle;
    }

    public void setStatusVehicle(StatusVehicle statusVehicle) {
        this.statusVehicle = statusVehicle;
    }
}
