package com.safetynet.alerts.presentation.model;

import com.safetynet.alerts.presentation.model.Firestation;
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
    public void personShouldHaveGettersForAllVars() {

        //Preparation
        //Covered in BeforeEach

        //Method & Verification
        assertEquals("123 Address", firestation.getAddress());
        assertEquals(1, firestation.getStation());

    }

    @Test
    public void personShouldHaveSettersForAllVars() {

        //Preparation
        //Covered in BeforeEach

        //Method
        firestation.setAddress("123 Street Two");
        firestation.setStation(2);

        //Verification
        assertEquals("123 Street Two", firestation.getAddress());
        assertEquals(2, firestation.getStation());
    }

}
