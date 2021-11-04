package com.safetynet.alerts.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FirestationTest {

    private static Firestation firestation;

    @BeforeEach
    private void setUp() {
        firestation = new Firestation("123 Address", 1);
    }

    @Test
    public void personShouldHaveGettersForAllFields() {

        //Preparation
        //Covered in BeforeEach

        //Method & Verification
        assertEquals(firestation.getAddress(),"123 Address");
        assertEquals(firestation.getStation(),1);

    }

    @Test
    public void personShouldHaveSettersForAllFields() {

        //Preparation
        //Covered in BeforeEach

        //Method
        firestation.setAddress("123 Street Two");
        firestation.setStation(2);

        //Verification
        assertEquals(firestation.getAddress(),"123 Street Two");
        assertEquals(firestation.getStation(),2);
    }

}
