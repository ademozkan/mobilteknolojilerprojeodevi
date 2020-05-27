package com.example.hatirlatma;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.example.hatirlatma.MainActivity.gecisId;


public class AlarmEklemeActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;

    public final String MyPREFERENCES = "MyPrefs" ;
    public static final String ayrlyil = "alarmyili";
    public static final String ayrlay = "alarmayi";
    public static final String ayrlgün = "alarmgünü";
    public static final String ayrlsaat = "alarmsaati";
    public static final String ayrldakika = "alarmdakika";
    CheckBox cboxAlarmEkle;
    LinearLayout lnrTarih;
    EditText txtTarih,txt_Saat,txt_Baslık,txt_Not;
    Context context=this;
    TextView tv_AtarlananTarih;
    Spinner spinner1;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    String paylasımMesajı;

    final Calendar takvim = Calendar.getInstance();
    // Şimdiki zaman bilgilerini alıyoruz. güncel yıl, güncel ay, güncel gün.
    int yil = takvim.get(Calendar.YEAR);
    int ay = takvim.get(Calendar.MONTH);
    int gun = takvim.get(Calendar.DAY_OF_MONTH);
    // Şimdiki zaman bilgilerini aldık. güncel saat, güncel dakika.

    int saat = takvim.get(Calendar.HOUR_OF_DAY);
    int dakika = takvim.get(Calendar.MINUTE);
    int gelenyil;
    int gelenay;
    int gelengün;
    int gelensaat;
    int gelendakika;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ekleme);
        SharedPreferences sharedPre = this.getPreferences(Context.MODE_PRIVATE);
        FrameLayout frameLayout=(FrameLayout)findViewById(R.id.frame);
        String savedString = sharedPre.getString("renk","white");
        if (savedString.toString().equals("grey")){
            frameLayout.setBackgroundColor(Color.GRAY);
        }

        cboxAlarmEkle=(CheckBox)findViewById(R.id.cbox_AlarmEkle);
        lnrTarih=(LinearLayout)findViewById(R.id.lnr_Tarih);
        txtTarih=(EditText)findViewById(R.id.txt_Tarih);
        txt_Saat=(EditText)findViewById(R.id.txt_Saat);
        txt_Not=(EditText)findViewById(R.id.txt_Not);
        txt_Baslık=(EditText)findViewById(R.id.txt_Baslık);
        tv_AtarlananTarih=(TextView)findViewById(R.id.tv_AyarlananTarih);
        FloatingActionButton btn_Ekle=(FloatingActionButton)findViewById(R.id.btn_Ekle);
        FloatingActionButton btn_Paylas=(FloatingActionButton)findViewById(R.id.btn_Paylas);
        txt_Baslık.requestFocus();
        spinner1=(Spinner)findViewById(R.id.spinner_Sirala);

        if (gecisId!=-1){
            VeriTabani veriTabani=new VeriTabani(AlarmEklemeActivity.this);
            String geldibaslık= veriTabani.iddenBaslık(gecisId);
            txt_Baslık.setText(geldibaslık);
            String geldiNot= veriTabani.iddenNot(gecisId);
            txt_Not.setText(geldiNot);
            String geldiCategory= veriTabani.iddenCategory(gecisId);

        }



        // Dropdown'a değerleri dolduruyoruz.
        //dropdown = findViewById(R.id.spinner1);
        String[] categori = new String[]{"Gorev", "Doğumgünü", "Toplantı","Not"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categori);
        spinner1.setAdapter(adapter);


        btn_Paylas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String karsılas=txt_Baslık.getText().toString();
                if (karsılas.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Lütfen Baslık Girin",Toast.LENGTH_LONG).show();
                }else {

                    paylasımMesajı=(spinner1.getSelectedItem().toString())+"-"+txt_Baslık.getText().toString()+"-"+txt_Not.getText().toString();

                    AlertDialog.Builder builder = new AlertDialog.Builder(AlarmEklemeActivity.this);
                    builder.setTitle("WhatsApp,Mail,Messenger,Mesaj,Isntagram ile Paylas");
                    builder.setMessage(paylasımMesajı);
                    builder.setNegativeButton("İptal", null);
                    builder.setPositiveButton("Paylas", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.setType("text/plain"); // text/html
                            sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,spinner1.getSelectedItem().toString());
                            sendIntent.putExtra(Intent.EXTRA_TEXT, paylasımMesajı);
                            startActivity(Intent.createChooser(sendIntent, "Share"));


                        }
                    });
                    builder.show();

                }

            }
        });





        //Alarm ekle Checkboxını dinleme metodu
        cboxAlarmEkle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked==false){
                    lnrTarih.setVisibility(View.GONE);
                }else{
                    lnrTarih.setVisibility(View.VISIBLE);
                }
                int ay1=ay+1;
                txtTarih.setText(gun+"/"+ay1+"/"+yil);
                int deger=dakika+5;
                txt_Saat.setText(saat + ":" + deger);
            }
        });

        //txtTarih click İventini dinleme
        txtTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTarih.setText("");

                final DatePickerDialog dpd = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                gelenay=month;

                                // ay değeri 0 dan başladığı için (Ocak=0, Şubat=1,..,Aralık=11)
                                // değeri 1 artırarak gösteriyoruz.
                                month += 1;
                                // year, month ve dayOfMonth değerleri seçilen tarihin değerleridir.
                                // Edittextte bu değerleri gösteriyoruz.
                                gelenyil=year;

                                gelengün=dayOfMonth;

                                String gun1=String.valueOf(dayOfMonth);
                                String ay1 = String.valueOf(month);


                                txtTarih.setText(gun1 + "/" + ay1 + "/" + year);

//

                            }
                            // datepicker açıldığında set edilecek değerleri buraya yazıyoruz.
                            // şimdiki zamanı göstermesi için yukarda tanmladığımz değşkenleri kullanyoruz.
                        }, yil, ay, gun);
                dpd.setTitle("Tarih Seç");
                dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", dpd);
                dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "", dpd);

                dpd.show();

            }
        });

        //txtTarih click İventini dinleme
        txt_Saat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_Saat.setText("");


                TimePickerDialog tpd = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // hourOfDay ve minute değerleri seçilen saat değerleridir.
                                // Edittextte bu değerleri gösteriyoruz.

                                String saat1=String.valueOf(hourOfDay);
                                String dakika1 = String.valueOf(minute);
                                gelensaat=hourOfDay;
                                gelendakika=minute;



                                txt_Saat.setText(saat1 + ":" + dakika1);


                            }

                            // timepicker açıldığında set edilecek değerleri buraya yazıyoruz.
                            // şimdiki zamanı göstermesi için yukarda tanımladğımız değişkenleri kullanıyoruz.
                            // true değeri 24 saatlik format için.
                        }, saat, dakika, true);
                tpd.setTitle("SAAT SEÇ");

                tpd.show();

            }
        });



        btn_Ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AlarmEklemeActivity.this);
                if (txt_Baslık.getText().toString().trim().equals("") && txt_Not.getText().toString().trim().equals("")){
                    builder.setTitle("Alarm Uyarı");
                    builder.setMessage("Baslık veya notu girmediniz!!");
                    builder.setPositiveButton("Tamam", null);


                    builder.show();
                }else{
                    if (cboxAlarmEkle.isChecked()==true){
                        builder.setTitle("Alarm Uyarı");
                        builder.setMessage("Alarm "+txtTarih.getText().toString()+" - "+txt_Saat.getText().toString()+" zamanına ayarlandı. Onaylıyormusun?");
                        builder.setNegativeButton("Hayır", null);
                        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {






                                String gelenBaslık = txt_Baslık.getText().toString();
                                String gelenNot =txt_Not .getText().toString();
                                String gelenCategori = spinner1.getSelectedItem().toString();
                                String[] gelenzaman = {txtTarih.getText().toString() + " " + txt_Saat.getText().toString()};

                                if (gecisId!=-1){
                                    //Güncelleme işlemleri
                                    // Veritabanı bağlantımızı açalım ver ardından gerekli bilgileri VeriDuzenle metotuna gönderelim
                                    if (cboxAlarmEkle.isChecked()==false){

                                    }
                                    VeriTabani vt = new VeriTabani(AlarmEklemeActivity.this);
                                    vt.VeriDuzenle(gecisId,gelenNot,gelenCategori,gelenBaslık, gelenzaman[0]);

                                }else{
                                    //Ekle işlemleri
                                    alarmMgr= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                    Calendar objCalendar = Calendar.getInstance();


                                    objCalendar.set(Calendar.YEAR, objCalendar.get(Calendar.YEAR));
                                    Calendar cal =Calendar.getInstance();
                                    String zaman1[]= txtTarih.getText().toString().split("/");
                                    String saat1[]= txt_Saat.getText().toString().split(":");
                                    int ghhjk=cal.get(Calendar.MONTH);
                                    int jkk=cal.get(Calendar.DAY_OF_MONTH);
                                    objCalendar.set(Calendar.YEAR,Integer.valueOf(zaman1[2]));
                                    objCalendar.set(Calendar.MONTH,Integer.valueOf(zaman1[1])-1);
                                    objCalendar.set(Calendar.DAY_OF_MONTH,Integer.valueOf(zaman1[0]));

                                    objCalendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(saat1[0]));
                                    objCalendar.set(Calendar.MINUTE, Integer.valueOf(saat1[1]));
                                    objCalendar.set(Calendar.SECOND, 0);
                                    objCalendar.set(Calendar.MILLISECOND,0);



                                    Intent alamShowIntent = new Intent(AlarmEklemeActivity.this,AlarmReceiver.class);

                                    alamShowIntent.putExtra("veriadi",txt_Baslık.getText().toString());

                                    alarmIntent= PendingIntent.getBroadcast(AlarmEklemeActivity.this, 0,alamShowIntent,0 );

                                    alarmMgr.set(AlarmManager.RTC_WAKEUP,objCalendar.getTimeInMillis(), alarmIntent);


                                    VeriTabani vt = new VeriTabani(AlarmEklemeActivity.this);
                                    vt.VeriEkle(gelenNot, gelenCategori, gelenBaslık, gelenzaman[0]);
                                    Intent gecisYap = new Intent(AlarmEklemeActivity.this, MainActivity.class);
                                    startActivity(gecisYap);

                                }

//                                alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//                                Intent intent = new Intent(context, AlarmReceiver.class);
//                                alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//
//                                alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                                        SystemClock.elapsedRealtime() +
//                                                10 * 1000, alarmIntent);
//
//
//
//
//                                Intent gecisYap = new Intent(AlarmEklemeActivity.this, MainActivity.class);
//                                Toast.makeText(getApplicationContext(),"Ekleme veya Güncelleme işlemi yapıldı",Toast.LENGTH_LONG).show();
//                                startActivity(gecisYap);





                            }
                        });
                        builder.show();
                    }else{
                        builder.setTitle("Alarm Uyarı");
                        builder.setMessage("Alarm Eklenmedi. Zamana Şuanın Tarihi Atılacak. Onaylıyormusun?");
                        builder.setNegativeButton("Hayır", null);
                        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ay=ay+1;
                                String gelenBaslık = txt_Baslık.getText().toString();
                                String gelenNot =txt_Not .getText().toString();
                                String gelenCategori = spinner1.getSelectedItem().toString();
                                //Alarm eklenmesse Simdiki zamanın tarihini Ekle veritabanına
                                String[] gelenzaman = {gun+"/"+ay+"/"+yil + " " + saat+":"+dakika};

                                if (gecisId!=-1){
                                    //Güncelleme işlemleri
                                    // Veritabanı bağlantımızı açalım ver ardından gerekli bilgileri VeriDuzenle metotuna gönderelim
                                    if (cboxAlarmEkle.isChecked()==false){

                                    }
                                    VeriTabani vt = new VeriTabani(AlarmEklemeActivity.this);
                                    vt.VeriDuzenle(gecisId,gelenNot,gelenCategori,gelenBaslık, gelenzaman[0]);

                                }else{
                                    //Ekle işlemleri
                                    VeriTabani vt = new VeriTabani(AlarmEklemeActivity.this);
                                    vt.VeriEkle(gelenNot, gelenCategori, gelenBaslık, gelenzaman[0]);

                                }

//                                alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//                                Intent intent = new Intent(context, AlarmReceiver.class);
//                                alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//
//                                alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                                        SystemClock.elapsedRealtime() +
//                                                10 * 1000, alarmIntent);
//
//
//
//                                Intent gecisYap = new Intent(AlarmEklemeActivity.this, MainActivity.class);
//                                Toast.makeText(getApplicationContext(),"Ekleme veya Güncelleme işlemi yapıldı",Toast.LENGTH_LONG).show();
//                                startActivity(gecisYap);

                            }
                        });
                        builder.show();

                    }

                }




            }
        });










    }
}
