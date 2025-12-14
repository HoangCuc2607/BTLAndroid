package com.example.quanlynguyenlieunhahang.controllers;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.example.quanlynguyenlieunhahang.database.transactionsDAO;
import com.example.quanlynguyenlieunhahang.models.Ingredient;
import com.example.quanlynguyenlieunhahang.models.Transaction;

import java.util.List;

public class transactionsDAOController {
    private transactionsDAO dao;
    public transactionsDAOController(Context context)
    {
        dao = new transactionsDAO(context);
    }

    public long addTransaction(Transaction tran)
    {
        try{
            return dao.addTransaction(tran);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public boolean updateTransaction(Transaction tran)
    {
        try{
            return dao.updateTransaction(tran);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public  boolean deleteTransaction(int id)
    {
        try{
            return dao.deleteTransaction(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Transaction getTransactionById(int id)
    {
        try{
            return dao.getTransactionByID(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Transaction> getAllTransaction()
    {
        try{
            return dao.getAllTransaction();
        } catch (SQLException e) {
            Log.d("e", e.toString());
            return  null;
        }
    }
    public boolean deleteTransactionsByIngreId(int ingreId)
    {
        try {
            return dao.deleteTranSactionsByIngreId(ingreId);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("eeee", e.toString());
            return false;
        }
    }
    public List<Transaction> getTransByDate(String date)
    {
        try {
            return dao.getTranByDate(date);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
