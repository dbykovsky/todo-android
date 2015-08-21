package com.home.dbykovskyy.simpletodo.database;

/**
 * Created by dbykovskyy on 8/19/15.
 */
public class Item {
    long _id;
    public String itemName;

    @Override
    public String toString() {
        return itemName;
    }

}
