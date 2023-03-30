package com.safar.smartmessdevhacks.model;

import android.util.Log;

import com.google.firebase.firestore.GeoPoint;

public class Owner {

    private static final String TAG = "Owner";
    private String name, email, messName, upi, geoHash, phoneNumber, messType;
    private GeoPoint geoPoint;
    private int customerCount;
    private double reviewCount, avgReview;

    public Owner() {
    }

    public Owner(String name, String email, String messName, String upi, String geoHash, String phoneNumber, GeoPoint geoPoint, String messType) {
        this.name = name;
        this.email = email;
        this.messName = messName;
        this.upi = upi;
        this.geoHash = geoHash;
        this.phoneNumber = phoneNumber;
        this.geoPoint = geoPoint;
        this.customerCount = 0;
        this.reviewCount = 0.0;
        this.avgReview = 0.0;
        this.messType = messType;
    }

    public Owner(double avgReview) {
        this.reviewCount = avgReview;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessName() {
        return messName;
    }

    public void setMessName(String messName) {
        this.messName = messName;
    }

    public String getUpi() {
        return upi;
    }

    public void setUpi(String upi) {
        this.upi = upi;
    }

    public String getGeoHash() {
        return geoHash;
    }

    public void setGeoHash(String geoHash) {
        this.geoHash = geoHash;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public int getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(int customerCount) {
        this.customerCount = customerCount;
    }

    public double getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(double reviewCount) {
        this.reviewCount = reviewCount;
    }

    public double getAvgReview() {
        return avgReview;
    }

    public void setAvgReview(double avgReview) {
        this.avgReview = avgReview;
    }

    public String getMessType() {
        return messType;
    }

    public void setMessType(String messType) {
        this.messType = messType;
    }
}
