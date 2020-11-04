package com.swaliya.wowmax.model;

public class MyListData {

    private String url;
    private String name;
    private String cat;
    private String rel;
    private String qlt;
    private String dur;

    public MyListData(String name, int imgl) {
        this.name = name;
        this.imgl = imgl;
    }

    public MyListData(String url, String name, String cat, String rel, String qlt, String dur, String desp, int imgl, int imgp) {
        this.url = url;
        this.name = name;
        this.cat = cat;
        this.rel = rel;
        this.qlt = qlt;
        this.dur = dur;
        this.desp = desp;
        this.imgl = imgl;
        this.imgp = imgp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getQlt() {
        return qlt;
    }

    public void setQlt(String qlt) {
        this.qlt = qlt;
    }

    public String getDur() {
        return dur;
    }

    public void setDur(String dur) {
        this.dur = dur;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public int getImgl() {
        return imgl;
    }

    public void setImgl(int imgl) {
        this.imgl = imgl;
    }

    public int getImgp() {
        return imgp;
    }

    public void setImgp(int imgp) {
        this.imgp = imgp;
    }

    private String desp;
    private int imgl;
    private int imgp;




}
