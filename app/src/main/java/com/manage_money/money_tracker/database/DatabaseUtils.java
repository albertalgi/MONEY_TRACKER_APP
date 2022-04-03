package com.manage_money.money_tracker.database;

import com.manage_money.money_tracker.database.DAO.MovimentsDao;
import com.manage_money.money_tracker.database.database.AppDatabase;

public class DatabaseUtils {
    public MovimentsDao movimentsDao;
    public AppDatabase db;

    public DatabaseUtils(AppDatabase db) {
        this.db = db;
        this.movimentsDao = db.movimentsDao();
    }
}
