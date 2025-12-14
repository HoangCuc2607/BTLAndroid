package com.example.quanlynguyenlieunhahang.controllers;

import android.content.Context;
import android.database.SQLException;

import com.example.quanlynguyenlieunhahang.database.notificationsDAO;
import com.example.quanlynguyenlieunhahang.models.Notification;
import com.example.quanlynguyenlieunhahang.views.NguyenLieu;

import java.util.List;

public class notificationsDAOController {
    private notificationsDAO dao;
    public notificationsDAOController(Context context)
    {
        dao = new notificationsDAO(context);
    }
    public long addNotification(Notification noti)
    {
        try{
            return dao.addNotification(noti);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public boolean isNotificationExists(int ingreId, String notiType)
    {
        try{
            return dao.isNotificationExists(ingreId, notiType);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Notification> getNotificationsByDate(String date)
    {
        try{
            return dao.getNotiByDate(date);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Boolean setIsReadTrue(int notiId)
    {
        try {
            return dao.setIsReadTrue(notiId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
