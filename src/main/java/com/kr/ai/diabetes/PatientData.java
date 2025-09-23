package com.kr.ai.diabetes;

public class PatientData {
    private int age;
    private double weight; // in kg
    private double height; // in meters
    private double bmi; // optional, can calculate if not provided
    private double glucose;
    private double bloodPressure;
    private double insulin;

    // getters and setters
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getBmi() { return bmi; }
    public void setBmi(double bmi) { this.bmi = bmi; }

    public double getGlucose() { return glucose; }
    public void setGlucose(double glucose) { this.glucose = glucose; }

    public double getBloodPressure() { return bloodPressure; }
    public void setBloodPressure(double bloodPressure) { this.bloodPressure = bloodPressure; }

    public double getInsulin() { return insulin; }
    public void setInsulin(double insulin) { this.insulin = insulin; }
}
