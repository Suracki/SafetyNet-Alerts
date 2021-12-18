package com.safetynet.alerts.presentation.model.entity;

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
