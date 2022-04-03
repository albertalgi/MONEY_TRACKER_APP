package com.manage_money.money_tracker;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.manage_money.money_tracker.R;
import com.manage_money.money_tracker.database.database.AppDatabase;
import com.manage_money.money_tracker.database.entities.MovimentEntity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddMovimentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private boolean isIngres = true;
    private boolean isEdit = false;
    private int elementToBeEditedId = 0;

    private EditText classMoviment;

    //Classification dialog
    private TextView calendarDate;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    //For classification purposes
    int classificacioMoviment = -1;

    //For the taken pictures
    String picPath = "";
    Bitmap movimentPic = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_moviment);

        getSupportActionBar().setTitle(getResources().getString(R.string.addEdit));

        classMoviment = (EditText) findViewById(R.id.classifInputAddMovement);
        classMoviment.setFocusable(false);

        // Date picker setup
        calendarDate = findViewById(R.id.dateInputAddMovement);
        calendarDate.setFocusable(false);
        findViewById(R.id.dateInputAddMovement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        //Check if extras exist
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            int movimentId = extras.getInt("editElement");
            try {
                setEditAttributes(movimentId);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_moviment_menu, menu);
        return true;
    }

    //Handle image pic
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.openCamAddMoviment:
                if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                    if (ContextCompat.checkSelfPermission(AddMovimentActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        //Request CAM permission
                        ActivityCompat.requestPermissions(AddMovimentActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
                    } else {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //startActivityForResult(takePictureIntent,100);
                        File pic = null;
                        try {
                            pic = createImageFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (pic != null) {
                            Uri photoUri = FileProvider.getUriForFile(this, "com.manage_money.money_tracker", pic);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            try {
                                startActivityForResult(takePictureIntent, 100);
                            } catch (ActivityNotFoundException e) {
                                Log.e("Cam ERROR", e.toString());
                            }
                        }
                    }
                }
                break;
            case R.id.openPicMoviment:
                Intent intent = new Intent(this, ShowMovimentPic.class);
                intent.putExtra("picPath",this.picPath);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(takePictureIntent,100);
        File pic = null;
        try {
            pic = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (pic != null) {
            Uri photoUri = FileProvider.getUriForFile(this, "com.manage_money.money_tracker", pic);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            try {
                startActivityForResult(takePictureIntent, 100);
            } catch (ActivityNotFoundException e) {
                Log.e("Cam ERROR", e.toString());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK) {
            File f = new File(this.picPath);
            this.movimentPic = BitmapFactory.decodeFile(this.picPath);
            Log.i("IMATGE recuperada", this.picPath);
            Log.i("IMATGE", String.valueOf(this.movimentPic));
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        this.picPath = image.getAbsolutePath();
        Log.i("PIC_PATH",this.picPath);
        return image;
    }

    public void createClassificationPickerDialog(View v) {
        dialogBuilder = new AlertDialog.Builder(this);
        final View classificationPickerPopupView = getLayoutInflater().inflate(R.layout.classification_selection, null);

        dialogBuilder.setView(classificationPickerPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    public void onClassificationClicked(View view) {
        switch(view.getId()) {
            case R.id.facturesClass:
                classificacioMoviment = 0;
                classMoviment.setText(R.string.billClass);
                break;
            case R.id.transportClass:
                classificacioMoviment = 1;
                classMoviment.setText(R.string.transportClass);
                break;
            case R.id.rentClass:
                classificacioMoviment = 2;
                classMoviment.setText(R.string.houseRent);
                break;
            case R.id.groceriesClass:
                classificacioMoviment = 3;
                classMoviment.setText(R.string.compraClass);
                break;
            case R.id.sportClass:
                classificacioMoviment = 4;
                classMoviment.setText(R.string.esportClass);
                break;
            case R.id.presentClass:
                classificacioMoviment = 5;
                classMoviment.setText(R.string.presentsClass);
                break;
            case R.id.gasClass:
                classificacioMoviment = 6;
                classMoviment.setText(R.string.gasClass);
                break;
            case R.id.transferClass:
                classificacioMoviment = 7;
                classMoviment.setText(R.string.transferClass);
                break;
            case R.id.othersClass:
                classificacioMoviment = 8;
                classMoviment.setText(R.string.varisClass);
                break;
        }
        dialog.dismiss();
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month = month+1;
        //calendarDate.setText(dayOfMonth+"/"+month+"/"+year);
        //The following format is the right one to handle SQLite queries by Date "yyyy-MM-dd"
        calendarDate.setText(String.format("%02d",year)+"-"+String.format("%02d",month)+"-"+String.format("%02d",dayOfMonth));
    }

    private void setEditAttributes(int movimentId) throws InterruptedException {
        //Init DB
        AppDatabase db = AppDatabase.getInstance(this);

        MovimentEntity movimentAux = db.movimentsDao().getMovimentById(movimentId);

        //Set picture path from DB
        this.picPath = movimentAux.picturePath;

        //Get Attributes
        EditText titl = (EditText)findViewById(R.id.titleInputAddMovement);
        EditText quant = (EditText)findViewById(R.id.quantityInputAddMovement);
        EditText dat = (EditText)findViewById(R.id.dateInputAddMovement);
        EditText observ = (EditText)findViewById(R.id.observInputAddMovement);
        EditText classif = (EditText)findViewById(R.id.classifInputAddMovement);

        titl.setText(movimentAux.titol);
        quant.setText(String.format("%.2f", movimentAux.quantity));
        dat.setText(movimentAux.data);
        observ.setText(movimentAux.observacions);

        switch(movimentAux.classification) {
            case "0":
                classif.setText(getResources().getString(R.string.billClass));
                classificacioMoviment = 0;
                break;
            case "1":
                classif.setText(getResources().getString(R.string.transportClass));
                classificacioMoviment = 1;
                break;
            case "2":
                classif.setText(getResources().getString(R.string.houseRent));
                classificacioMoviment = 2;
                break;
            case "3":
                classif.setText(getResources().getString(R.string.compraClass));
                classificacioMoviment = 3;
                break;
            case "4":
                classif.setText(getResources().getString(R.string.esportClass));
                classificacioMoviment = 4;
                break;
            case "5":
                classif.setText(getResources().getString(R.string.presentsClass));
                classificacioMoviment = 5;
                break;
            case "6":
                classif.setText(getResources().getString(R.string.gasClass));
                classificacioMoviment = 6;
                break;
            case "7":
                classif.setText(getResources().getString(R.string.transferClass));
                classificacioMoviment = 7;
                break;
            case "8":
                classif.setText(getResources().getString(R.string.varisClass));
                classificacioMoviment = 8;
                break;
        }

        this.isEdit = true;
        this.elementToBeEditedId = movimentId;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.incomeInputAddMovement:
                if (checked)
                    isIngres = true;
                    break;
            case R.id.outcomeInputAddMovement:
                if (checked)
                    isIngres = false;
                    break;
        }
    }

    public void addMovement(View view) {
        Context context = getApplicationContext();
        CharSequence text = getResources().getString(R.string.addEditToast);
        int duration = Toast.LENGTH_SHORT;

        if (validateInputs()) { //All inputs are correct and can be added to database and return to MainActivity.
            //Init DB
            AppDatabase db = AppDatabase.getInstance(this);

            //Get Attributes
            EditText titl = (EditText)findViewById(R.id.titleInputAddMovement);
            EditText quant = (EditText)findViewById(R.id.quantityInputAddMovement);
            EditText dat = (EditText)findViewById(R.id.dateInputAddMovement);
            EditText observ = (EditText)findViewById(R.id.observInputAddMovement);

            String titol = titl.getText().toString();
            double quantity = Double.parseDouble(quant.getText().toString().replace(",","."));
            String data = dat.getText().toString();
            String tipus;

            if (isIngres) {
                tipus = "INGRES";
            } else {
                tipus = "DESPESA";
            }

            String observacions = observ.getText().toString();
            String classificacio = Integer.toString(this.classificacioMoviment);

            if(this.isEdit) {
                db.movimentsDao().updateMoviment(this.elementToBeEditedId, tipus, quantity, titol, data, observacions, classificacio, this.picPath);
            } else {
                MovimentEntity newMoviment = new MovimentEntity(titol, quantity, tipus, data, observacions, classificacio, this.picPath);
                //Insert or update to database
                db.movimentsDao().insertMoviment(newMoviment);
            }

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }

    public void cancelar(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private boolean validateInputs() {
        //Get Attributes
        EditText titl = (EditText)findViewById(R.id.titleInputAddMovement);
        EditText quant = (EditText)findViewById(R.id.quantityInputAddMovement);
        EditText dat = (EditText)findViewById(R.id.dateInputAddMovement);
        EditText classif = (EditText)findViewById(R.id.classifInputAddMovement);
        RadioButton despesesRadio= (RadioButton)findViewById(R.id.outcomeInputAddMovement);
        RadioButton ingressosRadio= (RadioButton)findViewById(R.id.incomeInputAddMovement);

        String text = titl.getText().toString();
        if(text == null || text.length() == 0) {
            titl.setError(getResources().getText(R.string.AddMovementValidator));
            return false;
        }

        if(!despesesRadio.isChecked() && !ingressosRadio.isChecked()) {
            Toast toast = Toast.makeText(this, getResources().getText(R.string.AddMovementValidatorRadioButton), Toast.LENGTH_LONG);
            toast.show();
            return false;
        }

        text = quant.getText().toString();
        if(text == null || text.length() == 0) {
            quant.setError(getResources().getText(R.string.AddMovementValidator));
            return false;
        }
        text = dat.getText().toString();
        if(text == null || text.length() == 0) {
            dat.setError(getResources().getText(R.string.AddMovementValidator));
            return false;
        }
        text = classif.getText().toString();
        if(text == null || text.length() == 0) {
            classif.setError(getResources().getText(R.string.AddMovementValidator));
            return false;
        }

        return true;
    }
}