package com.safetynet.alerts.presentation.model;

import com.safetynet.alerts.presentation.model.Person;
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
    public void personShouldHaveGettersForAllVars() {

        //Preparation
        //Covered in BeforeEach

        //Method & Verification
        assertEquals("First", person.getFirstName());
        assertEquals("Last", person.getLastName());
        assertEquals("123 Street", person.getAddress());
        assertEquals("City", person.getCity());
        assertEquals("123456", person.getZip());
        assertEquals("555-1234", person.getPhone());
        assertEquals("name@home.com", person.getEmail());

    }

    @Test
    public void personShouldHaveSettersForAllVars() {

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
        assertEquals("Newfirst", person.getFirstName());
        assertEquals("Newlast", person.getLastName());
        assertEquals("123 Street Two", person.getAddress());
        assertEquals("New City", person.getCity());
        assertEquals("567890", person.getZip());
        assertEquals("555-5678", person.getPhone());
        assertEquals("new@home.com", person.getEmail());

    }

}
