package com.example.quanlynguyenlieunhahang.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Tên cơ sở dữ liệu và phiên bản
    private static final String DATABASE_NAME = "nguyenlieunhahang.db";
    private static final int DATABASE_VERSION = 1;

    // Tên bảng và cột cho bảng Ingredient
    public static final String TABLE_INGREDIENTS = "ingredients";
    public static final String COLUMN_INGREDIENT_ID = "id";
    public static final String COLUMN_INGREDIENT_NAME = "name";
    public static final String COLUMN_INGREDIENT_IMAGE = "image_data";
    public static final String COLUMN_INGREDIENT_QUANTITY = "quantity";
    public static final String COLUMN_INGREDIENT_UNIT = "unit";
    public static final String COLUMN_INGREDIENT_EXPIRATION_DATE = "expirationDate";
    public static final String COLUMN_INGREDIENT_LAST_UPDATED = "lastUpdated";
    public static final String COLUMN_INGREDIENT_PRICE = "price";
    public static final String COLUMN_INGREDIENT_SUPPLIER_ID = "supplierId";

    // Tên bảng và cột cho bảng Notifications (hạn sử dụng)
    public static final String TABLE_NOTIFICATIONS = "notifications";
    public static final String COLUMN_NOTIFICATION_ID = "id";
    public static final String COLUMN_NOTIFICATION_INGREDIENT_ID = "ingredientId";
    public static final String COLUMN_NOTIFICATION_TYPE = "type"; // EXPIRING_2_DAYS | EXPIRED
    public static final String COLUMN_NOTIFICATION_CREATED_DATE = "createdDate";
    public static final String COLUMN_NOTIFICATION_IS_READ = "isRead";


    // Tên bảng và cột cho bảng Transaction
    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String COLUMN_TRANSACTION_ID = "id";
    public static final String COLUMN_TRANSACTION_INGREDIENT_ID = "ingredientId";
    public static final String COLUMN_TRANSACTION_DATE = "transactionDate";
    public static final String COLUMN_TRANSACTION_TYPE = "transactionType";
    public static final String COLUMN_TRANSACTION_QUANTITY = "quantity";
    public static final String COLUMN_TRANSACTION_UNIT = "unit";
    public static final String COLUMN_TRANSACTION_NOTE = "note";
    public static final String COLUMN_TRANSACTION_TOTAL_AMOUNT = "totalAmount";

    // Tên bảng và cột cho bảng Supplier
    public static final String TABLE_SUPPLIERS = "suppliers";
    public static final String COLUMN_SUPPLIER_ID = "id";
    public static final String COLUMN_SUPPLIER_NAME = "name";
    public static final String COLUMN_SUPPLIER_CONTACT_INFO = "contactInfo";
    public static final String COLUMN_SUPPLIER_ADDRESS = "address";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String ingredients = "create table if not exists " + TABLE_INGREDIENTS+"("+
                COLUMN_INGREDIENT_ID + " integer  primary key autoincrement, "+
                COLUMN_INGREDIENT_NAME + " text not null, "+
                COLUMN_INGREDIENT_IMAGE + " blob, "+
                COLUMN_INGREDIENT_EXPIRATION_DATE + " text, "+
                COLUMN_INGREDIENT_UNIT + " text not null, "+
                COLUMN_INGREDIENT_QUANTITY + " real not null, "+
                COLUMN_INGREDIENT_LAST_UPDATED + " text, "+
                COLUMN_INGREDIENT_PRICE + " real, "+
                COLUMN_INGREDIENT_SUPPLIER_ID + " int not null, "+
                "foreign key (" + COLUMN_INGREDIENT_SUPPLIER_ID+ ") references " +
                TABLE_SUPPLIERS+ "(" + COLUMN_SUPPLIER_ID+ "))";

                ;
        db.execSQL(ingredients);

        String notifications = "create table if not exists " + TABLE_NOTIFICATIONS + " (" +
                COLUMN_NOTIFICATION_ID + " integer primary key autoincrement, " +
                COLUMN_NOTIFICATION_INGREDIENT_ID + " integer not null, " +
                COLUMN_NOTIFICATION_TYPE + " text not null, " +
                COLUMN_NOTIFICATION_CREATED_DATE + " text not null, " +
                COLUMN_NOTIFICATION_IS_READ + " integer not null, "+
                "unique (" + COLUMN_NOTIFICATION_INGREDIENT_ID + ", " + COLUMN_NOTIFICATION_TYPE + "), " +
                "foreign key (" + COLUMN_NOTIFICATION_INGREDIENT_ID + ") references " +
                TABLE_INGREDIENTS + "(" + COLUMN_INGREDIENT_ID + ")" +
                ")";
        db.execSQL(notifications);


        String suppliers = "create table if not exists "+ TABLE_SUPPLIERS+ "("+
                COLUMN_SUPPLIER_ID + " integer primary key autoincrement, "+
                COLUMN_SUPPLIER_NAME + " text not null, "+
                COLUMN_SUPPLIER_CONTACT_INFO + " text, "+
                COLUMN_SUPPLIER_ADDRESS + " text )";
        db.execSQL(suppliers);

        String transactions = "create table if not exists "+ TABLE_TRANSACTIONS+ "("+
                COLUMN_TRANSACTION_ID + " integer primary key autoincrement, " +
                COLUMN_TRANSACTION_TYPE + " text not null, "+
                COLUMN_TRANSACTION_DATE + " text not null, "+
                COLUMN_TRANSACTION_QUANTITY + " text not null, "+
                COLUMN_TRANSACTION_UNIT + " text not null, "+
                COLUMN_TRANSACTION_NOTE + " text, "+
                COLUMN_TRANSACTION_TOTAL_AMOUNT + " real, "+
                COLUMN_TRANSACTION_INGREDIENT_ID + " integer not null, "+
                "foreign key ("+ COLUMN_TRANSACTION_INGREDIENT_ID + ") references "+
                TABLE_INGREDIENTS + "("+ COLUMN_INGREDIENT_ID + "))";
        db.execSQL(transactions);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(db);
    }
}