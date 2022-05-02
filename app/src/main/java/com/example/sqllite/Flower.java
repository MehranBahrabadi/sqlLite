package com.example.sqllite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.NonNull;

public class Flower {

    public static final String KEY_ID = "productId";
    public static final String KEY_NAME = "name";
    public static final String KEY_CAT = "category";
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_PRICE = "price";
    public static final String KEY_INSTR = "instructions";

    private long productId;
    private String name;
    private String category;
    private String photo;
    private String instructions;
    private double price;


    public Flower() {

    }

    public Flower(long productId, String name, String category, String photo, String instructions, double price) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.photo = photo;
        this.instructions = instructions;
        this.price = price;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public ContentValues getContentValuesForDb(){
        ContentValues values = new ContentValues();
        values.put(Flower.KEY_ID , productId);
        values.put(Flower.KEY_CAT , category);
        values.put(Flower.KEY_PRICE , price);
        values.put(Flower.KEY_PHOTO , photo);
        values.put(Flower.KEY_NAME , name);
        values.put(Flower.KEY_INSTR , instructions);
        return values;
    }
    @SuppressLint("Range")
    public static Flower cursorToFlower(Cursor cursor){
        Flower flower = new Flower();
        flower.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
        flower.setCategory(cursor.getString(cursor.getColumnIndex(KEY_CAT)));
        flower.setPhoto(cursor.getString(cursor.getColumnIndex(KEY_PHOTO)));
        flower.setPrice(cursor.getDouble(cursor.getColumnIndex(KEY_PRICE)));
        flower.setInstructions(cursor.getString(cursor.getColumnIndex(KEY_INSTR)));
        flower.setProductId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
        return flower;
    }
}
