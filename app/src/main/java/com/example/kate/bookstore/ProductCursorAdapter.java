package com.example.kate.bookstore;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kate.bookstore.data.ProductContract.ProductEntry;

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView name = view.findViewById(R.id.product_title);
        TextView price = view.findViewById(R.id.product_price);
        TextView stock = view.findViewById(R.id.product_stock);
        Button sell = view.findViewById(R.id.button_sell);

        int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);

        final int id = cursor.getInt(idColumnIndex);
        String nameText = cursor.getString(nameColumnIndex);
        String priceText = context.getString(R.string.price, cursor.getString(priceColumnIndex));
        final int quantity = cursor.getInt(quantityColumnIndex);

        String stockText;
        if (quantity == 0) {
            stockText = context.getString(R.string.out_of_stock);
        } else {
            stockText = context.getString(R.string.in_stock, Integer.toString(quantity));
        }

        name.setText(nameText);
        price.setText(priceText);
        stock.setText(stockText);

        if (quantity == 0) {
            sell.setVisibility(View.GONE);
        } else {
            sell.setVisibility(View.VISIBLE);
        }

        sell.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (quantity <= 0) {
                    return;
                }

                Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);

                ContentValues values = new ContentValues();
                values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity - 1);

                int rowsAffected = context.getContentResolver().update(currentProductUri, values, null, null);

                CharSequence toastMessage = context.getText(R.string.toast_one_item_sold);
                if (rowsAffected == 0) {
                    toastMessage = context.getText(R.string.toast_selling_failed);
                }
                Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
