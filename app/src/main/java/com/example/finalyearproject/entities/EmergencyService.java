package com.example.finalyearproject.entities;

public class EmergencyService {

    private String id;
    private String country;
    private String ambulanceNumber;
    private String fireNumber;
    private String policeNumber;

    public EmergencyService(String id, String country, String ambulanceNumber, String fireNumber, String policeNumber) {
        this.id = id;
        this.country = country;
        this.ambulanceNumber = ambulanceNumber;
        this.fireNumber = fireNumber;
        this.policeNumber = policeNumber;
    }

    public String getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public String getAmbulanceNumber() {
        return ambulanceNumber;
    }

    public String getFireNumber() {
        return fireNumber;
    }

    public String getPoliceNumber() {
        return policeNumber;
    }
}
