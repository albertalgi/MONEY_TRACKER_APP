package com.manage_money.money_tracker.database.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.manage_money.money_tracker.database.DAO.MovimentsDao;
import com.manage_money.money_tracker.database.DAO.ReportsDao;
import com.manage_money.money_tracker.database.entities.MovimentEntity;
import com.manage_money.money_tracker.database.entities.Report;

@Database(entities = {MovimentEntity.class, Report.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "app-db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return instance;
    }
    public abstract MovimentsDao movimentsDao();
    public abstract ReportsDao reportsDao();
}