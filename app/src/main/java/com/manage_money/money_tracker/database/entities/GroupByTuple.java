package com.manage_money.money_tracker.database.entities;

import androidx.room.ColumnInfo;

public class GroupByTuple {
    @ColumnInfo(name = "total")
    public double totalMoviment;

    @ColumnInfo(name = "classification")
    public String classification;

    @ColumnInfo(name = "titol")
    public String titol;

    @ColumnInfo(name = "data")
    public String data;
}
