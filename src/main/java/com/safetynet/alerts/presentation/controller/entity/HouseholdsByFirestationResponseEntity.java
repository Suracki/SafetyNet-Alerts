package com.safetynet.alerts.presentation.controller.entity;

import com.safetynet.alerts.presentation.model.*;

import java.util.ArrayList;

public class HouseholdsByFirestationResponseEntity {

    private ArrayList<FirestationCoverage> firestations;

    public HouseholdsByFirestationResponseEntity() {
        firestations = new ArrayList<>();
    }
    public void addStation(Firestation firestation, ArrayList<HouseholdEntity> households) {
        if (firestations.size() == 0) {
            firestations.add(new FirestationCoverage(firestation, households));
        }
        else {
            boolean added = false;
            for (FirestationCoverage coverage : firestations) {
                if (coverage.getStation() == firestation.getStation()) {
                    coverage.addHousehold(households);
                    added = true;
                }
            }
            if (!added) {
                firestations.add(new FirestationCoverage(firestation, households));
            }
        }
    }

    private class FirestationCoverage {

        private int station;
        private ArrayList<HouseholdEntity> households;

        public FirestationCoverage(Firestation firestation, ArrayList<HouseholdEntity> households) {
            station = firestation.getStation();
            this.households = households;
        }

        public void addHousehold(ArrayList<HouseholdEntity> household) {
            households.addAll(household);
        }

        public int getStation() {
            return station;
        }
    }
}
