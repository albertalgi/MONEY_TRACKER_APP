package com.manage_money.money_tracker.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reports")
public class Report {

    public Report(String name, String initDate, String endDate) {
        this.initDate = initDate;
        this.endDate = endDate;
        this.name = name + " " + this.initDate + " - " + this.endDate;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "initData")
    public String initDate;

    @ColumnInfo(name = "endDate")
    public String endDate;

    @ColumnInfo(name = "name")
    public String name;
}