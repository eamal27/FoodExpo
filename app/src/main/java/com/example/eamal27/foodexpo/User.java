package com.example.eamal27.foodexpo;

import android.location.Address;

class User {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Address address;

    User(){
        firstName="";
        lastName="";
        email="";
        phone="";
        address=null;
    }

    User(String firstName, String lastName, String email, String phone, Address address){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
