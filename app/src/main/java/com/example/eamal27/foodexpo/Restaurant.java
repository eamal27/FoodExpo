package com.example.eamal27.foodexpo;

import android.location.Address;

import java.util.ArrayList;

public class Restaurant {

    private String name;
    private String businessId;
    private String email;
    private String phone;
    private Address address;
    private ArrayList<FoodItem> menu;

    Restaurant(){
        this.setName("");
        this.setBusinessId("");
        this.setEmail("");
        this.setPhone("");
        this.setAddress(null);
        menu = new ArrayList<FoodItem>();
    }

    Restaurant(String name, String businessId, String email, String phone, Address address){
        this.name = name;
        this.businessId = businessId;
        this.email = email;
        this.phone = phone;
        this.address = address;
        menu = new ArrayList<FoodItem>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void addMenuItem(FoodItem item){
        menu.add(item);
    }

    public ArrayList<FoodItem> getMenu(){
        return menu;
    }
}
