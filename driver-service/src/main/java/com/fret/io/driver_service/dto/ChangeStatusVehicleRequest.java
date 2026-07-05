package com.fret.io.driver_service.dto;

import com.fret.io.driver_service.model.StatusVehicle;
import jakarta.validation.constraints.NotNull;

public class ChangeStatusVehicleRequest {

    @NotNull (message = "Preencha o campo do status para concluir")
    private StatusVehicle statusVehicle;

    public StatusVehicle getStatusVehicle() {
        return statusVehicle;
    }

    public void setStatusVehicle(StatusVehicle statusVehicle) {
        this.statusVehicle = statusVehicle;
    }
}
