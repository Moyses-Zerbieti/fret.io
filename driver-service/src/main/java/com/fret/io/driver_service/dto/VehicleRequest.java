package com.fret.io.driver_service.dto;

import com.fret.io.driver_service.model.TypeVehicle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class VehicleRequest {

    @Pattern(
            regexp =  "^[A-Z]{3}-?(\\d{4}|\\d[A-Z]\\d{2})$",
            message = "Informe uma placa válida"
    )
    @Size(min = 7, max = 8)
    @NotBlank(message = "Informe a placa do veículo para efetuar o cadastro")
    private String plate;

    @NotNull(message = "Descreva o tipo do veículo para efetuar o cadastro")
    private TypeVehicle typeVehicle;

    @NotBlank(message = "Informe a marca do veículo para efetuar o cadastro")
    private String brand;

    @NotBlank(message = "Infome o modelo do veículo para efetuar o cadastro")
    private String model;

    @NotNull(message = "Informe o ano de fabricação do veículo para efetuar o cadastro")
    private Integer vehicleYear;

    @NotNull(message = "Informe a capacidade em kilogramas do veículo para efeturar o cadastro")
    private BigDecimal capacityKg;

    @NotNull(message = "Informe a capacidade em metros cúbicos do veículo para efetuar o cadastro")
    private BigDecimal capacityM3;

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
}
