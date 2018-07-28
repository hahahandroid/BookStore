package com.example.kate.bookstore.data;


import android.provider.BaseColumns;

public final class ProductContract {
    public static abstract class ProductEntry implements BaseColumns {

        public static final String TABLE_NAME = "products";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "name";
        public static final String COLUMN_PRODUCT_PRICE = "price";
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
        public static final String COLUMN_PRODUCT_SUPLIER_NAME = "suplier_name";
        public static final String COLUMN_PRODUCT_SUPLIER_PHONE = "suplier_phone";

    }
}

