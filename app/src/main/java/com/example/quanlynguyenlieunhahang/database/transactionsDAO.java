package com.example.quanlynguyenlieunhahang.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.quanlynguyenlieunhahang.models.Ingredient;
import com.example.quanlynguyenlieunhahang.models.Transaction;

import java.util.ArrayList;
import java.util.List;

public class transactionsDAO {
    final DatabaseHelper helper;
    private SQLiteDatabase db;
    public transactionsDAO(Context context) {
        helper = new DatabaseHelper(context);
    }
    private void openread()
    {
        db = helper.getReadableDatabase();
    }
    private void openwrite()
    {
        db = helper.getWritableDatabase();
    }

    public long addTransaction(Transaction tran) throws SQLException
    {
        openwrite();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TRANSACTION_DATE, tran.getTransactionDate());
        values.put(DatabaseHelper.COLUMN_TRANSACTION_NOTE, tran.getNote());
        values.put(DatabaseHelper.COLUMN_TRANSACTION_QUANTITY, tran.getQuantity());
        values.put(DatabaseHelper.COLUMN_TRANSACTION_TYPE, tran.getTransactionType());
        values.put(DatabaseHelper.COLUMN_TRANSACTION_INGREDIENT_ID, tran.getIngredientID());
        values.put(DatabaseHelper.COLUMN_TRANSACTION_UNIT, tran.getUnit());
        values.put(DatabaseHelper.COLUMN_TRANSACTION_TOTAL_AMOUNT, tran.getTotalAmount());

        long id = db.insert(DatabaseHelper.TABLE_TRANSACTIONS, null, values);
        db.close();
        return id;

    }

    public boolean updateTransaction(Transaction tran) throws  SQLException
    {
        openwrite();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TRANSACTION_DATE, tran.getTransactionDate());
        values.put(DatabaseHelper.COLUMN_TRANSACTION_NOTE, tran.getNote());
        values.put(DatabaseHelper.COLUMN_TRANSACTION_QUANTITY, tran.getQuantity());
        values.put(DatabaseHelper.COLUMN_TRANSACTION_TYPE, tran.getTransactionType());
        values.put(DatabaseHelper.COLUMN_TRANSACTION_INGREDIENT_ID, tran.getIngredientID());
        values.put(DatabaseHelper.COLUMN_TRANSACTION_UNIT, tran.getUnit());
        values.put(DatabaseHelper.COLUMN_TRANSACTION_TOTAL_AMOUNT, tran.getTotalAmount());

        int rows = db.update(DatabaseHelper.TABLE_TRANSACTIONS, values, "id = ?",
                new String[]{String.valueOf(tran.getId())});
        db.close();
        return rows > 0;

    }

    public boolean deleteTransaction(int id) throws Exception
    {
        openwrite();
        int rows = db.delete(DatabaseHelper.TABLE_TRANSACTIONS, "id = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rows > 0;

    }

    public Transaction getTransactionByID(int id) throws SQLException
    {
        openread();
        Cursor c = null;
        Transaction tran = null;
        c = db.query(DatabaseHelper.TABLE_TRANSACTIONS, null, "id = ?",
                new String[]{String.valueOf(id)}, null, null, DatabaseHelper.COLUMN_TRANSACTION_DATE + " desc");
        if (c.moveToFirst())
        {
            tran = new Transaction();
            tran.setId(c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_ID)));
            tran.setQuantity(c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_QUANTITY)));
            tran.setUnit(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_UNIT)));
            tran.setTransactionDate(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_DATE)));
            tran.setNote(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_NOTE)));
            tran.setIngredientID(c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_INGREDIENT_ID)));
            tran.setTransactionType(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_TYPE)));
            tran.setTotalAmount(c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_TOTAL_AMOUNT)));
            c.close();
        }
        if(db != null && db.isOpen())
        {
            db.close();
        }
        return tran;
    }

    public List<Transaction> getAllTransaction() throws SQLException
    {
        openread();
        Cursor c = null;
        List<Transaction> trans = null;
        c = db.query(DatabaseHelper.TABLE_TRANSACTIONS, null, null, null,
                null, null, DatabaseHelper.COLUMN_TRANSACTION_DATE + " desc");
        if(c!= null)
        {
            trans = new ArrayList<>();
            while (c.moveToNext()) {
                Transaction tran = new Transaction();
                tran.setId(c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_ID)));
                tran.setQuantity(c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_QUANTITY)));
                tran.setUnit(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_UNIT)));
                tran.setTransactionDate(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_DATE)));
                tran.setNote(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_NOTE)));
                tran.setIngredientID(c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_INGREDIENT_ID)));
                tran.setTransactionType(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_TYPE)));
                tran.setTotalAmount(c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_TOTAL_AMOUNT)));
                trans.add(tran);

            }
            c.close();
        }
        if (db != null && db.isOpen()) db.close();
        Log.d("Size 1: ", String.valueOf(trans.size()));
        return trans;
    }
    public boolean deleteTranSactionsByIngreId(int ingreId) throws SQLException
    {
        openwrite();
        int rows = db.delete(DatabaseHelper.TABLE_TRANSACTIONS, DatabaseHelper.COLUMN_TRANSACTION_INGREDIENT_ID + " = ?",
                new String[]{String.valueOf(ingreId)});
        Log.d("so dog: ", String.valueOf(rows));
        db.close();
        return rows > 0;
    }
    public List<Transaction> getTranByDate(String date) throws SQLException
    {
        Log.d("Ngay tk: ", date);
        openread();
        List<Transaction> list = null;
        Cursor c = null;
        c = db.query(DatabaseHelper.TABLE_TRANSACTIONS, null, DatabaseHelper.COLUMN_TRANSACTION_DATE + " Like ?",
                new String[]{"%"+ date + "%"}, null, null, DatabaseHelper.COLUMN_TRANSACTION_DATE + " desc");
        if(c!=null)
        {
            list = new ArrayList<>();
            while (c.moveToNext())
            {
                Transaction tran = new Transaction();
                tran.setId(c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_ID)));
                tran.setQuantity(c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_QUANTITY)));
                tran.setUnit(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_UNIT)));
                tran.setTransactionDate(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_DATE)));
                tran.setNote(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_NOTE)));
                tran.setIngredientID(c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_INGREDIENT_ID)));
                tran.setTransactionType(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_TYPE)));
                tran.setTotalAmount(c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_TOTAL_AMOUNT)));
                list.add(tran);

            }
            Log.d("Tran size: ", String.valueOf(list.size()));
            c.close();
        }
        db.close();
        return list;
    }
}
