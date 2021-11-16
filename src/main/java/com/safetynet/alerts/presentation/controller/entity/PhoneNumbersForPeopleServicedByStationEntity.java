package com.safetynet.alerts.presentation.controller.entity;

import java.util.ArrayList;

public class PhoneNumbersForPeopleServicedByStationEntity {

    private ArrayList<String> phoneNumber;

    public PhoneNumbersForPeopleServicedByStationEntity() {
        phoneNumber = new ArrayList<>();
    }

    public void addPhone(String number) {
        phoneNumber.add(number);
    }

}
