package com.lucca.fileSorter.model;

import java.util.List;

public class Sale {

    private int id;
    private List<Item> itemList;
    private Salesman salesman;

    public Sale(int id, List<Item> itemList, Salesman salesman) {
        this.id = id;
        this.itemList = itemList;
        this.salesman = salesman;
    }

    public Sale(){

    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public Salesman getSalesman() {
        return salesman;
    }

    public void setSalesman(Salesman salesman) {
        this.salesman = salesman;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSaleValue(){
        double value = 0;

        for (Item item : itemList){
            value += (item.getPrice() * item.getQuantity());
        }

        return value;
    }
}
