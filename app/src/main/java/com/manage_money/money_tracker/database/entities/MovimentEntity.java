package com.manage_money.money_tracker.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "moviments")
public class MovimentEntity {

    public MovimentEntity(String titol, double quantity, String tipus, String data, String observacions, String classification, String picturePath) {
        this.titol = titol;
        this.quantity = quantity;
        this.tipus = tipus;
        this.data = data;
        this.observacions = observacions;
        this.classification = classification;
        this.picturePath = picturePath;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "titol")
    public String titol;

    @ColumnInfo(name = "quantitat")
    public double quantity;

    @ColumnInfo(name = "tipus")
    public String tipus;

    @ColumnInfo(name = "data")
    public String data;

    @ColumnInfo(name = "notes")
    public String observacions;

    @ColumnInfo(name = "classification")
    public String classification;

    @ColumnInfo(name = "picturePath")
    public String picturePath;

}
