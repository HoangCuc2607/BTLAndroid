package com.example.quanlynguyenlieunhahang.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.quanlynguyenlieunhahang.models.Supplier;

import java.util.ArrayList;
import java.util.List;

public class suppliersDAO {
    private DatabaseHelper helper;
    private SQLiteDatabase db = null;
    public suppliersDAO(Context context)
    {
        helper = new DatabaseHelper(context);
    }
    private void opendbwrite()
    {
        db = helper.getWritableDatabase();
    }
    private void opendbread()
    {
        db = helper.getReadableDatabase();
    }
    public long addSupplier(Supplier sup) throws SQLException
    {
        opendbwrite();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SUPPLIER_NAME, sup.getName());
        values.put(DatabaseHelper.COLUMN_SUPPLIER_ADDRESS, sup.getAddress());
        values.put(DatabaseHelper.COLUMN_SUPPLIER_CONTACT_INFO, sup.getContactInfo());
        long id = db.insert(DatabaseHelper.TABLE_SUPPLIERS, null, values);
        db.close();
        return id;
    }
    public boolean updateSupplier(Supplier sup) throws SQLException
    {
        opendbwrite();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SUPPLIER_NAME, sup.getName());
        values.put(DatabaseHelper.COLUMN_SUPPLIER_ADDRESS, sup.getAddress());
        values.put(DatabaseHelper.COLUMN_SUPPLIER_CONTACT_INFO, sup.getContactInfo());
        int rows = db.update(DatabaseHelper.TABLE_SUPPLIERS, values,
                "id = ?", new String[]{String.valueOf(sup.getId())});
        db.close();
        return rows > 0;
    }
    public boolean deleteSupplier(int id) throws SQLException
    {
        opendbwrite();
        int rows = db.delete(DatabaseHelper.TABLE_SUPPLIERS, "id = ?",
                new String[]{String.valueOf(id)});
        return  rows > 0;
    }

    public List<Supplier> getAllSupplier() throws SQLException
    {
        opendbread();
        List<Supplier> list = null;
        Cursor c = null;
        try{
            c = db.query(DatabaseHelper.TABLE_SUPPLIERS, null, null,
                    null, null, null,"id desc");
            if(c!= null)
            {
                list = new ArrayList<>();
                while (c.moveToNext())
                {
                    Supplier sup = new Supplier();
                    sup.setId(c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_ID)));
                    sup.setName(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_NAME)));
                    sup.setAddress(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_ADDRESS)));
                    sup.setContactInfo(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_CONTACT_INFO)));
                    list.add(sup);
                }
            }
        }
        finally {
            if(c!= null) c.close();
            if(db!= null && db.isOpen()) db.close();
        }
        return list;
    }
    public Supplier getSupplierById(int id)
    {
        opendbread();
        Supplier sup = null;
        Cursor c = null;
        try
        {
            c = db.query(DatabaseHelper.TABLE_SUPPLIERS, null, "id = ?",
                    new String[]{String.valueOf(id)}, null, null, null );
            if(c!= null && c.moveToFirst())
            {
                sup = new Supplier();
                sup.setId(c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_ID)));
                sup.setName(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_NAME)));
                sup.setAddress(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_ADDRESS)));
                sup.setContactInfo(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_CONTACT_INFO)));

            }
        }finally {
            if(c!= null) c.close();
            if (db!=null && db.isOpen()) db.close();
        }
        return sup;
    }
    public List<String> getAllSupplierName()
    {
        opendbread();
        List<String> list = null;
        Cursor c = null;
        try
        {
            c = db.query(DatabaseHelper.TABLE_SUPPLIERS, new String[]{DatabaseHelper.COLUMN_SUPPLIER_NAME},
                    null, null, null, null, "id desc");
            if(c!= null)
            {
                while (c.moveToNext())
                {
                    list = new ArrayList<>();
                    list.add(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_NAME)));
                }
            }
        }finally {
            if(c != null) c.close();
            if (db != null && db.isOpen()) db.close();
        }
        return list;
    }

}
