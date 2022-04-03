package com.manage_money.money_tracker.reports;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.manage_money.money_tracker.R;
import com.manage_money.money_tracker.database.entities.Report;

import java.util.List;

public class ReportsGridAdapter extends BaseAdapter {
    Context context;
    List<Report> reports;
    int layout;
    public ReportsGridAdapter(Context context, List<Report> dates, int layoutId) {
        this.context = context;
        this.reports = dates;
        this.layout = layoutId;
    }

    @Override
    public int getCount() {
        return reports.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        convertView = inflater.inflate(layout, parent, false);
        TextView monthYear = (TextView) convertView.findViewById(R.id.gridViewElementMonth);
        monthYear.setText(this.reports.get(position).initDate);
        return convertView;
    }


}
