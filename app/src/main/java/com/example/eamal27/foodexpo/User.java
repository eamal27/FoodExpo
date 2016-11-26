package com.example.eamal27.foodexpo;

import android.location.Address;

/**
 * Created by 100484424 on 11/21/2016.
 */

public class User {

    private String firstName;
    private String lastName;
    private String email;
    private Address address;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
