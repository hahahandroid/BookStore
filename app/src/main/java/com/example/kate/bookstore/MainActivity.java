package com.example.kate.bookstore;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.kate.bookstore.data.ProductContract.ProductEntry;
import com.example.kate.bookstore.data.ProductDbHelper;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    private ProductDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new ProductDbHelper(this);

        insertData();
        queryData();
    }

    private void insertData() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Code: The Hidden Language of Computer Hardware and Software");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 19.64);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 12);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPLIER_NAME, "Microsoft Press");
        values.put(ProductEntry.COLUMN_PRODUCT_SUPLIER_PHONE, "+79090909090");

        long newRowId = db.insert(ProductEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Log.e(LOG_TAG, "Data insert failed");
        } else {
            Log.v(LOG_TAG, "Data insert succeed");
        }
    }

    private Cursor queryData() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] columns = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_SUPLIER_NAME,
                ProductEntry.COLUMN_PRODUCT_SUPLIER_PHONE
        };

        Cursor cursor = db.query(
                ProductEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        try {
            int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int suplierColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPLIER_NAME);
            int suplierPhoneColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPLIER_PHONE);

            String message;

            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                long currentPrice = cursor.getLong(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSuplier = cursor.getString(suplierColumnIndex);
                String currentSuplierPhone = cursor.getString(suplierPhoneColumnIndex);

                message = currentID + " - " +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSuplier + " - " +
                        currentSuplierPhone;

                Log.v(LOG_TAG, message);
            }

        } finally {
            cursor.close();
        }
        return cursor;
    }
}
