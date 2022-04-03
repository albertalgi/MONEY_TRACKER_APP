package com.manage_money.money_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.manage_money.money_tracker.R;

import java.io.File;

public class ShowMovimentPic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_moviment_pic);

        getSupportActionBar().setTitle(getResources().getString(R.string.showMovimentPicTitle));
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_vector_test);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            ImageView movimentPic = (ImageView) findViewById(R.id.movimentPicElement);
            String picPath = extras.getString("picPath");
            if(picPath.equals("")) {
                movimentPic.setVisibility(View.GONE);
                TextView picText = (TextView) findViewById(R.id.emptyPicElement);
                picText.setVisibility(View.VISIBLE);
            } else {
                File f = new File(picPath);
                Bitmap image = BitmapFactory.decodeFile(picPath);
                movimentPic.setImageBitmap(image);
                movimentPic.setRotation(90);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}