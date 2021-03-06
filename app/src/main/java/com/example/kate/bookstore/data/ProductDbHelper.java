package com.example.kate.bookstore.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kate.bookstore.data.ProductContract.ProductEntry;

public class ProductDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "products.db";
    public static final int DATABASE_VERSION = 1;

    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + ProductEntry.TABLE_NAME +
                "(" +
                ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL," +
                ProductEntry.COLUMN_PRODUCT_PRICE + " REAL NOT NULL," +
                ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL," +
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME + " TEXT NOT NULL," +
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE + " TEXT NOT NULL" +
                ")";

        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No upgrade needed yet
    }
}