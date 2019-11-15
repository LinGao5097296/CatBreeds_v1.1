package com.sky.CatBreeds.database;

import java.io.Serializable;

public class Details implements Serializable {
    private int MID;
    private String name;
    private String made_from;
    private String content;
    private String price;
    private String BID;
    private int sale;

    public Details() {
        super();
    }
    public Details(String BID, String name, String content,String price) {
        super();
        this.BID=BID;
        this.name=name;
        this.content = content;
        this.price=price;

    }
    public Details(int MID, String name) {
        super();
        this.MID=MID;
        this.name=name;


    }

    public int getMID(){
        return MID;
    }

    public void setMID(int MID) {
        this.MID = MID;
    }

    public String getBID() {
        return BID;
    }

    public void setBID(String BID) {
        this.BID = BID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMade_from() {
        return made_from;
    }

    public void setMade_from(String made_from) {
        this.made_from = made_from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getSale(){
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

}
