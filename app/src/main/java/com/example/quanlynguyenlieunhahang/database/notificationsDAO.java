package com.example.quanlynguyenlieunhahang.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlynguyenlieunhahang.models.Notification;

import java.util.ArrayList;
import java.util.List;

public class notificationsDAO {
    private DatabaseHelper helper;
    private SQLiteDatabase db;
    public notificationsDAO(Context context)
    {
        helper = new DatabaseHelper(context);
    }
    public void openwrite()
    {
        db = helper.getWritableDatabase();
    }
    public void openread()
    {
        db = helper.getReadableDatabase();
    }
    public long addNotification(Notification noti) throws SQLException
    {
        openwrite();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NOTIFICATION_TYPE, noti.getType());
        values.put(DatabaseHelper.COLUMN_NOTIFICATION_INGREDIENT_ID, noti.getIngredientId());
        values.put(DatabaseHelper.COLUMN_NOTIFICATION_CREATED_DATE, noti.getCreatedDate());
        values.put(DatabaseHelper.COLUMN_NOTIFICATION_IS_READ, noti.getIsRead());
        long id = db.insert(DatabaseHelper.TABLE_NOTIFICATIONS, null,  values);
        db.close();
        return id;
    }

    public boolean isNotificationExists(int ingreId, String notiType) throws SQLException
    {
        openread();
        Cursor c = db.query(DatabaseHelper.TABLE_NOTIFICATIONS, null,
                DatabaseHelper.COLUMN_NOTIFICATION_INGREDIENT_ID + " = ? and "+
                DatabaseHelper.COLUMN_NOTIFICATION_TYPE + " = ?",
                new String[]{String.valueOf(ingreId), notiType}, null, null, null);
        boolean exists = c.moveToFirst();
        c.close();
        db.close();
        return exists;
    }
    public List<Notification> getNotiByDate(String date) throws SQLException
    {
        openread();
        List<Notification> list = new ArrayList<>();
        Cursor c = db.query(DatabaseHelper.TABLE_NOTIFICATIONS, null,
                DatabaseHelper.COLUMN_NOTIFICATION_CREATED_DATE + " = ?",
                new String[]{String.valueOf(date)}, null, null, "id desc");
        while (c.moveToNext())
        {
            Notification noti = new Notification();
            noti.setId(c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOTIFICATION_ID)));
            noti.setType(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOTIFICATION_TYPE)));
            noti.setCreatedDate(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOTIFICATION_CREATED_DATE)));
            noti.setIngredientId(c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOTIFICATION_INGREDIENT_ID)));
            noti.setIsRead(c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOTIFICATION_IS_READ)));
            list.add(noti);
        }
        c.close();
        db.close();
        return list;
    }

    public boolean setIsReadTrue(int notiId) throws SQLException
    {
        openwrite();
        ContentValues value = new ContentValues();
        value.put(DatabaseHelper.COLUMN_NOTIFICATION_IS_READ, 1);
        int rows = db.delete(DatabaseHelper.TABLE_NOTIFICATIONS, "id = ?", new String[]{String.valueOf(notiId)});
        db.close();
        return rows > 0;
    }
}
