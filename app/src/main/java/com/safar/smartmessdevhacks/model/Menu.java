package com.safar.smartmessdevhacks.model;

import java.util.ArrayList;

public class Menu {
    private String menuName, note;
    private ArrayList<String> contents;

    public Menu() {
    }

    public Menu(String menuName, String note, ArrayList<String> contents) {
        this.menuName = menuName;
        this.note = note;
        this.contents = contents;
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

    public ArrayList<String> getContents() {
        return contents;
    }

    public void setContents(ArrayList<String> contents) {
        this.contents = contents;
    }
}
