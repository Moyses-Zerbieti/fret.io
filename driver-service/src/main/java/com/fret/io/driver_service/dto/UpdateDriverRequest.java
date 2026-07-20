package com.fret.io.driver_service.dto;

import com.fret.io.driver_service.validation.annotation.ValidPhoneNumber;

public class UpdateDriverRequest {


    private String fullName;

    @ValidPhoneNumber
    private String phone;

    private String cnhNumber;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCnhNumber() {
        return cnhNumber;
    }

    public void setCnhNumber(String cnhNumber) {
        this.cnhNumber = cnhNumber;
    }
}
