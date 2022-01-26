package com.example.nowastesociety.model;

public class AddressviewModel {

    private String tvaddressType, tvAddressdetails, addressId, landmark, flatOrHouseOrBuildingOrCompany, pinCode, townOrCity, userId, Lat, Long;

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLong() {
        return Long;
    }

    public void setLong(String aLong) {
        Long = aLong;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getFlatOrHouseOrBuildingOrCompany() {
        return flatOrHouseOrBuildingOrCompany;
    }

    public void setFlatOrHouseOrBuildingOrCompany(String flatOrHouseOrBuildingOrCompany) {
        this.flatOrHouseOrBuildingOrCompany = flatOrHouseOrBuildingOrCompany;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getTownOrCity() {
        return townOrCity;
    }

    public void setTownOrCity(String townOrCity) {
        this.townOrCity = townOrCity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTvaddressType() {
        return tvaddressType;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public void setTvaddressType(String tvaddressType) {
        this.tvaddressType = tvaddressType;
    }

    public String getTvAddressdetails() {
        return tvAddressdetails;
    }

    public void setTvAddressdetails(String tvAddressdetails) {
        this.tvAddressdetails = tvAddressdetails;
    }
}
