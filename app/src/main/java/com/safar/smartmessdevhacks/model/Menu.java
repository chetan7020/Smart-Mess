package com.safar.smartmessdevhacks.model;

import java.util.ArrayList;

public class Menu {
    private String id, menuName, note;
    private int price;
    private ArrayList<String> contents;

    public Menu() {
    }

    public Menu(String id, String menuName, String note, int price, ArrayList<String> contents) { //Add
        this.id = id;
        this.menuName = menuName;
        this.note = note;
        this.price = price;
        this.contents = contents;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ArrayList<String> getContents() {
        return contents;
    }

    public void setContents(ArrayList<String> contents) {
        this.contents = contents;
    }
}
