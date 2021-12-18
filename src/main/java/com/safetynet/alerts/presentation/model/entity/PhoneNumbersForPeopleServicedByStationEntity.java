package com.safetynet.alerts.presentation.model.entity;

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
