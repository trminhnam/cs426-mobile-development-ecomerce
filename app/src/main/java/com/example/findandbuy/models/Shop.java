package com.example.findandbuy.models;

public class Shop {
    private String uid, fullName, shopNames, phoneNum, district, city, country, address, email, password, confirmPassword, profileImage;

    public Shop() {}

    public Shop(String uid, String fullName, String shopNames, String phoneNum, String district, String city, String country, String address, String email, String password, String confirmPassword, String profileImage) {
        this.uid = uid;
        this.fullName = fullName;
        this.shopNames = shopNames;
        this.phoneNum = phoneNum;
        this.district = district;
        this.city = city;
        this.country = country;
        this.address = address;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.profileImage = profileImage;
    }

    public String getUid(){
        return uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShopNames() {
        return shopNames;
    }

    public void setShopNames(String shopNames) {
        this.shopNames = shopNames;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
