package com.company.pojo;

import java.util.List;
import com.company.PojoBase;
import com.company.PojoInterface;
import com.company.custom.ModelYear;


public class Car extends PojoBase implements PojoInterface {

  private long serialNumber;
  private ModelYear modelYear;
  private boolean available;
  private String code;
  private int someNumbers;
  private String vehicleCode;
  private String extras;
  private String discountedModel;
  private String engine;
  private String manufacturer;
  private String model;
  private String activationCode;
  private List<String> fuelFigures;
  private List<String> performanceFigures;

  @Override
  public long serialNumber() {
    return this.serialNumber;
  }

  public void serialNumber(long serialNumber) {
    this.serialNumber = serialNumber;
  }

  @Override
  public ModelYear modelYear() {
    return this.modelYear;
  }

  public void modelYear(ModelYear modelYear) {
    this.modelYear = modelYear;
  }

  @Override
  public boolean available() {
    return this.available;
  }

  public void available(boolean available) {
    this.available = available;
  }

  @Override
  public String code() {
    return this.code;
  }

  public void code(String code) {
    this.code = code;
  }

  @Override
  public int someNumbers() {
    return this.someNumbers;
  }

  public void someNumbers(int someNumbers) {
    this.someNumbers = someNumbers;
  }

  @Override
  public String vehicleCode() {
    return this.vehicleCode;
  }

  public void vehicleCode(String vehicleCode) {
    this.vehicleCode = vehicleCode;
  }

  @Override
  public String extras() {
    return this.extras;
  }

  public void extras(String extras) {
    this.extras = extras;
  }

  @Override
  public String discountedModel() {
    return this.discountedModel;
  }

  public void discountedModel(String discountedModel) {
    this.discountedModel = discountedModel;
  }

  @Override
  public String engine() {
    return this.engine;
  }

  public void engine(String engine) {
    this.engine = engine;
  }

  @Override
  public String manufacturer() {
    return this.manufacturer;
  }

  public void manufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  @Override
  public String model() {
    return this.model;
  }

  public void model(String model) {
    this.model = model;
  }

  @Override
  public String activationCode() {
    return this.activationCode;
  }

  public void activationCode(String activationCode) {
    this.activationCode = activationCode;
  }

  @Override
  public List<String> fuelFigures() {
    return this.fuelFigures;
  }

  public void fuelFigures(List<String> fuelFigures) {
    this.fuelFigures = fuelFigures;
  }

  @Override
  public List<String> performanceFigures() {
    return this.performanceFigures;
  }

  public void performanceFigures(List<String> performanceFigures) {
    this.performanceFigures = performanceFigures;
  }

  public Car newInstance(long serialNumber,
      ModelYear modelYear,
      boolean available,
      String code,
      int someNumbers,
      String vehicleCode,
      String extras,
      String discountedModel,
      String engine,
      String manufacturer,
      String model,
      String activationCode,
      List<String> fuelFigures,
      List<String> performanceFigures) {
    this.serialNumber(serialNumber);
    this.modelYear(modelYear);
    this.available(available);
    this.code(code);
    this.someNumbers(someNumbers);
    this.vehicleCode(vehicleCode);
    this.extras(extras);
    this.discountedModel(discountedModel);
    this.engine(engine);
    this.manufacturer(manufacturer);
    this.model(model);
    this.activationCode(activationCode);
    this.fuelFigures(fuelFigures);
    this.performanceFigures(performanceFigures);
  return this;
  }

  @Override
  public String toString() {
    return ""; // todo 
  }
}