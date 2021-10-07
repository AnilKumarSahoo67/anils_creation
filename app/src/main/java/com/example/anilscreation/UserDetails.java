package com.example.anilscreation;

public class UserDetails {
    private String Name,address,contact,image;
    private int ID;
    public UserDetails(int ID,String name, String address, String contact, String image) {
        this.ID=ID;
        this.Name = name;
        this.address = address;
        this.contact = contact;
        this.image=image;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
