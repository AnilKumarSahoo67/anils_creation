package com.example.anilscreation.Contact;

public class StudentDataPOJO {
    private String slNo,name,phone;

    public StudentDataPOJO(String slNo, String name, String phone) {
        this.slNo = slNo;
        this.name = name;
        this.phone = phone;
    }

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
