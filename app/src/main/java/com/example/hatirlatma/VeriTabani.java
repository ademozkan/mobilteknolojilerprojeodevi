package com.example.hatirlatma;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VeriTabani extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "hatirlatmalar";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLO_BASLIK = "baslık";
    private static final String ROW_ID = "id";
    private static final String ROW_NT = "nt";
    private static final String ROW_CATEGORY = "categori";
    private static final String ROW_BSLK = "bslk";
    private static final String ZAMAN = "zaman";
    int saydır1=0;




    public VeriTabani(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLO_BASLIK + "("
                + ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ROW_NT + " TEXT NOT NULL, "
                + ROW_CATEGORY + " TEXT NOT NULL, "
                + ZAMAN + " TEXT NOT NULL,"
                + ROW_BSLK + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLO_BASLIK);
        onCreate(db);
    }

//    private String getDateTime() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat(
//                "dd/MM/yyyy HH:mm", Locale.getDefault());
//        Date date = new Date();
//        return dateFormat.format(date);
//    }

    public void VeriEkle(String nt, String categori, String baslık,String zaman){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(ROW_NT, nt);
            cv.put(ROW_CATEGORY, categori);
            cv.put(ZAMAN, zaman);
            cv.put(ROW_BSLK, baslık);
            db.insert(TABLO_BASLIK, null,cv);
        }catch (Exception e){

        }
        db.close();
    }

    public List<String> VeriListele(){
        List<String> veriler = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String[] stunlar = {ROW_ID,ROW_BSLK,ROW_NT,ROW_CATEGORY, ZAMAN};
            Cursor cursor = db.query(TABLO_BASLIK, stunlar,null,null,null,null,null);
            //mevzu burda

            while (cursor.moveToNext()){

                veriler.add(cursor.getInt(0)+" - "+"Başlık:"+cursor.getString(1) + " - " +" Not:" +cursor.getString(2) + " - " +" Categori:"+ cursor.getString(3) + " - " + "Hatırlatma Zamanı="+cursor.getString(4));

            }
        }catch (Exception e){

        }
        db.close();
        return veriler;
    }
    public List<String> VeriListele1(String haftalik){
        List<String> veriler = new ArrayList<String>();
        if (haftalik.equals("aylik")){

            SQLiteDatabase db = this.getReadableDatabase();
            final java.util.Calendar takvim = java.util.Calendar.getInstance();
            // Şimdiki zaman bilgilerini alıyoruz. güncel yıl, güncel ay, güncel gün.
            int yil = takvim.get(java.util.Calendar.YEAR);
            int ay = takvim.get(java.util.Calendar.MONTH);
            int gun = takvim.get(java.util.Calendar.DAY_OF_MONTH);
            // Şimdiki zaman bilgilerini aldık. güncel saat, güncel dakika.

            int saat = takvim.get(java.util.Calendar.HOUR_OF_DAY);
            int dakika = takvim.get(Calendar.MINUTE);

            try {
                String[] stunlar = {ROW_ID,ROW_BSLK,ROW_NT,ROW_CATEGORY, ZAMAN};
                Cursor cursor = db.query(TABLO_BASLIK, stunlar,null,null,null,null,null);
                int saydır=0;
                while (cursor.moveToNext()){


                    veriler.add(cursor.getInt(0)+" - "+"Başlık:"+cursor.getString(1) + " - " +" Not:" +cursor.getString(2) + " - " +" Categori:"+ cursor.getString(3) + " - " + "Hatırlatma Zamanı="+cursor.getString(4));





                }
                for (String sayi : veriler) { //sayılar dizisinin değeri tek tek sayi değişkenine atanıyor
                    String ss=sayi;
                    String zz[]= ss.toString().split("=");
                    String bir= zz[0];
                    String[] iki=zz[1].split(" ");
                    String[] verilertarih=iki[0].split("/");
                    String[] verilersaat=iki[1].split(":");
                    int gundegeri =Integer.valueOf(verilertarih[0])*86400;
                    int aydegeri =Integer.valueOf(verilertarih[1])*2629743;
                    int toplamData=gundegeri+aydegeri;
                    int toplam=((1+ay)*2629743)+(gun*86400);
                    int ifsonuc=toplamData-toplam;
                    if ((0<=ifsonuc)){
                        if ((ifsonuc<2592000)){


                        }else{
                            veriler.remove(sayi);
                            saydır=saydır+1;
                        }

                    }
                    if ((0>=ifsonuc)){
                        if ((ifsonuc>-2592000)){

                        }else{
                            veriler.remove(sayi);
                            saydır=saydır+1;
                        }

                    }


                }
            }catch (Exception e){

            }
            db.close();


        }

        if (haftalik.equals("gunluk")){

            SQLiteDatabase db = this.getReadableDatabase();
            final java.util.Calendar takvim = java.util.Calendar.getInstance();
            // Şimdiki zaman bilgilerini alıyoruz. güncel yıl, güncel ay, güncel gün.
            int yil = takvim.get(java.util.Calendar.YEAR);
            int ay = takvim.get(java.util.Calendar.MONTH);
            int gun = takvim.get(java.util.Calendar.DAY_OF_MONTH);
            // Şimdiki zaman bilgilerini aldık. güncel saat, güncel dakika.

            int saat = takvim.get(java.util.Calendar.HOUR_OF_DAY);
            int dakika = takvim.get(Calendar.MINUTE);

            try {
                String[] stunlar = {ROW_ID,ROW_BSLK,ROW_NT,ROW_CATEGORY, ZAMAN};
                Cursor cursor = db.query(TABLO_BASLIK, stunlar,null,null,null,null,null);
                int saydır=0;
                while (cursor.moveToNext()){


                        veriler.add(cursor.getInt(0)+" - "+"Başlık:"+cursor.getString(1) + " - " +" Not:" +cursor.getString(2) + " - " +" Categori:"+ cursor.getString(3) + " - " + "Hatırlatma Zamanı="+cursor.getString(4));





                }
                for (String sayi : veriler) { //sayılar dizisinin değeri tek tek sayi değişkenine atanıyor
                    String ss=sayi;
                    String zz[]= ss.toString().split("=");
                    String bir= zz[0];
                    String[] iki=zz[1].split(" ");
                    String[] verilertarih=iki[0].split("/");
                    String[] verilersaat=iki[1].split(":");
                    int gundegeri =Integer.valueOf(verilertarih[0])*86400;
                    int aydegeri =Integer.valueOf(verilertarih[1])*2629743;
                    int toplamData=gundegeri+aydegeri;
                    int toplam=((1+ay)*2629743)+(gun*86400);
                    int ifsonuc=toplamData-toplam;
                    if ((0<=ifsonuc)){
                        if ((ifsonuc<86400)){


                        }else{
                            veriler.remove(sayi);
                            saydır=saydır+1;
                        }

                    }
                    if ((0>=ifsonuc)){
                        if ((ifsonuc>-86400)){

                        }else{
                            veriler.remove(sayi);
                            saydır=saydır+1;
                        }

                    }


                }
            }catch (Exception e){

            }
            db.close();


            for (String jds:veriler){
                String asda=jds;
                String adsf;
                String affds="566";
            }


        }
        if (haftalik.equals("haftalik")){

            SQLiteDatabase db = this.getReadableDatabase();
            final java.util.Calendar takvim = java.util.Calendar.getInstance();
            // Şimdiki zaman bilgilerini alıyoruz. güncel yıl, güncel ay, güncel gün.
            int yil = takvim.get(java.util.Calendar.YEAR);
            int ay = takvim.get(java.util.Calendar.MONTH);
            int gun = takvim.get(java.util.Calendar.DAY_OF_MONTH);
            // Şimdiki zaman bilgilerini aldık. güncel saat, güncel dakika.

            int saat = takvim.get(java.util.Calendar.HOUR_OF_DAY);
            int dakika = takvim.get(Calendar.MINUTE);

            try {
                String[] stunlar = {ROW_ID,ROW_BSLK,ROW_NT,ROW_CATEGORY, ZAMAN};
                Cursor cursor = db.query(TABLO_BASLIK, stunlar,null,null,null,null,null);
                int saydır=0;
                while (cursor.moveToNext()){


                    veriler.add(cursor.getInt(0)+" - "+"Başlık:"+cursor.getString(1) + " - " +" Not:" +cursor.getString(2) + " - " +" Categori:"+ cursor.getString(3) + " - " + "Hatırlatma Zamanı="+cursor.getString(4));





                }
                for (String sayi : veriler) { //sayılar dizisinin değeri tek tek sayi değişkenine atanıyor
                    String ss=sayi;
                    String zz[]= ss.toString().split("=");
                    String bir= zz[0];
                    String[] iki=zz[1].split(" ");
                    String[] verilertarih=iki[0].split("/");
                    String[] verilersaat=iki[1].split(":");
                    int gundegeri =Integer.valueOf(verilertarih[0])*86400;
                    int aydegeri =Integer.valueOf(verilertarih[1])*2629743;
                    int toplamData=gundegeri+aydegeri;
                    int toplam=((1+ay)*2629743)+(gun*86400);
                    int ifsonuc=toplamData-toplam;
                    if ((0<=ifsonuc)){
                        if ((ifsonuc<604800)){


                        }else{
                            veriler.remove(sayi);
                            saydır=saydır+1;
                        }

                    }
                    if ((0>=ifsonuc)){
                        if ((ifsonuc>-604800)){

                        }else{
                            veriler.remove(sayi);
                            saydır=saydır+1;
                        }

                    }


                }
                for (String jds:veriler){
                    String asda=jds;
                    String adsf;
                    String affds="566";
                }
            }catch (Exception e){

            }
            db.close();


        }



        return veriler;

    }
    public List<String> VeriListeleCategori(){
        List<String> veriler = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            String[] stunlar = {ROW_ID,ROW_BSLK,ROW_NT,ROW_CATEGORY, ZAMAN};
            Cursor cursor = db.query(TABLO_BASLIK, stunlar,null,null,null,null,null);

            while (cursor.moveToNext()){


                veriler.add(cursor.getInt(0)+" - "+"Başlık:"+cursor.getString(1) + " - " +" Not:" +cursor.getString(2) + " - " +" Categori:"+ cursor.getString(3) + " - " + "Hatırlatma Zamanı="+cursor.getString(4));
                for (String sayi : veriler) { //sayılar dizisinin değeri tek tek sayi değişkenine atanıyor
                    String ss=sayi;
                    String zz[]= ss.toString().split("Categori:");
                    String bir= zz[0];
                    String[] iki=zz[1].split(" - ");
                    String categori=iki[0];
                    if (!categori.equals("Gorev")){
                        veriler.remove(sayi);
                        //veriler.remove(saydır1);

                    }




                }




            }
        }catch (Exception e){

        }
        db.close();





        return veriler;

    }


    public void VeriSil(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String where = ROW_ID +" = '"+ id + "'";
            db.delete(TABLO_BASLIK, where, null);


        }catch (Exception e){

        }
        db.close();
    }


    public void VeriDuzenle(int id, String nt, String categori, String baslık,String zaman){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(ROW_NT, nt);
            cv.put(ROW_CATEGORY, categori);
            cv.put(ROW_BSLK, baslık);
            cv.put(ZAMAN, zaman);
            String as=String.valueOf(id);
            String where = ROW_ID +" = '"+ as + "'";
            db.update(TABLO_BASLIK,cv,where,null);
        }catch (Exception e){
        }
        db.close();
    }


    public String iddenBaslık(int id){
        String veri ="";
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String where = ROW_ID +" = '"+ id + "'";

            String[] stunlar = {ROW_BSLK};
            Cursor cursor = db.query(TABLO_BASLIK, stunlar,where,null,null,null,null);

            while (cursor.moveToNext()){

                veri=(cursor.getString(0));
            }
        }catch (Exception e){

        }
        db.close();
        return veri;
    }

    public String iddenNot(int id){
        String veri ="";
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String where = ROW_ID +" = '"+ id + "'";

            String[] stunlar = {ROW_NT};
            Cursor cursor = db.query(TABLO_BASLIK, stunlar,where,null,null,null,null);

            while (cursor.moveToNext()){

                veri=(cursor.getString(0));
            }
        }catch (Exception e){

        }
        db.close();
        return veri;
    }
    public String iddenZaman(int id){
        String veri ="";
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String where = ROW_ID +" = '"+ id + "'";

            String[] stunlar = {ZAMAN};
            Cursor cursor = db.query(TABLO_BASLIK, stunlar,where,null,null,null,null);

            while (cursor.moveToNext()){

                veri=(cursor.getString(0));
            }
        }catch (Exception e){

        }
        db.close();
        return veri;
    }

    public String iddenCategory(int id){
        String veri ="";
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String where = ROW_ID +" = '"+ id + "'";

            String[] stunlar = {ROW_CATEGORY};
            Cursor cursor = db.query(TABLO_BASLIK, stunlar,where,null,null,null,null);

            while (cursor.moveToNext()){

                veri=(cursor.getString(0));
            }
        }catch (Exception e){

        }
        db.close();
        return veri;
    }

}
