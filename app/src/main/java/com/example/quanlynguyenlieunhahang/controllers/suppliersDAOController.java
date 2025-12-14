package com.example.quanlynguyenlieunhahang.controllers;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.example.quanlynguyenlieunhahang.database.suppliersDAO;
import com.example.quanlynguyenlieunhahang.models.Supplier;

import java.util.ArrayList;
import java.util.List;

public class suppliersDAOController {
    private suppliersDAO dao;
    public suppliersDAOController(Context context)
    {
        dao = new suppliersDAO(context);
    }
    public long addSupplier(Supplier sup)
    {
        try{
            return dao.addSupplier(sup);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public boolean updateSupplier(Supplier sup)
    {
        try
        {
            return dao.updateSupplier(sup);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteSupplier(int id)
    {
        try{
            return  dao.deleteSupplier(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Supplier> getAllSupplier()
    {
        try
        {
            return dao.getAllSupplier();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Supplier getSupplierById(int id)
    {
        try{
            return dao.getSupplierById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<String> getAllSupplierName()
    {
        try {
            return dao.getAllSupplierName();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
