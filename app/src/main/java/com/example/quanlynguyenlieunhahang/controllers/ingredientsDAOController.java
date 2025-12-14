package com.example.quanlynguyenlieunhahang.controllers;

import android.content.Context;
import android.database.SQLException;

import com.example.quanlynguyenlieunhahang.database.ingredientsDAO;
import com.example.quanlynguyenlieunhahang.models.Ingredient;

import java.util.List;

public class ingredientsDAOController {
    private ingredientsDAO dao;
    public ingredientsDAOController(Context context)
    {
        dao = new ingredientsDAO(context);
    }
    public long addIngredient(Ingredient ing)
    {
        try {
            return dao.addIngredient(ing);
        } catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }

    }
    public boolean updateIngredient(Ingredient ing)
    {
        try{
            return dao.updateIngredient(ing);
        }catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteIngredient(int id)
    {
        try
        {
            return dao.deleteIngredient(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Ingredient> getAllIngredient()
    {
        try
        {
            return dao.getAllIngredient();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Ingredient getIngredientById(int id)
    {
        try
        {
            return dao.getIngredientById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<Ingredient> getIngrediensBySupId(int id)
    {
        try{
            return dao.getIngredientsBySupId(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean deleteIngreBySupId(int id)
    {
        try{
            return dao.deleteIngreBySupId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
