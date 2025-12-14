package com.example.quanlynguyenlieunhahang.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlynguyenlieunhahang.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class ingredientsDAO {
    private SQLiteDatabase db;
    private DatabaseHelper helper;
    public ingredientsDAO(Context context)
    {
        helper = new DatabaseHelper(context);
    }
    public void opendbwrite()
    {
        db = helper.getWritableDatabase();
    }
    public void opendbread()
    {
        db = helper.getReadableDatabase();
    }
    public long addIngredient(Ingredient ing) throws SQLException
    {
        opendbwrite();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_INGREDIENT_NAME, ing.getName());
        values.put(DatabaseHelper.COLUMN_INGREDIENT_IMAGE, ing.getImageData());
        values.put(DatabaseHelper.COLUMN_INGREDIENT_QUANTITY, ing.getQuantity());
        values.put(DatabaseHelper.COLUMN_INGREDIENT_UNIT, ing.getUnit());
        values.put(DatabaseHelper.COLUMN_INGREDIENT_EXPIRATION_DATE, ing.getExpirationDate());
        values.put(DatabaseHelper.COLUMN_INGREDIENT_LAST_UPDATED, ing.getLastUpdated());
        values.put(DatabaseHelper.COLUMN_INGREDIENT_PRICE, ing.getPrice());
        values.put(DatabaseHelper.COLUMN_INGREDIENT_SUPPLIER_ID, ing.getSupplierId());
        long id = db.insert(DatabaseHelper.TABLE_INGREDIENTS, null, values);
        db.close();
        return id;

    }

    public boolean updateIngredient(Ingredient ing) throws SQLException
    {
        opendbwrite();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_INGREDIENT_NAME, ing.getName());
        values.put(DatabaseHelper.COLUMN_INGREDIENT_IMAGE, ing.getImageData());
        values.put(DatabaseHelper.COLUMN_INGREDIENT_QUANTITY, ing.getQuantity());
        values.put(DatabaseHelper.COLUMN_INGREDIENT_UNIT, ing.getUnit());
        values.put(DatabaseHelper.COLUMN_INGREDIENT_EXPIRATION_DATE, ing.getExpirationDate());
        values.put(DatabaseHelper.COLUMN_INGREDIENT_LAST_UPDATED, ing.getLastUpdated());
        values.put(DatabaseHelper.COLUMN_INGREDIENT_PRICE, ing.getPrice());
        values.put(DatabaseHelper.COLUMN_INGREDIENT_SUPPLIER_ID, ing.getSupplierId());

        int rows = db.update(DatabaseHelper.TABLE_INGREDIENTS, values,
                "id = ?", new String[]{String.valueOf(ing.getId())});
        db.close();
        return rows > 0;
    }
    public boolean deleteIngredient(int id) throws SQLException
    {
        opendbwrite();
        int rows = db.delete(DatabaseHelper.TABLE_INGREDIENTS, " id = ?",
                new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public List<Ingredient> getAllIngredient() throws SQLException
    {
        opendbread();
        List<Ingredient> listIngredients = null;
        Cursor c = null;
        try {
            c = db.query(DatabaseHelper.TABLE_INGREDIENTS, null, null,
                    null, null, null, "id desc");
            if (c != null) {
                listIngredients = new ArrayList<>();
                while (c.moveToNext()) {
                    Ingredient ing = new Ingredient();
                    ing.setId(c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_ID)));
                    ing.setName(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_NAME)));
                    ing.setExpirationDate(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_EXPIRATION_DATE)));
                    ing.setQuantity(c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_QUANTITY)));
                    ing.setUnit(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_UNIT)));
                    ing.setImageData(c.getBlob(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_IMAGE)));
                    ing.setLastUpdated(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_LAST_UPDATED)));
                    ing.setPrice(c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_PRICE)));
                    ing.setSupplierId(c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_SUPPLIER_ID)));
                    listIngredients.add(ing);
                }
            }
        }finally {
            if(c!= null) c.close();
            if(db != null && db.isOpen()) db.close();

        }
        return listIngredients;
    }
    public Ingredient getIngredientById(int id)
    {
        opendbread();
        Cursor c = null;
        Ingredient ing = null;
        try {

                c = db.query(DatabaseHelper.TABLE_INGREDIENTS, null, "id = ?",
                        new String[]{String.valueOf(id)}, null, null, null);
                if(c != null && c.moveToFirst())
                {
                    ing = new Ingredient();
                    ing.setId(c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_ID)));
                    ing.setName(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_NAME)));
                    ing.setExpirationDate(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_EXPIRATION_DATE)));
                    ing.setQuantity(c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_QUANTITY)));
                    ing.setUnit(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_UNIT)));
                    ing.setImageData(c.getBlob(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_IMAGE)));
                    ing.setLastUpdated(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_LAST_UPDATED)));
                    ing.setPrice(c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_PRICE)));
                    ing.setSupplierId(c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_SUPPLIER_ID)));

            }
        }finally {
            if (c != null) c.close();
            if(db != null && db.isOpen()) db.close();
        }
        return ing;
    }

    public List<Ingredient> getIngredientsBySupId(int id) throws  SQLException
    {
        opendbread();
        Cursor c = null;
        List<Ingredient> list = null;
        c = db.query(DatabaseHelper.TABLE_INGREDIENTS, null, DatabaseHelper.COLUMN_INGREDIENT_SUPPLIER_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, DatabaseHelper.COLUMN_INGREDIENT_NAME + " desc");
        if(c!=null)
        {
            list = new ArrayList<>();
            while (c.moveToNext())
            {
                Ingredient ing = new Ingredient();
                ing.setId(c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_ID)));
                ing.setName(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_NAME)));
                ing.setExpirationDate(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_EXPIRATION_DATE)));
                ing.setQuantity(c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_QUANTITY)));
                ing.setUnit(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_UNIT)));
                ing.setImageData(c.getBlob(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_IMAGE)));
                ing.setLastUpdated(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_LAST_UPDATED)));
                ing.setPrice(c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_PRICE)));
                ing.setSupplierId(c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INGREDIENT_SUPPLIER_ID)));
                list.add(ing);
            }
            c.close();
        }
        if(db!=null && db.isOpen()) db.close();
        return list;
    }
    public boolean deleteIngreBySupId(int id) throws SQLException
    {
        opendbwrite();

        int rows = db.delete(DatabaseHelper.TABLE_INGREDIENTS, DatabaseHelper.COLUMN_INGREDIENT_SUPPLIER_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rows > 0;
    }
}
