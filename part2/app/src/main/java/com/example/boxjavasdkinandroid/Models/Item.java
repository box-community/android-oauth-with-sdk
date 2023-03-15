package com.example.boxjavasdkinandroid.Models;

public class Item {
    private final String name;
    private final String modifiedBy;

    public Item(String name, String modifiedBy) {
        this.name = name;
        this.modifiedBy = modifiedBy;
    }

    public String getName() {
        return name;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }
}