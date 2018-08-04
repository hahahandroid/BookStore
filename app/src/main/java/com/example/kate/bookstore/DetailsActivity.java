package com.example.kate.bookstore;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kate.bookstore.data.ProductContract.ProductEntry;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PRODUCT_LOADER = 0;

    private Uri mCurrentProductUri;

    private TextView mName;
    private TextView mPrice;
    private TextView mStock;
    private TextView mSupplierName;
    private TextView mSupplierPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        getSupportLoaderManager().initLoader(PRODUCT_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, mCurrentProductUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            mName = findViewById(R.id.product_name);
            mPrice = findViewById(R.id.product_price);
            mStock = findViewById(R.id.product_stock);
            mSupplierName = findViewById(R.id.supplier_name);
            mSupplierPhone = findViewById(R.id.supplier_phone);

            Button buttonSell = findViewById(R.id.button_sell);
            Button buttonBuy = findViewById(R.id.button_buy);
            Button buttonCallSupplier = findViewById(R.id.button_call_supplier);

            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE);

            String nameText = cursor.getString(nameColumnIndex);
            String priceText = getString(R.string.price, cursor.getString(priceColumnIndex));
            final int quantity = cursor.getInt(quantityColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            final String supplierPhone = cursor.getString(supplierPhoneColumnIndex);

            String stockText;
            if (quantity == 0) {
                stockText = getString(R.string.out_of_stock);
            } else {
                stockText = getString(R.string.in_stock, Integer.toString(quantity));
            }

            mName.setText(nameText);
            mPrice.setText(priceText);
            mStock.setText(stockText);
            mSupplierName.setText(supplierName);
            mSupplierPhone.setText(supplierPhone);

            if (quantity == 0) {
                buttonSell.setEnabled(false);
            } else {
                buttonSell.setEnabled(true);
            }

            buttonSell.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (quantity <= 0) {
                        return;
                    }

                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity - 1);

                    int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);

                    CharSequence toastMessage = getText(R.string.toast_one_item_sold);
                    if (rowsAffected == 0) {
                        toastMessage = getText(R.string.toast_selling_failed);
                    }
                    Toast.makeText(DetailsActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
                }
            });

            buttonBuy.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity + 1);

                    int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);

                    CharSequence toastMessage = getText(R.string.toast_one_item_bought);
                    if (rowsAffected == 0) {
                        toastMessage = getText(R.string.toast_buying_failed);
                    }
                    Toast.makeText(DetailsActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
                }
            });

            buttonCallSupplier.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + supplierPhone));
                    startActivity(callIntent);
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mName.setText("");
        mPrice.setText("");
        mStock.setText("");
        mSupplierName.setText("");
        mSupplierPhone.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(this, EditorActivity.class);
                intent.setData(mCurrentProductUri);
                startActivity(intent);
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteProduct() {
        if (mCurrentProductUri == null) {
            return;
        }

        int rowsAffected = getContentResolver().delete(mCurrentProductUri, null, null);

        String toastMessage = getString(R.string.delete_product_successful);
        if (rowsAffected == 0) {
            toastMessage = getString(R.string.delete_product_failed);
        }
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
        finish();
    }

}
