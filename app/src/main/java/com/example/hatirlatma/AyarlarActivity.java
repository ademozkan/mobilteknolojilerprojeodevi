package com.example.hatirlatma;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class AyarlarActivity extends AppCompatActivity {
    Button btn;
    TextView txtView;
    String renk="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);
        TextView txtVarTarih=(TextView)findViewById(R.id.textViewZaman);
        final RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.rltvLayout);
        btn = findViewById(R.id.btnSelRingtone);
        txtView = findViewById(R.id.tvRingtone);

        final Spinner spinerMod=(Spinner)findViewById(R.id.spinnerMod);
        String[] sira = new String[]{"Light","Grey"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sira);
        spinerMod.setAdapter(adapter);
        spinerMod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if (spinerMod.getSelectedItem().toString().equals("Grey")){
                    relativeLayout.setBackgroundColor(Color.GRAY);
                    renk="grey";


                }else{
                    relativeLayout.setBackgroundColor(Color.WHITE);
                    renk="white";
                }

                SharedPreferences sharedPre = AyarlarActivity.this.getSharedPreferences("sharedPre",Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPre.edit();

                editor.putString("renk",renk); //string değer ekleniyor



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        Calendar rightNow = Calendar.getInstance();
        int yil=Calendar.YEAR;
        int ay=Calendar.MONTH;
        int gün=Calendar.DAY_OF_MONTH;
        int saat=Calendar.HOUR_OF_DAY;
        int dakika=Calendar.MINUTE;
        txtVarTarih.setText(gün+"/"+ay+"/"+yil+"/"+"  "+saat+":"+dakika);





        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent to select Ringtone.
                final Uri currentTone=
                        RingtoneManager.getActualDefaultRingtoneUri(AyarlarActivity.this,
                                RingtoneManager.TYPE_ALARM);
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentTone);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                startActivityForResult(intent, 999);
            }
        });
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri=null;


        if (requestCode == 999 && resultCode == RESULT_OK) {
            uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            txtView.setText("From :" + uri.getPath());
        }
        Intent intent =new Intent(AyarlarActivity.this,AlarmReceiver.class);
        Bundle bund=new Bundle();
        bund.putString("url", uri.getPath());

        intent.putExtras(bund);

        sendBroadcast(intent);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
