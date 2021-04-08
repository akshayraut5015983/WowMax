package com.swaliya.wowmax.model;

public class DbModel {
    private String id;
    private String name;
    private String desp;

    public DbModel(String id, String name, String desp) {
        this.id = id;
        this.name = name;
        this.desp = desp;
    }

    public DbModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }
}
