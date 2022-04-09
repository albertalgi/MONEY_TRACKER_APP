package com.manage_money.money_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

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

    int month = 0;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.show_reports_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_report_item:
                selectReportDate();
                break;
        }
        return true;
    }

    public void selectReportDate() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View reportDateSelector = getLayoutInflater().inflate(R.layout.create_report_dialog, null);

        dialogBuilder.setView(reportDateSelector);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    public void onMonthClicked(View view) {
        //Reset background of other elements
        TextView january = dialog.findViewById(R.id.january);
        january.setBackgroundColor(Color.WHITE);
        TextView february = dialog.findViewById(R.id.february);
        february.setBackgroundColor(Color.WHITE);
        TextView march = dialog.findViewById(R.id.march);
        march.setBackgroundColor(Color.WHITE);
        TextView april = dialog.findViewById(R.id.april);
        april.setBackgroundColor(Color.WHITE);
        TextView may = dialog.findViewById(R.id.may);
        may.setBackgroundColor(Color.WHITE);
        TextView june = dialog.findViewById(R.id.june);
        june.setBackgroundColor(Color.WHITE);
        TextView jul = dialog.findViewById(R.id.jul);
        jul.setBackgroundColor(Color.WHITE);
        TextView aug = dialog.findViewById(R.id.august);
        aug.setBackgroundColor(Color.WHITE);
        TextView sep = dialog.findViewById(R.id.september);
        sep.setBackgroundColor(Color.WHITE);
        TextView oct = dialog.findViewById(R.id.october);
        oct.setBackgroundColor(Color.WHITE);
        TextView nov = dialog.findViewById(R.id.november);
        nov.setBackgroundColor(Color.WHITE);
        TextView dec = dialog.findViewById(R.id.december);
        dec.setBackgroundColor(Color.WHITE);

        TextView month_element = dialog.findViewById(view.getId());
        month_element.setBackgroundColor(Color.GRAY);
        switch(view.getId()) {
            case R.id.january:
                month = 1;
                break;
            case R.id.february:
                month = 2;
                break;
            case R.id.march:
                month = 3;
                break;
            case R.id.april:
                month = 4;
                break;
            case R.id.may:
                month = 5;
                break;
            case R.id.june:
                month = 6;
                break;
            case R.id.jul:
                month = 7;
                break;
            case R.id.august:
                month = 8;
                break;
            case R.id.september:
                month = 9;
                break;
            case R.id.october:
                month = 10;
                break;
            case R.id.november:
                month = 11;
                break;
            case R.id.december:
                month = 12;
                break;
        }
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

    public void createManualReport(View v){
        EditText year = (EditText) dialog.findViewById(R.id.year_createreport);
        String inputYear = year.getText().toString();

        if (inputYear == "" || !(inputYear.matches("^[0-9]{4}$")) || month == 0) {
            if (!(inputYear.matches("^[0-9]{4}$"))) {
                year.setError(getResources().getString(R.string.yearError));
            }

            if (month == 0) {
                Toast toast = Toast.makeText(this, getResources().getText(R.string.monthError), Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
            //If all correct, create the report and dismiss dialog
            Report report = new Report("REPORT",formatter.format(TimerUtils.getFirstDayOfSpecifiedMonthAndYear(month,Integer.parseInt(inputYear))), formatter.format(TimerUtils.getLastDayOfSpecifiedMonthAndYear(month,Integer.parseInt(inputYear))));
            db.reportsDao().insertReport(report);
            month = 0;
            dialog.dismiss();
            finish();
            startActivity(getIntent());
        }
    }

    public void cancelManualReportCreation(View v) {
        dialog.dismiss();
    }

    public void createReport(View view) {
        //TODO date format for SQLite is "yyyy-MM-dd". Change in the future for dd/MM/yyyy
        Report report = new Report("REPORT", formatter.format(TimerUtils.getFirstDayOfCurrentMonth()), formatter.format(TimerUtils.getLastDayOfCurrentMonth()));
        db.reportsDao().insertReport(report);
        this.reports = this.db.reportsDao().getAllReports();
        Intent intent = new Intent(ShowReportsActivity.this, MainActivity.class);
        //Set report init and end date arrays
        getMonthsArray();
        intent.putExtra("initReportDate", this.initDates.get(0));
        intent.putExtra("endReportDate", this.endDates.get(0));
        startActivity(intent);
    }
}