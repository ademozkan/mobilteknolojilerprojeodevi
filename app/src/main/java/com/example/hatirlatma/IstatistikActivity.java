package com.example.hatirlatma;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class IstatistikActivity extends AppCompatActivity {
    ListView listeleGorev;
    LinearLayout linearCbox,linearHepsi;
    TextView tv_GorevEkle;
    int zo=1;
    SharedPreferences sharedPref;
    List<String> list;



    int perform=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istatistik);
        tv_GorevEkle=(TextView)findViewById(R.id.tv_GorevEkle);
        listeleGorev=(ListView) findViewById(R.id.lwGorev);
        linearHepsi=(LinearLayout)findViewById(R.id.lnr_Gorev);
        linearCbox=(LinearLayout)findViewById(R.id.lnrChxBx);
        FloatingActionButton btn_Hesapla=(FloatingActionButton)findViewById(R.id.btn_Istatistikhesap);
        SharedPreferences sharedPre = this.getPreferences(Context.MODE_PRIVATE);
        FrameLayout frameLayout=(FrameLayout)findViewById(R.id.frm2);
        String savedString = sharedPre.getString("renk","white");
        if (savedString.toString().equals("grey")){
            frameLayout.setBackgroundColor(Color.GRAY);
        }
        // Dropdown'a değerleri dolduruyoruz.


        btn_Hesapla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkbox işlemleri
                int count = linearCbox.getChildCount();
                for (int i = 0; i < count; i++) {
                    View vk = linearCbox.getChildAt(i);
                    if (vk instanceof CheckBox) {


                        if (((CheckBox) vk).isChecked()){
                            perform++;

                        };
                    }
                }
                int cıkar=count-1;
                double bolme=Float.valueOf(perform)/Float.valueOf(cıkar);

                final double performans=bolme*100;

                sharedPref = IstatistikActivity.this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                VeriTabani vt = new VeriTabani(IstatistikActivity.this);
                List<String> list = vt.VeriListeleCategori();
                int i=1;
                for (String sayi : list) { //sayılar dizisinin değeri tek tek sayi değişkenine atanıyor
                    String categori[]=sayi.split("-");
                    String catego[]=sayi.split(":");
                    String iki3=categori[1];
                    String degerli[] =iki3.split(":");
                    String veri=degerli[1];



                    View vk = linearCbox.getChildAt(i);
                    if (vk instanceof CheckBox) {


                        if (((CheckBox) vk).isChecked()){

                            editor.putBoolean(veri,true); //boolean değer ekleniyor
                             i++;

                        }else {
                            editor.putBoolean(veri,false);
                            i++;
                        }
                    }

                }
                editor.commit();


                AlertDialog.Builder builder = new AlertDialog.Builder(IstatistikActivity.this);
                builder.setTitle("Performansın ");
                builder.setMessage("%"+String.valueOf(performans));
                builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        perform=0;
                    }
                });
                builder.setPositiveButton("Ana Menü", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        perform=0;
                        Intent gecisYap = new Intent(IstatistikActivity.this, MainActivity.class);
                        startActivity(gecisYap);

                    }
                });
                builder.show();

            }
        });


        ListeleCat();

        //SharedPreferance islemleri kontroller

        for (String vere:list){
            linearCbox.addView(new CheckBox(IstatistikActivity.this));
            String categori[]=vere.split("-");
            String catego[]=vere.split(":");
            String iki3=categori[1];
            String degerli[] =iki3.split(":");
            String veri=degerli[1];
            sharedPref = IstatistikActivity.this.getPreferences(Context.MODE_PRIVATE);
            Boolean savedChecked = sharedPref.getBoolean(veri,false);
            View vk = linearCbox.getChildAt(zo);
            if (vk instanceof CheckBox) {

                ((CheckBox) vk).setChecked(savedChecked);
                zo++;
            }

        }
        zo=1;





        int count = linearCbox.getChildCount();

        for (int i = 0; i < count; i++) {
            View vk = linearCbox.getChildAt(i);
            if (vk instanceof CheckBox) {

                ((CheckBox) vk).setPadding(50,50,50,50);
            }
        }



    }


    public void ListeleCat(){
        VeriTabani vt = new VeriTabani(this);
        list = vt.VeriListeleCategori();








        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,android.R.id.text1,list){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                String categori[]=tv.getText().toString().split("-");
                String catego[]=tv.getText().toString().split(":");
                String iki3=categori[1];
                String degerli[] =iki3.split(":");
                String veri=degerli[1];
                tv.setText(veri);

                // Set the text color of TextView (ListView Item)





                // Generate ListView Item using TextView
                return view;

            }
        };
        listeleGorev.setAdapter(adapter);










        //Veri yokken ekranda kaybolması gerekenlerin kontrolu

        if(list.isEmpty()){
            linearHepsi.setVisibility(View.INVISIBLE);
            tv_GorevEkle.setVisibility(View.VISIBLE);
        }else{
            linearHepsi.setVisibility(View.VISIBLE);
            tv_GorevEkle.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(),"Liste Güncellendi.", Toast.LENGTH_LONG).show();
        }

    }


}
