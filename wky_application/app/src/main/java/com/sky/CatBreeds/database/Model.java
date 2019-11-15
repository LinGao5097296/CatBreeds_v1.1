package com.sky.CatBreeds.database;

import java.io.Serializable;

public class Model implements Serializable {
    private int MID;
    private String name;
    private String made_from;
    private String date_of_sale;
    private  String img;
    private String desc;
    private String type;
    private Float price;
    private String rn;
    private int sale;

    public Model() {
        super();
    }
    public Model(String name, Float price, String made_from, String date_of_sale ) {
        super();
        this.name=name;
        this.made_from=made_from;
        this.date_of_sale=date_of_sale;
        this.price=price;

    }
    public Model(int MID, String name) {
        super();
        this.MID=MID;
        this.name=name;


    }
    public Model(int MID, String name, String type, String desc,String img,String rn,String date_of_sale) {
        super();
        this.MID=MID;
        this.name=name;
        this.type=type;
        this.desc=desc;
        this.img=img;
        this.rn=rn;
        this.date_of_sale=date_of_sale;
    }
    public int getMID(){
        return MID;
    }

    public void setMID(int MID) {
        this.MID = MID;
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

    public String getDate_of_sale() {
        return date_of_sale;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }


    public void setDate_of_sale(String date_of_sale) {
        this.date_of_sale = date_of_sale;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setRn(String rn) {
        this.rn = rn;
    }

    public String getRn() {
        return rn;
    }

    public int getSale(){
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

}
