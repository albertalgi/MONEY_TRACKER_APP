package com.manage_money.money_tracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.manage_money.money_tracker.R;
import com.manage_money.money_tracker.database.database.AppDatabase;
import com.manage_money.money_tracker.database.entities.GroupByTuple;
import com.manage_money.money_tracker.database.entities.MovimentEntity;
import com.manage_money.money_tracker.model.moviments.MovimentListAdapter;
import com.manage_money.money_tracker.model.moviments.TipusMoviment;
import com.manage_money.money_tracker.utils.timer.TimerUtils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PieChart pieChart;
    private ListView lv;

    private static String divisa = "EUR";

    AppDatabase db;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private static String initReportDate;
    private static String endReportDate;

    SharedPreferences sharedPref;

    //TODO date format for SQLite is "yyyy-MM-dd". Change in the future for dd/MM/yyyy
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat actionBarFormatter = new SimpleDateFormat("dd MMM");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //Remove APP name on the Action Bar
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //View decorView = getWindow().getDecorView();
        // Hide the status bar.
        //int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        //decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
       // ActionBar actionBar = getActionBar();
        //actionBar.hide();
        //Check if extras exist from ShowReportsActivity
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String initDate = extras.getString("initReportDate");
            String endDate = extras.getString("endReportDate");
            this.initReportDate = initDate;
            this.endReportDate = endDate;
        }

        try {
            String mes = getResources().getString(R.string.mesDe);
            Date data = new SimpleDateFormat("yyyy-MM-dd").parse(this.initReportDate);
            String sData = new SimpleDateFormat("'" +mes+ " 'MMMM yyyy").format(data);
            getSupportActionBar().setTitle(sData);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sharedPref= MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        divisa = sharedPref.getString("divisa","EUR");

        setUpDB();
        pieChart = findViewById(R.id.pieChart);
        updateMonthlyAmount();
        setUpListView(TipusMoviment.DESPESA);
        setupPieChart();
        loadChartData(TipusMoviment.DESPESA);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_manu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuDivisa:
                createCurrencyPickerDialog();
                break;
            case R.id.menuInterval:
                break;
            case R.id.addMoviment:
                addMovimentActivity();
                break;
            case R.id.toReports:
                Intent intent = new Intent(this, ShowReportsActivity.class);
                startActivity(intent);
                break;
            case R.id.monthlyInterval:
                initReportDate = formatter.format(TimerUtils.getFirstDayOfCurrentMonth());
                endReportDate = formatter.format(TimerUtils.getLastDayOfCurrentMonth());

                try {
                    String mes = getResources().getString(R.string.mesDe);
                    Date data = new SimpleDateFormat("yyyy-MM-dd").parse(this.initReportDate);
                    String sData = new SimpleDateFormat("'" +mes+ " 'MMMM yyyy").format(data);
                    getSupportActionBar().setTitle(sData);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                refreshActivityData(TipusMoviment.INGRES);
                refreshActivityData(TipusMoviment.DESPESA);
                break;
            case R.id.weeklyInterval:
                DatePickerDialog picker = new DatePickerDialog(
                        MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                monthOfYear = monthOfYear +1;
                                Date init = TimerUtils.getFirstDayOfWeekSpecifiedDate(year,monthOfYear,dayOfMonth);
                                Date end = TimerUtils.getLastDayOfWeekSpecifiedDate(year,monthOfYear,dayOfMonth);
                                initReportDate = formatter.format(init);
                                endReportDate = formatter.format(end);

                                //Set actionbar title
                                //String week = getResources().getString(R.string.week_report);
                                String week = actionBarFormatter.format((init)) + " - " + actionBarFormatter.format(end);
                                getSupportActionBar().setTitle(week);

                                //Refresh data
                                refreshActivityData(TipusMoviment.INGRES);
                                refreshActivityData(TipusMoviment.DESPESA);
                            }
                        },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                picker.show();
                break;

        }
        return true;
    }

    public void updateMonthlyAmount() {
        TextView editText = (TextView) findViewById(R.id.monthly_amount);
        if(db.movimentsDao().getMovimentsByDate(this.initReportDate,this.endReportDate).size() == 0) {
            editText.setText("0.0" + divisa);
        } else {
            editText.setText(String.format("%.2f",(db.movimentsDao().getSumIngressosByDate(this.initReportDate,this.endReportDate) - db.movimentsDao().getSumDespesesByDate(this.initReportDate,this.endReportDate))) + " " + divisa);
        }
    }

    public void addMovimentActivity() {
        Intent intent = new Intent(this, AddMovimentActivity.class);
        startActivity(intent);
    }

    public void editMovimentIntent(int movimentid) {
        Intent intent = new Intent(this, AddMovimentActivity.class);
        intent.putExtra("editElement", movimentid);
        startActivity(intent);
    }

    public void changeTipusMoviment(View view) {
        if (view.getId() == R.id.ingressosButton) {
            setUpListView(TipusMoviment.INGRES);
            loadChartData(TipusMoviment.INGRES);
        } else {
            setUpListView(TipusMoviment.DESPESA);
            loadChartData(TipusMoviment.DESPESA);
        }
    }

    public void setUpListView(TipusMoviment tipusMoviment) {
        lv = (ListView) findViewById(R.id.llistaMoviments);

        List<String> llista = new ArrayList<String>();

        for (MovimentEntity moviment : getMoviments(tipusMoviment)) {
            llista.add(moviment.titol + ": " + String.format("%.2f", moviment.quantity) + " " + divisa);
        }

        MovimentListAdapter arrayAdapter = new MovimentListAdapter(this, R.layout.movements_list_item, llista, getMoviments(tipusMoviment), tipusMoviment);

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editMovimentIntent(getMoviments(tipusMoviment).get(position).id);
            }
        });

    }

    private List<MovimentEntity> getMoviments(TipusMoviment tipusMoviment) {

        List<MovimentEntity> llista;

        if (tipusMoviment == TipusMoviment.DESPESA) {
            llista = this.db.movimentsDao().getDespesesByDate(this.initReportDate,this.endReportDate);
        } else {
            llista = this.db.movimentsDao().getIngressosByDate(this.initReportDate,this.endReportDate);
        }

        return llista;
    }

    private List<GroupByTuple> getMovimentsGroupBy(TipusMoviment tipusMoviment) {
        List<GroupByTuple> llista;

        if (tipusMoviment == TipusMoviment.DESPESA) {
            llista = this.db.movimentsDao().getGroupByDespesesByDate(this.initReportDate,this.endReportDate);
        } else {
            llista = this.db.movimentsDao().getGroupByIngressosByDate(this.initReportDate,this.endReportDate);
        }

        return llista;
    }

    private List<GroupByTuple> convertClassificationToReadableString(List<GroupByTuple> llista) {
        List<GroupByTuple> convertedLlista = new ArrayList<GroupByTuple>();
        for (GroupByTuple item: llista) {
            switch(item.classification) {
                case "0":
                    item.classification = getResources().getString(R.string.billClass);
                    break;
                case "1":
                    item.classification = getResources().getString(R.string.transportClass);
                    break;
                case "2":
                    item.classification = getResources().getString(R.string.houseRent);
                    break;
                case "3":
                    item.classification = getResources().getString(R.string.compraClass);
                    break;
                case "4":
                    item.classification = getResources().getString(R.string.esportClass);
                    break;
                case "5":
                    item.classification = getResources().getString(R.string.presentsClass);
                    break;
                case "6":
                    item.classification = getResources().getString(R.string.gasClass);
                    break;
                case "7":
                    item.classification = getResources().getString(R.string.transferClass);
                    break;
                case "8":
                    item.classification = getResources().getString(R.string.varisClass);
                    break;
            }
            convertedLlista.add(item);
        }
        return llista;
    }

    private void loadChartData(TipusMoviment tipusMoviment) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (GroupByTuple moviment : convertClassificationToReadableString(getMovimentsGroupBy(tipusMoviment))) {
            entries.add(new PieEntry((float)moviment.totalMoviment, moviment.classification));
        }

        String label = "";

        if (tipusMoviment == TipusMoviment.DESPESA) {
            label = String.format("%.2f",(this.db.movimentsDao().getSumDespesesByDate(this.initReportDate, this.endReportDate))) + " " + divisa;
            if (entries.isEmpty()) {
                label = getResources().getString(R.string.no_expenses);
            } else {
                label = getResources().getString(R.string.mainOutcome) + " " + label;
            }
        } else {
            label = String.format("%.2f",(this.db.movimentsDao().getSumIngressosByDate(this.initReportDate, this.endReportDate))) + " " + divisa;
            if (entries.isEmpty()) {
                label = getResources().getString(R.string.no_income);
            } else {
                label = getResources().getString(R.string.mainIncome) + " " + label;
            }
        }
        pieChart.setCenterText(label);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, label);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterTextSize(17);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setEnabled(false);
    }

    private void setUpDB() {
        this.db = AppDatabase.getInstance(this);
    }

    public void createCurrencyPickerDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View classificationPickerPopupView = getLayoutInflater().inflate(R.layout.currency_list_selector, null);



        dialogBuilder.setView(classificationPickerPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        //Load listView data with currencies
        ListView lvAux = (ListView) classificationPickerPopupView.findViewById(R.id.currencyListView);

        List<String> llista = new ArrayList<String>();

        llista.add(Currency.getInstance("USD").toString());
        llista.add(Currency.getInstance("EUR").toString());
        llista.add(Currency.getInstance("GBP").toString());
        llista.add(Currency.getInstance("JPY").toString());
        llista.add(Currency.getInstance("AUD").toString());
        llista.add(Currency.getInstance("CAD").toString());
        llista.add(Currency.getInstance("CHF").toString());
        llista.add(Currency.getInstance("CNH").toString());
        llista.add(Currency.getInstance("HKD").toString());
        llista.add(Currency.getInstance("NZD").toString());
        llista.add(Currency.getInstance("INR").toString());


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, llista);

        lvAux.setAdapter(arrayAdapter);

        lvAux.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                divisa = llista.get(position);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("divisa",divisa);
                editor.apply();

                refreshActivityData(TipusMoviment.INGRES);
                refreshActivityData(TipusMoviment.DESPESA);
                dialog.dismiss();
            }
        });
    }

    public void refreshActivityData(TipusMoviment tm) {
        this.setUpListView(tm);
        this.loadChartData(tm);
        this.updateMonthlyAmount();
    }
}