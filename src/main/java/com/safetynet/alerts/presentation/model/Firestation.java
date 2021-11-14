package com.safetynet.alerts.presentation.model;

public class Firestation {

    private String address;
    private int station;

    public Firestation(String address, int station) {
        this.address = address;
        this.station = station;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStation() {
        return station;
    }

    public void setStation(int station) {
        this.station = station;
    }

    public void update(Firestation newFirestation) {
        this.address = newFirestation.getAddress();
        this.station = newFirestation.getStation();
    }

    @Override
    public String toString() {
        String output = "{\"address\":\"" + address +
                "\", \"station\":\"" + station + "\"}";
        return output;
    }
}
