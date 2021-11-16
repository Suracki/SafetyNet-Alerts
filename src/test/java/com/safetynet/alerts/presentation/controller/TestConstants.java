package com.safetynet.alerts.presentation.controller;

public class TestConstants {
    /*
    Strings used for output verification in Endpoint Controller tests
    Note for any new methods that for comparison, test output should be formatted via .replace("\n", "").replace("  ", "")
     */

    //Constants for GetMappingControllerTest
    public static final String firestationReturnsPeopleServicedByStationNumber = "{\"persons\": [{\"firstName\": \"Peter\",\"lastName\": \"Duncan\",\"address\": \"644 Gershwin Cir\",\"city\": \"Culver\",\"zip\": \"97451\",\"phone\": \"841-874-6512\"},{\"firstName\": \"Reginold\",\"lastName\": \"Walker\",\"address\": \"908 73rd St\",\"city\": \"Culver\",\"zip\": \"97451\",\"phone\": \"841-874-8547\"},{\"firstName\": \"Jamie\",\"lastName\": \"Peters\",\"address\": \"908 73rd St\",\"city\": \"Culver\",\"zip\": \"97451\",\"phone\": \"841-874-7462\"},{\"firstName\": \"Brian\",\"lastName\": \"Stelzer\",\"address\": \"947 E. Rose Dr\",\"city\": \"Culver\",\"zip\": \"97451\",\"phone\": \"841-874-7784\"},{\"firstName\": \"Shawna\",\"lastName\": \"Stelzer\",\"address\": \"947 E. Rose Dr\",\"city\": \"Culver\",\"zip\": \"97451\",\"phone\": \"841-874-7784\"},{\"firstName\": \"Kendrik\",\"lastName\": \"Stelzer\",\"address\": \"947 E. Rose Dr\",\"city\": \"Culver\",\"zip\": \"97451\",\"phone\": \"841-874-7784\"}],\"adults\": 5,\"children\": 1}";
    public static final String childAlertReturnsListOfChildrenAtAddress = "{\"children\": [{\"firstName\": \"Tenley\",\"lastName\": \"Boyd\",\"age\": 9},{\"firstName\": \"Roger\",\"lastName\": \"Boyd\",\"age\": 4}],\"adults\": [{\"firstName\": \"John\",\"lastName\": \"Boyd\"},{\"firstName\": \"Jacob\",\"lastName\": \"Boyd\"},{\"firstName\": \"Felicia\",\"lastName\": \"Boyd\"}]}";
    public static final String phoneAlertReturnsListOfPhoneNumbersForFirestationNumber = "{\"phoneNumber\": [\"841-874-6512\",\"841-874-8547\",\"841-874-7462\",\"841-874-7784\",\"841-874-7784\",\"841-874-7784\"]}";
    public static final String fireReturnsFirestationNumberAndResidentsForAddress = "{\"firestation\": {\"address\": \"1509 Culver St\",\"station\": 3},\"persons\": [{\"firstName\": \"John\",\"lastName\": \"Boyd\",\"phone\": \"841-874-6512\",\"age\": 37,\"medications\": [\"aznol:350mg\",\"hydrapermazol:100mg\"],\"allergies\": [\"nillacilan\"]},{\"firstName\": \"Jacob\",\"lastName\": \"Boyd\",\"phone\": \"841-874-6513\",\"age\": 32,\"medications\": [\"pharmacol:5000mg\",\"terazine:10mg\",\"noznazol:250mg\"],\"allergies\": []},{\"firstName\": \"Tenley\",\"lastName\": \"Boyd\",\"phone\": \"841-874-6512\",\"age\": 9,\"medications\": [],\"allergies\": [\"peanut\"]},{\"firstName\": \"Roger\",\"lastName\": \"Boyd\",\"phone\": \"841-874-6512\",\"age\": 4,\"medications\": [],\"allergies\": []},{\"firstName\": \"Felicia\",\"lastName\": \"Boyd\",\"phone\": \"841-874-6544\",\"age\": 35,\"medications\": [\"tetracyclaz:650mg\"],\"allergies\": [\"xilliathal\"]}]}";
    public static final String floodStationsReturnsListOfHouseholdsForEachProvidedStationNumber = "{\"firestations\": [{\"station\": 2,\"households\": [{\"people\": [{\"firstName\": \"Jonanathan\",\"lastName\": \"Marrack\",\"phone\": \"841-874-6513\",\"age\": 32,\"medications\": [],\"allergies\": []}],\"address\": \"29 15th St\",\"station\": 2},{\"people\": [{\"firstName\": \"Sophia\",\"lastName\": \"Zemicks\",\"phone\": \"841-874-7878\",\"age\": 33,\"medications\": [\"aznol:60mg\",\"hydrapermazol:900mg\",\"pharmacol:5000mg\",\"terazine:500mg\"],\"allergies\": [\"peanut\",\"shellfish\",\"aznol\"]},{\"firstName\": \"Warren\",\"lastName\": \"Zemicks\",\"phone\": \"841-874-7512\",\"age\": 36,\"medications\": [],\"allergies\": []},{\"firstName\": \"Zach\",\"lastName\": \"Zemicks\",\"phone\": \"841-874-7512\",\"age\": 4,\"medications\": [],\"allergies\": []}],\"address\": \"892 Downing Ct\",\"station\": 2},{\"people\": [{\"firstName\": \"Eric\",\"lastName\": \"Cadigan\",\"phone\": \"841-874-7458\",\"age\": 76,\"medications\": [\"tradoxidine:400mg\"],\"allergies\": []}],\"address\": \"951 LoneTree Rd\",\"station\": 2}]},{\"station\": 4,\"households\": [{\"people\": [{\"firstName\": \"Lily\",\"lastName\": \"Cooper\",\"phone\": \"841-874-9845\",\"age\": 27,\"medications\": [],\"allergies\": []}],\"address\": \"489 Manchester St\",\"station\": 4},{\"people\": [{\"firstName\": \"Tony\",\"lastName\": \"Cooper\",\"phone\": \"841-874-6874\",\"age\": 27,\"medications\": [\"hydrapermazol:300mg\",\"dodoxadin:30mg\"],\"allergies\": [\"shellfish\"]},{\"firstName\": \"Ron\",\"lastName\": \"Peters\",\"phone\": \"841-874-8888\",\"age\": 56,\"medications\": [],\"allergies\": []},{\"firstName\": \"Allison\",\"lastName\": \"Boyd\",\"phone\": \"841-874-9888\",\"age\": 56,\"medications\": [\"aznol:200mg\"],\"allergies\": [\"nillacilan\"]}],\"address\": \"112 Steppes Pl\",\"station\": 4}]}]}";
    public static final String personInfoReturnsDetailsForAllPeopleWithProvidedName = "{\"persons\": [{\"firstName\": \"John\",\"lastName\": \"Boyd\",\"address\": \"1509 Culver St\",\"city\": \"Culver\",\"zip\": \"97451\",\"age\": 37,\"email\": \"jaboyd@email.com\",\"medications\": [\"aznol:350mg\",\"hydrapermazol:100mg\"],\"allergies\": [\"nillacilan\"]}]}";
    public static final String communityEmailReturnsEmailAddressesForAllResidentsOfProvidedCity = "{\"emails\": [\"jaboyd@email.com\",\"drk@email.com\",\"tenz@email.com\",\"jaboyd@email.com\",\"jaboyd@email.com\",\"drk@email.com\",\"tenz@email.com\",\"jaboyd@email.com\",\"jaboyd@email.com\",\"tcoop@ymail.com\",\"lily@email.com\",\"soph@email.com\",\"ward@email.com\",\"zarc@email.com\",\"reg@email.com\",\"jpeter@email.com\",\"jpeter@email.com\",\"aly@imail.com\",\"bstel@email.com\",\"ssanw@email.com\",\"bstel@email.com\",\"clivfd@ymail.com\",\"gramps@email.com\"]}";

    //Constant for PersonControllerTest
    public static final String personControllerPostExpectedResponse = "{\"firstName\": \"TestFirst\",\"lastName\": \"TestLast\",\"address\": \"Test Street\",\"city\": \"Test City\",\"zip\": \"123\",\"phone\": \"555-1234\",\"email\": \"test@email.com\"}";

    //Constant for FirestationControllerTest
    public static final String firestationControllerPostExpectedResponse = "{\"address\": \"123 Test Street\",\"station\": 5}";

    //Constant for MedicalRecordControllerTest
    public static final String medicalRecordControllerPostExpectedResponse = "{\"firstName\": \"Testame\",\"lastName\": \"Name\",\"birthdate\": \"01/01/2000\",\"medications\": [\"something:1g\",\"another:2g\"],\"allergies\": [\"nothing\",\"really\"]}";

}
