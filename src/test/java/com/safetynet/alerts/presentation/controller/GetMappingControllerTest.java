package com.safetynet.alerts.presentation.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(GetMappingController.class)
@WebAppConfiguration
@Import(GetControllerBeanSetup.class)
public class GetMappingControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    private void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    private String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
    private <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

    @Test
    public void firestationReturnsPeopleServicedByStationNumber() throws Exception {
        //Preparation
        String uri = "/firestation?stationNumber=1";
        String expectedResponse = "{\n" +
                "\"persons\": [\n" +
                "        {\"firstName\":\"Brian\",\"lastName\":\"Stelzer\",\"address\":\"947 E. Rose Dr\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-7784\"},\n" +
                "        {\"firstName\":\"Jamie\",\"lastName\":\"Peters\",\"address\":\"908 73rd St\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-7462\"},\n" +
                "        {\"firstName\":\"Kendrik\",\"lastName\":\"Stelzer\",\"address\":\"947 E. Rose Dr\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-7784\"},\n" +
                "        {\"firstName\":\"Peter\",\"lastName\":\"Duncan\",\"address\":\"644 Gershwin Cir\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-6512\"},\n" +
                "        {\"firstName\":\"Reginold\",\"lastName\":\"Walker\",\"address\":\"908 73rd St\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-8547\"},\n" +
                "        {\"firstName\":\"Shawna\",\"lastName\":\"Stelzer\",\"address\":\"947 E. Rose Dr\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-7784\"}\n" +
                "    ],\n" +
                "    \"adults\":[5],\"children\":[1]\n" +
                "}";


        //Method
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //Verification
        assertEquals(200, status);
        assertEquals(expectedResponse,mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void childAlertReturnsListOfChildrenAtAddress() throws Exception {
        //Preparation
        String uri = "/childAlert?address=1509 Culver St";
        String expectedResponse = "{\n" +
                "    \"children\": [\n" +
                "        {\"firstName\":\"Roger\",\"lastName\":\"Boyd\",\"age\":\"4\"},\n" +
                "        {\"firstName\":\"Tenley\",\"lastName\":\"Boyd\",\"age\":\"9\"}\n" +
                "    ],\n" +
                "    \"adults\": [\n" +
                "        {\"firstName\":\"Felicia\",\"lastName\":\"Boyd\"},\n" +
                "        {\"firstName\":\"Jacob\",\"lastName\":\"Boyd\"},\n" +
                "        {\"firstName\":\"John\",\"lastName\":\"Boyd\"}\n" +
                "    ]\n" +
                "}";


        //Method
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //Verification
        assertEquals(200, status);
        assertEquals(expectedResponse,mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void phoneAlertReturnsListOfPhoneNumbersForFirestationNumber() throws Exception {
        //Preparation
        String uri = "/phoneAlert?firestation=1";
        String expectedResponse = "{\n" +
                "    \"phoneNumbers\": [\n" +
                "        {\"phone\":\"841-874-6512\"},\n" +
                "        {\"phone\":\"841-874-8547\"},\n" +
                "        {\"phone\":\"841-874-7462\"},\n" +
                "        {\"phone\":\"841-874-7784\"},\n" +
                "        {\"phone\":\"841-874-7784\"},\n" +
                "        {\"phone\":\"841-874-7784\"}\n" +
                "    ]\n" +
                "}";

        //Method
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //Verification
        assertEquals(200, status);
        assertEquals(expectedResponse,mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void fireReturnsFirestationNumberAndResidentsForAddress() throws Exception {
        //Preparation
        String uri = "/fire?address=1509 Culver St";
        String expectedResponse = "{\n" +
                "    \"firestations\": [\n" +
                "        {\"address\":\"1509 Culver St\",\"station\":3}\n" +
                "    ],\n" +
                "    \"persons\": [\n" +
                "        {\"firstName\":\"Felicia\",\"lastName\":\"Boyd\",\"phone\":\"841-874-6544\",\"Age\":\"35\",\"medications\":[\"tetracyclaz:650mg\"],\"allergies\":[\"xilliathal\"]},\n" +
                "        {\"firstName\":\"Jacob\",\"lastName\":\"Boyd\",\"phone\":\"841-874-6513\",\"Age\":\"32\",\"medications\":[\"pharmacol:5000mg\",\"terazine:10mg\",\"noznazol:250mg\"],\"allergies\":[]},\n" +
                "        {\"firstName\":\"John\",\"lastName\":\"Boyd\",\"phone\":\"841-874-6512\",\"Age\":\"37\",\"medications\":[\"aznol:350mg\",\"hydrapermazol:100mg\"],\"allergies\":[\"nillacilan\"]},\n" +
                "        {\"firstName\":\"Roger\",\"lastName\":\"Boyd\",\"phone\":\"841-874-6512\",\"Age\":\"4\",\"medications\":[],\"allergies\":[]},\n" +
                "        {\"firstName\":\"Tenley\",\"lastName\":\"Boyd\",\"phone\":\"841-874-6512\",\"Age\":\"9\",\"medications\":[],\"allergies\":[\"peanut\"]}\n" +
                "    ]\n" +
                "}";

        //Method
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //Verification
        assertEquals(200, status);
        assertEquals(expectedResponse,mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void floodStationsReturnsListOfHouseholdsForEachProvidedStationNumber() throws Exception {
        //Preparation
        String uri = "/flood/stations?stations=2,4";
        String expectedResponse = "{\n" +
                "    \"firestations\": [\n" +
                "        {\"station\":2,\"households\":[\n" +
                "            {\"address\": \"29 15th St\",\"persons\":[\n" +
                "                {\"firstName\":\"Jonanathan\",\"lastName\":\"Marrack\",\"phone\":\"841-874-6513\",\"Age\":\"32\",\"medications\":[],\"allergies\":[]}\n" +
                "            ]},\n" +
                "            {\"address\": \"892 Downing Ct\",\"persons\":[\n" +
                "                {\"firstName\":\"Sophia\",\"lastName\":\"Zemicks\",\"phone\":\"841-874-7878\",\"Age\":\"33\",\"medications\":[\"aznol:60mg\",\"hydrapermazol:900mg\",\"pharmacol:5000mg\",\"terazine:500mg\"],\"allergies\":[\"peanut\",\"shellfish\",\"aznol\"]},\n" +
                "                {\"firstName\":\"Warren\",\"lastName\":\"Zemicks\",\"phone\":\"841-874-7512\",\"Age\":\"36\",\"medications\":[],\"allergies\":[]},\n" +
                "                {\"firstName\":\"Zach\",\"lastName\":\"Zemicks\",\"phone\":\"841-874-7512\",\"Age\":\"4\",\"medications\":[],\"allergies\":[]}\n" +
                "            ]},\n" +
                "            {\"address\": \"951 LoneTree Rd\",\"persons\":[\n" +
                "                {\"firstName\":\"Eric\",\"lastName\":\"Cadigan\",\"phone\":\"841-874-7458\",\"Age\":\"76\",\"medications\":[\"tradoxidine:400mg\"],\"allergies\":[]}\n" +
                "            ]}\n" +
                "        ]},\n" +
                "        {\"station\":4,\"households\":[\n" +
                "            {\"address\": \"489 Manchester St\",\"persons\":[\n" +
                "                {\"firstName\":\"Lily\",\"lastName\":\"Cooper\",\"phone\":\"841-874-9845\",\"Age\":\"27\",\"medications\":[],\"allergies\":[]}\n" +
                "            ]},\n" +
                "            {\"address\": \"112 Steppes Pl\",\"persons\":[\n" +
                "                {\"firstName\":\"Allison\",\"lastName\":\"Boyd\",\"phone\":\"841-874-9888\",\"Age\":\"56\",\"medications\":[\"aznol:200mg\"],\"allergies\":[\"nillacilan\"]},\n" +
                "                {\"firstName\":\"Ron\",\"lastName\":\"Peters\",\"phone\":\"841-874-8888\",\"Age\":\"56\",\"medications\":[],\"allergies\":[]},\n" +
                "                {\"firstName\":\"Tony\",\"lastName\":\"Cooper\",\"phone\":\"841-874-6874\",\"Age\":\"27\",\"medications\":[\"hydrapermazol:300mg\",\"dodoxadin:30mg\"],\"allergies\":[\"shellfish\"]}\n" +
                "            ]}\n" +
                "        ]}\n" +
                "    ]\n" +
                "}";

        //Method
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //Verification
        assertEquals(200, status);
        assertEquals(expectedResponse,mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void personInfoReturnsDetailsForAllPeopleWithProvidedName() throws Exception {
        //Preparation
        String uri = "/personInfo?firstName=John&lastName=Boyd";
        String expectedResponse = "{\n" +
                "    \"persons\": [\n" +
                "        {\"firstName\":\"John\",\"lastName\":\"Boyd\",\"address\":\"1509 Culver St\",\"city\":\"Culver\",\"zip\":\"97451\",\"Age\":\"37\",\"email\":\"jaboyd@email.com\",\"medications\":[\"aznol:350mg\",\"hydrapermazol:100mg\"],\"allergies\":[\"nillacilan\"]}\n" +
                "    ]\n" +
                "}";

        //Method
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //Verification
        assertEquals(200, status);
        assertEquals(expectedResponse,mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void communityEmailReturnsEmailAddressesForAllResidentsOfProvidedCity() throws Exception {
        //Preparation
        String uri = "/communityEmail?city=Culver";
        String expectedResponse = "{\n" +
                "    \"residentEmails\": [\n" +
                "        {\"email\":\"aly@imail.com\"},\n" +
                "        {\"email\":\"bstel@email.com\"},\n" +
                "        {\"email\":\"clivfd@ymail.com\"},\n" +
                "        {\"email\":\"gramps@email.com\"},\n" +
                "        {\"email\":\"jaboyd@email.com\"},\n" +
                "        {\"email\":\"jaboyd@email.com\"},\n" +
                "        {\"email\":\"drk@email.com\"},\n" +
                "        {\"email\":\"jpeter@email.com\"},\n" +
                "        {\"email\":\"jaboyd@email.com\"},\n" +
                "        {\"email\":\"drk@email.com\"},\n" +
                "        {\"email\":\"bstel@email.com\"},\n" +
                "        {\"email\":\"lily@email.com\"},\n" +
                "        {\"email\":\"jaboyd@email.com\"},\n" +
                "        {\"email\":\"reg@email.com\"},\n" +
                "        {\"email\":\"jaboyd@email.com\"},\n" +
                "        {\"email\":\"jpeter@email.com\"},\n" +
                "        {\"email\":\"ssanw@email.com\"},\n" +
                "        {\"email\":\"soph@email.com\"},\n" +
                "        {\"email\":\"tenz@email.com\"},\n" +
                "        {\"email\":\"tenz@email.com\"},\n" +
                "        {\"email\":\"tcoop@ymail.com\"},\n" +
                "        {\"email\":\"ward@email.com\"},\n" +
                "        {\"email\":\"zarc@email.com\"}\n" +
                "    ]\n" +
                "}";

        //Method
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //Verification
        assertEquals(200, status);
        assertEquals(expectedResponse,mvcResult.getResponse().getContentAsString());
    }
}
