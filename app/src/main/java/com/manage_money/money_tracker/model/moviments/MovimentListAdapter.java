package com.manage_money.money_tracker.model.moviments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.manage_money.money_tracker.MainActivity;
import com.manage_money.money_tracker.R;
import com.manage_money.money_tracker.database.database.AppDatabase;
import com.manage_money.money_tracker.database.entities.MovimentEntity;

import java.util.List;

public class MovimentListAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private List<String> movimentsList;
    private int layout;
    private List<MovimentEntity> moviments;
    private AppDatabase db;
    private TipusMoviment tipusMoviment;


    private void setUpDB(Context context) {
        this.db = AppDatabase.getInstance(context);
    }

    public MovimentListAdapter(Context context, int resourceId, List<String> llista, List<MovimentEntity> moviments, TipusMoviment tipusMoviment) {
        super(context, resourceId, llista);
        mContext = context;
        this.setUpDB(mContext);
        movimentsList = llista;
        layout = resourceId;
        this.moviments = moviments;
        this.tipusMoviment = tipusMoviment;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = null;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(layout, parent, false);
        viewHolder = new ViewHolder();
        viewHolder.edita = (ImageView) convertView.findViewById(R.id.editButton);
        viewHolder.elimina = (ImageView) convertView.findViewById(R.id.deleteButton);
        viewHolder.text = (TextView) convertView.findViewById(R.id.itemString);
        viewHolder.data = (TextView) convertView.findViewById(R.id.data);

        viewHolder.text.setText((CharSequence) getItem(position));
        viewHolder.data.setText((CharSequence) moviments.get(position).data);

        MovimentEntity moviment = moviments.get(position);

        viewHolder.edita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) mContext).editMovimentIntent(moviment.id);
            }
        });

        viewHolder.elimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.movimentsDao().deleteMoviment(moviment);
                ((MainActivity) mContext).refreshActivityData(tipusMoviment);
            }
        });
        return convertView;
    }
}

class ViewHolder {
    TextView text;
    TextView data;
    ImageView edita;
    ImageView elimina;
}
