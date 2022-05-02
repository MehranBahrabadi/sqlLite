package com.example.sqllite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.FontRes;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FlowerDbHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = "flowerDbHelper";
    private static  final String DB_NAME = "flower-db.db";
    private static  final int DB_VERSION = 1;
    public static final String TABLE_FLOWERS = "tb_flowers";
    private static final String CMD = "CREATE TABLE  IF NOT EXISTS `" + TABLE_FLOWERS +"` ( " +
            "`"+ Flower.KEY_ID +"` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "`"+Flower.KEY_NAME +"` TEXT, " +
            "`" + Flower.KEY_CAT +  "` TEXT, " +
            "`" + Flower.KEY_INSTR +"` TEXT, " +
            "`" + Flower.KEY_PRICE + "` NUMERIC, " +
            "`" + Flower.KEY_PHOTO + "` TEXT " +
            " )";
    private static final  String[] allColumns={Flower.KEY_ID , Flower.KEY_NAME ,
            Flower.KEY_CAT,Flower.KEY_INSTR , Flower.KEY_PHOTO , Flower.KEY_PRICE};


    public FlowerDbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CMD);
        Log.i(LOG_TAG, "table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //backup
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FLOWERS);
        Log.i(LOG_TAG, "table dropped: ");
        onCreate(db);
        //restore
    }

    public void insertFlower(Flower flower){
        //data base
        if(getFlowers(Flower.KEY_ID + "=" + flower.getProductId() ,null).isEmpty()){
            SQLiteDatabase db = getWritableDatabase();
            long  insertId = db.insert(TABLE_FLOWERS,null,flower.getContentValuesForDb());
            if(insertId == -1)
                Log.i(LOG_TAG, "data insertion failed .(item : " + flower.toString());
            else
                Log.i(LOG_TAG, "data inserted with id : " + insertId);
            if (db.isOpen()) db.close();
        }
    }
    @SuppressLint("Range")
    public List<Flower> getAllFlowers(){
        SQLiteDatabase db = getReadableDatabase();
        List<Flower> flowerList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM `" + TABLE_FLOWERS + "`",null);
        Log.i(LOG_TAG, "returned : " + cursor.getCount() + " rows.");
        if(cursor.moveToFirst()){
         do {
             //process for each row
             Flower flower = Flower.cursorToFlower(cursor);
             flowerList.add(flower);
         }while (cursor.moveToNext());
        }
        cursor.close();
        if(db.isOpen())db.close();
        return flowerList;
    }
    public void deleteFlower(long productId){
        SQLiteDatabase db = getWritableDatabase();
        int count = db.delete(TABLE_FLOWERS,Flower.KEY_ID + " = ?"  , new String[] {String.valueOf(productId)});
        Log.i(LOG_TAG, count + " rows deleted. ");
        if (db.isOpen()) db.close();
    }
    public void update(long id , ContentValues contentValues){
        SQLiteDatabase db = getWritableDatabase();
        int count = db.update(TABLE_FLOWERS,contentValues,Flower.KEY_ID + "=" + id , null);
        Log.i(LOG_TAG, count + " rows updated");
        if(db.isOpen())db.close();
    }
    public List<Flower> getFlowers(String selection , String[] selArgs){
        SQLiteDatabase db = getReadableDatabase();
        List<Flower> flowerList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_FLOWERS , allColumns , selection , selArgs,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Flower flower = Flower.cursorToFlower(cursor);
                flowerList.add(flower);
            }while(cursor.moveToNext());
        }
        cursor.close();
        if(db.isOpen())db.close();
        return flowerList;
    }
    public void getFlowers(List<Flower> flowerList , String selection , String[] selArgs){
        SQLiteDatabase db = getReadableDatabase();
        flowerList.clear();
        Cursor cursor = db.query(TABLE_FLOWERS , allColumns , selection , selArgs,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Flower flower = Flower.cursorToFlower(cursor);
                flowerList.add(flower);
            }while(cursor.moveToNext());
        }
        cursor.close();
        if(db.isOpen())db.close();
    }
    public List<Flower> getFlowers(String selection , String[] selArgs,String orderBy){
        SQLiteDatabase db =getReadableDatabase();
        List<Flower> flowerList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_FLOWERS,allColumns,selection,selArgs,null,null,orderBy);
        if(cursor.moveToFirst()){
            do{
                Flower flower = Flower.cursorToFlower(cursor);
                flowerList.add(flower);
            }while(cursor.moveToNext());
        }
        cursor.close();
        if(db.isOpen())db.close();
        return flowerList;
    }
}
