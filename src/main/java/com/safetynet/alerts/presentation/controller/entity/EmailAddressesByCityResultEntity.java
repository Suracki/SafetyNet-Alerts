package com.safetynet.alerts.presentation.controller.entity;

import java.util.ArrayList;

public class EmailAddressesByCityResultEntity {

    private ArrayList<String> emails;

    public EmailAddressesByCityResultEntity() {
        emails = new ArrayList<>();
    }

    public void addEmail(String email){
        emails.add(email);
    }

}
