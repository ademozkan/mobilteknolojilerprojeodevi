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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lvListele;
    TextView tvk;
    int idBul=0;
    public static int gecisId=-1;
    LinearLayout lnr_Kaybol;
    Spinner spnrtarihSirala;
    CheckBox cbx_Renklendir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton btnEkle=(FloatingActionButton)findViewById(R.id.btn_Gecis);
        FloatingActionButton btnAyar=(FloatingActionButton)findViewById(R.id.btn_ayarlar);
        FloatingActionButton btn_Istatistik=(FloatingActionButton)findViewById(R.id.btn_GecisIstatistik);
        lvListele=(ListView)findViewById(R.id.lwListele) ;
        tvk=(TextView)findViewById(R.id.txt_hatırlatmaEklemedin);
        lnr_Kaybol=(LinearLayout)findViewById(R.id.lnr_Kaybol);
        spnrtarihSirala=(Spinner)findViewById(R.id.spinner_Haftalk);
        cbx_Renklendir=(CheckBox)findViewById(R.id.cbox_Renklendir);


        // Dropdown'a değerleri dolduruyoruz.




        String[] sira = new String[]{"Hepsi","Günlük", "Haftalık", "Aylık"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sira);
        spnrtarihSirala.setAdapter(adapter);
        spnrtarihSirala.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if (spnrtarihSirala.getSelectedItem().toString().equals("Günlük")){
                    Listele1("gunluk");
                }
                if (spnrtarihSirala.getSelectedItem().toString().equals("Hepsi")){
                    Listele();
                }
                if (spnrtarihSirala.getSelectedItem().toString().equals("Haftalık")){
                    Listele1("haftalik");
                }
                if (spnrtarihSirala.getSelectedItem().toString().equals("Aylık")){
                    Listele1("aylik");
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        cbx_Renklendir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spnrtarihSirala.getSelectedItem().toString().equals("Günlük")){
                    Listele1("gunluk");
                }
                if (spnrtarihSirala.getSelectedItem().toString().equals("Hepsi")){
                    Listele();
                }
                if (spnrtarihSirala.getSelectedItem().toString().equals("Haftalık")){
                    Listele1("haftalik");
                }
                if (spnrtarihSirala.getSelectedItem().toString().equals("Aylık")){
                    Listele1("aylik");
                }
            }
        });





        btnEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gecisYap = new Intent(MainActivity.this, AlarmEklemeActivity.class);
                startActivity(gecisYap);
            }
        });

        btnAyar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gecisYap = new Intent(MainActivity.this, AyarlarActivity.class);
                startActivity(gecisYap);
            }
        });

        btn_Istatistik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gecisYap = new Intent(MainActivity.this, IstatistikActivity.class);
                startActivity(gecisYap);
            }
        });

        lvListele.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Tıklanan verimizi alıyoruz
                String item = lvListele.getItemAtPosition(position).toString();
                Long deger=lvListele.getSelectedItemId();
                TextView tr=(TextView) lvListele.getSelectedItem();
                //tr.setText("Ankara");



                // - Göre bölüyoruz
                String[] itemBol = item.split(" - ");
                // id'mizi alıyoruz
                idBul = Integer.valueOf(itemBol[0].toString());


                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Ne İstersin ?");
                builder.setMessage("Hatırlatma icin ne yapmak istersin?");
                builder.setNegativeButton("Sil", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String gelenId = String.valueOf(idBul);
                        VeriTabani vt = new VeriTabani(MainActivity.this);
                        vt.VeriSil(gelenId);
                        Toast.makeText(getApplicationContext(),"Silme İşlemi Tamamlandı.",Toast.LENGTH_LONG).show();
                        Listele();



                    }
                });
                builder.setPositiveButton("Guncelle veya Paylas", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gecisId=idBul;
                        Intent gecisYap = new Intent(MainActivity.this, AlarmEklemeActivity.class);
                        startActivity(gecisYap);
                    }
                });
                builder.show();
//                // Diğer verilerimizi set ediyoruz.
//                etAd.setText(itemBol[1].toString());
//                etSoyad.setText(itemBol[2].toString());
//                etTel.setText(itemBol[3].toString());



            }
        });

        //Listele();
    }

    public void Listele(){
        VeriTabani vt = new VeriTabani(MainActivity.this);
        List<String> list = vt.VeriListele();



        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,android.R.id.text1,list){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                String categori[]=tv.getText().toString().split("-");
                String zaman[]=tv.getText().toString().split("-");

                // Set the text color of TextView (ListView Item)
                if (cbx_Renklendir.isChecked()){
                    if (categori[3].toString().contains(" Categori:Gorev")){
                        tv.setTextColor(Color.RED);



                    }

                    if (categori[3].toString().contains(" Categori:Doğumgünü")){
                        tv.setTextColor(Color.GREEN);

                    }
                    if (categori[3].toString().contains(" Categori:Toplantı")){
                        tv.setTextColor(Color.BLUE);
                    }
                    if(categori[3].toString().contains(" Categori:Not")){
                        tv.setTextColor(Color.GRAY);
                    }

                }



                // Generate ListView Item using TextView
                return view;

            }
        };
        lvListele.setAdapter(adapter);





        //Veri yokken ekranda kaybolması gerekenlerin kontrolu
        lvListele.setAdapter(adapter);
        if(list.isEmpty()){
            tvk.setVisibility(View.VISIBLE);
            lnr_Kaybol.setVisibility(View.INVISIBLE);
        }else{
            tvk.setVisibility(View.INVISIBLE);
            lnr_Kaybol.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),"Liste Güncellendi.", Toast.LENGTH_LONG).show();
        }

    }


    public void Listele1(String veri){
        VeriTabani vt = new VeriTabani(MainActivity.this);
        List<String> list = vt.VeriListele1(veri);



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,android.R.id.text1,list){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                String categori[]=tv.getText().toString().split("-");
                String zaman[]=tv.getText().toString().split("-");

                // Set the text color of TextView (ListView Item)
                if (cbx_Renklendir.isChecked()){
                    if (categori[3].toString().contains(" Categori:Gorev")){
                        tv.setTextColor(Color.RED);



                    }

                    if (categori[3].toString().contains(" Categori:Doğumgünü")){
                        tv.setTextColor(Color.GREEN);

                    }
                    if (categori[3].toString().contains(" Categori:Toplantı")){
                        tv.setTextColor(Color.BLUE);
                    }
                    if(categori[3].toString().contains(" Categori:Not")){
                        tv.setTextColor(Color.GRAY);
                    }

                }


                // Generate ListView Item using TextView
                return view;

            }
        };
        lvListele.setAdapter(adapter);





        //Veri yokken ekranda kaybolması gerekenlerin kontrolu
        lvListele.setAdapter(adapter);
        if(list.isEmpty()){
            tvk.setVisibility(View.VISIBLE);
            lnr_Kaybol.setVisibility(View.INVISIBLE);
        }else{
            tvk.setVisibility(View.INVISIBLE);
            lnr_Kaybol.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),"Liste Güncellendi.", Toast.LENGTH_LONG).show();
        }

    }


}
