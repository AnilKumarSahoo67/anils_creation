package com.example.anilscreation.Contact;

public class ContactPojo {
    private String ID;
    private String NAME;
    private String NUMBER;

    public ContactPojo(String ID, String NAME, String NUMBER) {
        this.ID = ID;
        this.NAME = NAME;
        this.NUMBER = NUMBER;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getNUMBER() {
        return NUMBER;
    }

    public void setNUMBER(String NUMBER) {
        this.NUMBER = NUMBER;
    }
}
