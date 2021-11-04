package com.safetynet.alerts.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class PersonTest {

    private static Person person;

    @BeforeEach
    private void setUp() {
        person = new Person("First", "Last", "123 Street", "City",
                "123456", "555-1234", "name@home.com");
    }

    @Test
    public void personShouldHaveGettersForAllFields() {

        //Preparation
        //Covered in BeforeEach

        //Method & Verification
        assertEquals(person.getFirstName(),"First");
        assertEquals(person.getLastName(),"Last");
        assertEquals(person.getAddress(),"123 Street");
        assertEquals(person.getCity(),"City");
        assertEquals(person.getZip(),"123456");
        assertEquals(person.getPhone(),"555-1234");
        assertEquals(person.getEmail(),"name@home.com");

    }

    @Test
    public void personShouldHaveSettersForAllFields() {

        //Preparation
        //Covered in BeforeEach

        //Method
        person.setFirstName("Newfirst");
        person.setLastName("Newlast");
        person.setAddress("123 Street Two");
        person.setCity("New City");
        person.setZip("567890");
        person.setPhone("555-5678");
        person.setEmail("new@home.com");

        //Verification
        assertEquals(person.getFirstName(),"Newfirst");
        assertEquals(person.getLastName(),"Newlast");
        assertEquals(person.getAddress(),"123 Street Two");
        assertEquals(person.getCity(),"New City");
        assertEquals(person.getZip(),"567890");
        assertEquals(person.getPhone(),"555-5678");
        assertEquals(person.getEmail(),"new@home.com");

    }

}
