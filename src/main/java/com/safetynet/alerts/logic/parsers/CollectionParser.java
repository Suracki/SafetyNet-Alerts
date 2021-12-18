package com.safetynet.alerts.logic.parsers;

import com.safetynet.alerts.presentation.model.Firestation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * CollectionParser is used to parse collections of elements from the model and return specific information from them
 */
@Service
public class CollectionParser {

    /**
     * Returns all unique addresses from a collection of Firestation mapping objects
     *
     * @param firestations array of Firestation mapping objects
     * @return String array of addresses
     */
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
