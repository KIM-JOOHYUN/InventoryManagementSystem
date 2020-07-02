package com.example.inventorymanager;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * @brief Product
 * @details Structure product information.
 * productCode : unique code of product
 * productName : name of product
 * business : company selling product
 * stock : quantity remaining
 * barocde : barcode number
 * size : size of product
 * color : color of product
 * @author KIMJOOHYUN
 * */
public class Prod{
    public String productCode;
    public String productName;
    public String business;
    public int stock;
    public String barcode;
    public String size;
    public String color;
    public ArrayList<String> history;

    public Prod(){}
    public Prod(String productCode, String business, int stock ,String productName,String barcode,String size, String color){
        this.productCode = productCode;
        this.productName = productName;
        this.business = business;
        this.stock = stock ;
        this.barcode = barcode;
        this.size = size;
        this.color = color;

    }
}