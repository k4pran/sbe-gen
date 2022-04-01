package com.company;

import com.company.custom.ModelYear;

import java.util.List;

public interface PojoInterface {

    long serialNumber();

    ModelYear modelYear();

    boolean available();

    String code();

    int someNumbers();

    String vehicleCode();

    String extras();

    String discountedModel();

    String engine();

    String manufacturer();

    String model();

    String activationCode();

    List<String> fuelFigures();

    List<String> performanceFigures();
}
