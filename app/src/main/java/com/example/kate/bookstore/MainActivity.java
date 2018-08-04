package com.example.kate.bookstore;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kate.bookstore.data.ProductContract.ProductEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int PRODUCT_LOADER = 0;
    private static final String LOG_TAG = MainActivity.class.getName();
    private ProductCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        mAdapter = new ProductCursorAdapter(this, null);

        ListView listView = findViewById(R.id.list_view_products);
        listView.setAdapter(mAdapter);

        View emptyView = findViewById(R.id.empty_view_products);
        listView.setEmptyView(emptyView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.setData(ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id));
                startActivity(intent);
            }
        });

        getSupportLoaderManager().initLoader(PRODUCT_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertDummyData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_PRICE
        };
        return new CursorLoader(this, ProductEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private void insertDummyData() {
        ContentValues book;

        book = new ContentValues();
        book.put(ProductEntry.COLUMN_PRODUCT_NAME, "Code: The Hidden Language of Computer Hardware and Software");
        book.put(ProductEntry.COLUMN_PRODUCT_PRICE, 19.64);
        book.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 12);
        book.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, "Microsoft Press");
        book.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, "79090909090");
        getContentResolver().insert(ProductEntry.CONTENT_URI, book);

        book = new ContentValues();
        book.put(ProductEntry.COLUMN_PRODUCT_NAME, "Harry Potter and the Philosopher's Stone");
        book.put(ProductEntry.COLUMN_PRODUCT_PRICE, 13.49);
        book.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 10);
        book.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, "Children's book");
        book.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, "78080808080");
        getContentResolver().insert(ProductEntry.CONTENT_URI, book);

        book = new ContentValues();
        book.put(ProductEntry.COLUMN_PRODUCT_NAME, "Frog and Toad");
        book.put(ProductEntry.COLUMN_PRODUCT_PRICE, 8.39);
        book.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 2);
        book.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, "HarperCollins");
        book.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, "77070707070");
        getContentResolver().insert(ProductEntry.CONTENT_URI, book);
    }
}
