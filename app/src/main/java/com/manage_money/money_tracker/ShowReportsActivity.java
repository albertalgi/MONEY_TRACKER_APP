package com.manage_money.money_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.manage_money.money_tracker.R;
import com.manage_money.money_tracker.database.database.AppDatabase;
import com.manage_money.money_tracker.database.entities.Report;
import com.manage_money.money_tracker.reports.ReportsGridAdapter;
import com.manage_money.money_tracker.utils.timer.TimerUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ShowReportsActivity extends AppCompatActivity {

    GridView grid;
    AppDatabase db;
    List<Report> reports;
    List<String> initDates = new ArrayList<String>();
    List<String> endDates = new ArrayList<String>();

    TimerUtils timerUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_reports);

        getSupportActionBar().setTitle(getResources().getString(R.string.showReportsActivityTitle));

        //Set DB
        this.db = AppDatabase.getInstance(this);
        this.reports = this.db.reportsDao().getAllReports();

        if(isThereAReport()) {
            Button button = (Button)findViewById(R.id.startReportButton);
            button.setVisibility(View.GONE);
            //Start timer everyTime the Activity is created.
            this.timerUtils = TimerUtils.getInstance(this);
        }

        grid = (GridView) findViewById(R.id.reportsGridView);

        ReportsGridAdapter adapter = new ReportsGridAdapter(this,this.reports, R.layout.report_element_grid_view);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShowReportsActivity.this, MainActivity.class);
                Log.i("Report position", String.valueOf(position));
                //Set report init and end date arrays
                getMonthsArray();
                intent.putExtra("initReportDate", initDates.get(position));
                intent.putExtra("endReportDate", endDates.get(position));
                startActivity(intent);
            }
        });

        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                db.reportsDao().deleteReport(reports.get(position));
                Log.i("Report deleted", String.valueOf(position));
                finish();
                startActivity(getIntent());
                return true;
            }
        });
    }

    private void getMonthsArray() {
        for(Report report: this.reports) {
            this.initDates.add(report.initDate);
            this.endDates.add(report.endDate);
        }
    }

    private boolean isThereAReport() {
        List<Report> reports = this.db.reportsDao().getAllReports();
        if(reports.isEmpty())
            return false;
        return true;
    }

    public void createReport(View view) {
        //TODO date format for SQLite is "yyyy-MM-dd". Change in the future for dd/MM/yyyy
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Report report = new Report("REPORT", formatter.format(TimerUtils.getFirstDayOfCurrentMonth()), formatter.format(TimerUtils.getLastDayOfCurrentMonth()));
        db.reportsDao().insertReport(report);
        this.reports = this.db.reportsDao().getAllReports();
        Intent intent = new Intent(ShowReportsActivity.this, MainActivity.class);
        //Set report init and end date arrays
        Log.i("FIRST REPORT", "FIRST REPORT CREATED");
        getMonthsArray();
        intent.putExtra("initReportDate", this.initDates.get(0));
        intent.putExtra("endReportDate", this.endDates.get(0));
        startActivity(intent);
    }
}