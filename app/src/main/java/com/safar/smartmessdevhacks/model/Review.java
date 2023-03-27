package com.safar.smartmessdevhacks.model;

public class Review {

    private String email, text;
    private double star;

    public Review() {
    }

    public Review(String email, String text, double star) {
        this.email = email;
        this.text = text;
        this.star = star;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getStar() {
        return star;
    }

    public void setStar(double star) {
        this.star = star;
    }
}
