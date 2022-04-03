package com.manage_money.money_tracker.utils.timer;

import android.content.Context;
import android.util.Log;

import com.manage_money.money_tracker.database.database.AppDatabase;
import com.manage_money.money_tracker.database.entities.Report;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Singleton class. Only one instance crated.
 */
public class TimerUtils {
    private static TimerUtils timerUtils;
    private Context context;
    AppDatabase db;
    //TODO date format for SQLite is "yyyy-MM-dd". Change in the future for dd/MM/yyyy
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    /**
     *  Private Constructor for Singleton class
     */
    private TimerUtils(Context context) {
        this.context = context;
        db = AppDatabase.getInstance(this.context);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!isMonthlyReportCreated(formatter.format(getFirstDayOfCurrentMonth()))) {
                    createReport();
                    Log.i("TimerUtils","Report created");
                }
            }
        },getFirstDayOfCurrentMonth());
    }

    public static TimerUtils getInstance(Context context) {
        if(timerUtils == null) {
            timerUtils = new TimerUtils(context);
        }
        return timerUtils;
    }

    public static Date getFirstDayOfCurrentMonth() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH,1);
        return c.getTime();
    }

    public static Date getFirstDayOfCurrentWeek() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        Log.i("init WEEK",c.getTime().toString());
        return c.getTime();
    }

    public static Date getLastDayOfCurrentMonth() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DATE));
        return c.getTime();
    }

    public static Date getLastDayOfCurrentWeek() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        Log.i("end WEEK",c.getTime().toString());
        return c.getTime();
    }

    public void createReport() {
        Report report = new Report("REPORT", formatter.format(this.getFirstDayOfCurrentMonth()), formatter.format(this.getLastDayOfCurrentMonth()));
        db.reportsDao().insertReport(report);
    }

    public boolean isMonthlyReportCreated(String initDate) {
        List<Report> reports = db.reportsDao().getReportsByDate(initDate);
        if(reports.size() > 0) {
            Log.i("NUMBER REPORTS", initDate + ":" + String.valueOf(reports.size()) + " --> " + reports.get(0).initDate);
            return true;
        } else {
            return false;
        }
    }
}
