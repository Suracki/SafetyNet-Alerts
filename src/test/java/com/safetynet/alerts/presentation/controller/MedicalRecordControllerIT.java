package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.data.io.JsonDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(MedicalRecordController.class)
@WebAppConfiguration
@Import(ObjectControllerBeanSetup.class)
public class MedicalRecordControllerIT {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void makeSureTestDatabaseIsReady() throws Exception {
        resetDatafile();
    }

    @AfterAll
    public static void resetDatafile() throws Exception {
        JsonDAO jsonDAO = new JsonDAO();
        String databaseString = "{\n" +
                "    \"persons\": [\n" +
                "        {\"firstName\":\"John\",\"lastName\":\"Boyd\",\"address\":\"1509 Culver St\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-6512\",\"email\":\"jaboyd@email.com\"},\n" +
                "        {\"firstName\":\"Jacob\",\"lastName\":\"Boyd\",\"address\":\"1509 Culver St\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-6513\",\"email\":\"drk@email.com\"},\n" +
                "        {\"firstName\":\"Tenley\",\"lastName\":\"Boyd\",\"address\":\"1509 Culver St\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-6512\",\"email\":\"tenz@email.com\"},\n" +
                "        {\"firstName\":\"Roger\",\"lastName\":\"Boyd\",\"address\":\"1509 Culver St\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-6512\",\"email\":\"jaboyd@email.com\"},\n" +
                "        {\"firstName\":\"Felicia\",\"lastName\":\"Boyd\",\"address\":\"1509 Culver St\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-6544\",\"email\":\"jaboyd@email.com\"},\n" +
                "        {\"firstName\":\"Jonanathan\",\"lastName\":\"Marrack\",\"address\":\"29 15th St\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-6513\",\"email\":\"drk@email.com\"},\n" +
                "        {\"firstName\":\"Tessa\",\"lastName\":\"Carman\",\"address\":\"834 Binoc Ave\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-6512\",\"email\":\"tenz@email.com\"},\n" +
                "        {\"firstName\":\"Peter\",\"lastName\":\"Duncan\",\"address\":\"644 Gershwin Cir\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-6512\",\"email\":\"jaboyd@email.com\"},\n" +
                "        {\"firstName\":\"Foster\",\"lastName\":\"Shepard\",\"address\":\"748 Townings Dr\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-6544\",\"email\":\"jaboyd@email.com\"},\n" +
                "        {\"firstName\":\"Tony\",\"lastName\":\"Cooper\",\"address\":\"112 Steppes Pl\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-6874\",\"email\":\"tcoop@ymail.com\"},\n" +
                "        {\"firstName\":\"Lily\",\"lastName\":\"Cooper\",\"address\":\"489 Manchester St\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-9845\",\"email\":\"lily@email.com\"},\n" +
                "        {\"firstName\":\"Sophia\",\"lastName\":\"Zemicks\",\"address\":\"892 Downing Ct\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-7878\",\"email\":\"soph@email.com\"},\n" +
                "        {\"firstName\":\"Warren\",\"lastName\":\"Zemicks\",\"address\":\"892 Downing Ct\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-7512\",\"email\":\"ward@email.com\"},\n" +
                "        {\"firstName\":\"Zach\",\"lastName\":\"Zemicks\",\"address\":\"892 Downing Ct\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-7512\",\"email\":\"zarc@email.com\"},\n" +
                "        {\"firstName\":\"Reginold\",\"lastName\":\"Walker\",\"address\":\"908 73rd St\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-8547\",\"email\":\"reg@email.com\"},\n" +
                "        {\"firstName\":\"Jamie\",\"lastName\":\"Peters\",\"address\":\"908 73rd St\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-7462\",\"email\":\"jpeter@email.com\"},\n" +
                "        {\"firstName\":\"Ron\",\"lastName\":\"Peters\",\"address\":\"112 Steppes Pl\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-8888\",\"email\":\"jpeter@email.com\"},\n" +
                "        {\"firstName\":\"Allison\",\"lastName\":\"Boyd\",\"address\":\"112 Steppes Pl\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-9888\",\"email\":\"aly@imail.com\"},\n" +
                "        {\"firstName\":\"Brian\",\"lastName\":\"Stelzer\",\"address\":\"947 E. Rose Dr\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-7784\",\"email\":\"bstel@email.com\"},\n" +
                "        {\"firstName\":\"Shawna\",\"lastName\":\"Stelzer\",\"address\":\"947 E. Rose Dr\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-7784\",\"email\":\"ssanw@email.com\"},\n" +
                "        {\"firstName\":\"Kendrik\",\"lastName\":\"Stelzer\",\"address\":\"947 E. Rose Dr\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-7784\",\"email\":\"bstel@email.com\"},\n" +
                "        {\"firstName\":\"Clive\",\"lastName\":\"Ferguson\",\"address\":\"748 Townings Dr\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-6741\",\"email\":\"clivfd@ymail.com\"},\n" +
                "        {\"firstName\":\"Eric\",\"lastName\":\"Cadigan\",\"address\":\"951 LoneTree Rd\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-7458\",\"email\":\"gramps@email.com\"}\n" +
                "    ],\n" +
                "    \"firestations\": [\n" +
                "        {\"address\":\"1509 Culver St\",\"station\":3},\n" +
                "        {\"address\":\"29 15th St\",\"station\":2},\n" +
                "        {\"address\":\"834 Binoc Ave\",\"station\":3},\n" +
                "        {\"address\":\"644 Gershwin Cir\",\"station\":1},\n" +
                "        {\"address\":\"748 Townings Dr\",\"station\":3},\n" +
                "        {\"address\":\"112 Steppes Pl\",\"station\":3},\n" +
                "        {\"address\":\"489 Manchester St\",\"station\":4},\n" +
                "        {\"address\":\"892 Downing Ct\",\"station\":2},\n" +
                "        {\"address\":\"908 73rd St\",\"station\":1},\n" +
                "        {\"address\":\"112 Steppes Pl\",\"station\":4},\n" +
                "        {\"address\":\"947 E. Rose Dr\",\"station\":1},\n" +
                "        {\"address\":\"748 Townings Dr\",\"station\":3},\n" +
                "        {\"address\":\"951 LoneTree Rd\",\"station\":2}\n" +
                "    ],\n" +
                "    \"medicalrecords\": [\n" +
                "        {\"firstName\":\"John\",\"lastName\":\"Boyd\",\"birthdate\":\"03/06/1984\",\"medications\":[\"aznol:350mg\",\"hydrapermazol:100mg\"],\"allergies\":[\"nillacilan\"]},\n" +
                "        {\"firstName\":\"Jacob\",\"lastName\":\"Boyd\",\"birthdate\":\"03/06/1989\",\"medications\":[\"pharmacol:5000mg\",\"terazine:10mg\",\"noznazol:250mg\"],\"allergies\":[]},\n" +
                "        {\"firstName\":\"Tenley\",\"lastName\":\"Boyd\",\"birthdate\":\"02/18/2012\",\"medications\":[],\"allergies\":[\"peanut\"]},\n" +
                "        {\"firstName\":\"Roger\",\"lastName\":\"Boyd\",\"birthdate\":\"09/06/2017\",\"medications\":[],\"allergies\":[]},\n" +
                "        {\"firstName\":\"Felicia\",\"lastName\":\"Boyd\",\"birthdate\":\"01/08/1986\",\"medications\":[\"tetracyclaz:650mg\"],\"allergies\":[\"xilliathal\"]},\n" +
                "        {\"firstName\":\"Jonanathan\",\"lastName\":\"Marrack\",\"birthdate\":\"01/03/1989\",\"medications\":[],\"allergies\":[]},\n" +
                "        {\"firstName\":\"Tessa\",\"lastName\":\"Carman\",\"birthdate\":\"02/18/2012\",\"medications\":[],\"allergies\":[]},\n" +
                "        {\"firstName\":\"Peter\",\"lastName\":\"Duncan\",\"birthdate\":\"09/06/2000\",\"medications\":[],\"allergies\":[\"shellfish\"]},\n" +
                "        {\"firstName\":\"Foster\",\"lastName\":\"Shepard\",\"birthdate\":\"01/08/1980\",\"medications\":[],\"allergies\":[]},\n" +
                "        {\"firstName\":\"Tony\",\"lastName\":\"Cooper\",\"birthdate\":\"03/06/1994\",\"medications\":[\"hydrapermazol:300mg\",\"dodoxadin:30mg\"],\"allergies\":[\"shellfish\"]},\n" +
                "        {\"firstName\":\"Lily\",\"lastName\":\"Cooper\",\"birthdate\":\"03/06/1994\",\"medications\":[],\"allergies\":[]},\n" +
                "        {\"firstName\":\"Sophia\",\"lastName\":\"Zemicks\",\"birthdate\":\"03/06/1988\",\"medications\":[\"aznol:60mg\",\"hydrapermazol:900mg\",\"pharmacol:5000mg\",\"terazine:500mg\"],\"allergies\":[\"peanut\",\"shellfish\",\"aznol\"]},\n" +
                "        {\"firstName\":\"Warren\",\"lastName\":\"Zemicks\",\"birthdate\":\"03/06/1985\",\"medications\":[],\"allergies\":[]},\n" +
                "        {\"firstName\":\"Zach\",\"lastName\":\"Zemicks\",\"birthdate\":\"03/06/2017\",\"medications\":[],\"allergies\":[]},\n" +
                "        {\"firstName\":\"Reginold\",\"lastName\":\"Walker\",\"birthdate\":\"08/30/1979\",\"medications\":[\"thradox:700mg\"],\"allergies\":[\"illisoxian\"]},\n" +
                "        {\"firstName\":\"Jamie\",\"lastName\":\"Peters\",\"birthdate\":\"03/06/1982\",\"medications\":[],\"allergies\":[]},\n" +
                "        {\"firstName\":\"Ron\",\"lastName\":\"Peters\",\"birthdate\":\"04/06/1965\",\"medications\":[],\"allergies\":[]},\n" +
                "        {\"firstName\":\"Allison\",\"lastName\":\"Boyd\",\"birthdate\":\"03/15/1965\",\"medications\":[\"aznol:200mg\"],\"allergies\":[\"nillacilan\"]},\n" +
                "        {\"firstName\":\"Brian\",\"lastName\":\"Stelzer\",\"birthdate\":\"12/06/1975\",\"medications\":[\"ibupurin:200mg\",\"hydrapermazol:400mg\"],\"allergies\":[\"nillacilan\"]},\n" +
                "        {\"firstName\":\"Shawna\",\"lastName\":\"Stelzer\",\"birthdate\":\"07/08/1980\",\"medications\":[],\"allergies\":[]},\n" +
                "        {\"firstName\":\"Kendrik\",\"lastName\":\"Stelzer\",\"birthdate\":\"03/06/2014\",\"medications\":[\"noxidian:100mg\",\"pharmacol:2500mg\"],\"allergies\":[]},\n" +
                "        {\"firstName\":\"Clive\",\"lastName\":\"Ferguson\",\"birthdate\":\"03/06/1994\",\"medications\":[],\"allergies\":[]},\n" +
                "        {\"firstName\":\"Eric\",\"lastName\":\"Cadigan\",\"birthdate\":\"08/06/1945\",\"medications\":[\"tradoxidine:400mg\"],\"allergies\":[]}\n" +
                "    ]\n" +
                "}";
        jsonDAO.writeJsonToFile("database/testdata.json",databaseString);
    }

    //Testing successful interactions
    @Test
    public void medicalRecordEndpointCanAddNewMedicalRecordViaPost() throws Exception {
        //Preparation
        String uri = "/medicalRecord?firstName=Testame&lastName=Name&birthDate=01/01/2000&medications=something:1g,another:2g&allergies=nothing,really";

        //Method
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString()
                .replace("\n", "").replace("  ", "");

        assertEquals(201, status);
        assertEquals(TestConstants.medicalRecordControllerPostExpectedResponse,receivedResponse);
    }

    @Test
    public void medicalRecordEndpointCanUpdateMedicalRecordViaPut() throws Exception {
        //Preparation
        String uri = "/medicalRecord?firstName=Tessa&lastName=Carman&birthDate=01/01/2000&medications=&allergies=";

        //Method
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //Verification
        assertEquals(200, status);
    }

    @Test
    public void medicalRecordEndpointCanRemoveMedicalRecordViaDelete() throws Exception {
        //Preparation
        String uri = "/medicalRecord?firstName=Eric&lastName=Cadigan";

        //Method
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //Verification
        assertEquals(200, status);
    }

    //Testing unsuccessful interactions
    @Test
    public void medicalRecordEndpointReturns409WhenPostingDuplicateMedicalRecord() throws Exception {
        //Preparation
        String uri = "/medicalRecord?firstName=John&lastName=Boyd&birthDate=01/01/2000&medications=something:1g,another:2g&allergies=nothing,really";

        //Method
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();

        assertEquals(409, status);
    }

    @Test
    public void medicalRecordEndpointReturnsError404WhenUpdatingNonExistingMedicalRecord() throws Exception {
        //Preparation
        String uri = "/medicalRecord?firstName=Doesnt&lastName=Exist&birthDate=01/01/2000&medications=&allergies=";

        //Method
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //Verification
        assertEquals(404, status);
    }

    @Test
    public void medicalRecordEndpointReturnsError404WhenRemovingNonExistingMedicalRecord() throws Exception {
        //Preparation
        String uri = "/medicalRecord?firstName=Doesnt&lastName=Exist&birthDate=01/01/2000&medications=&allergies=";

        //Method
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();

        //Verification
        assertEquals(404, status);
    }

}
