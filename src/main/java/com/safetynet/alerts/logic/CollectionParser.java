package com.safetynet.alerts.logic;

import com.safetynet.alerts.presentation.model.Firestation;

import java.util.ArrayList;

public class CollectionParser {

    public String[] getAddressesFromFirestationMappings(Firestation[] firestations){
        ArrayList<String> addressesList = new ArrayList<String>();
        for (Firestation firestation : firestations){
            if (!addressesList.contains(firestation.getAddress())) {
                addressesList.add(firestation.getAddress());
            }
        }
        String[] addresses = new String[addressesList.size()];
        int index = 0;
        for (String address : addressesList) {
            addresses[index] = address;
            index++;
        }
        return addresses;
    }
}
