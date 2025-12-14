package com.example.quanlynguyenlieunhahang.fragments;

import android.content.Context;
import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynguyenlieunhahang.R;
import com.example.quanlynguyenlieunhahang.controllers.ingredientsDAOController;
import com.example.quanlynguyenlieunhahang.databinding.LayoutGiaoDichAdapterBinding;
import com.example.quanlynguyenlieunhahang.models.Ingredient;
import com.example.quanlynguyenlieunhahang.models.Transaction;

import java.util.List;

public class RecyclerViewTransactions extends RecyclerView.Adapter<RecyclerViewTransactions.MyViewHolder> {
    private List<Transaction> list;
    private Context context;
    private ingredientsDAOController dao;
    public RecyclerViewTransactions(List<Transaction> list, Context context)
    {
        this.list = list;
        this.context = context;
        this.dao = new ingredientsDAOController(context);
    }
    @NonNull
    @Override
    public RecyclerViewTransactions.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutGiaoDichAdapterBinding binding = LayoutGiaoDichAdapterBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewTransactions.MyViewHolder holder, int position) {

        Transaction tran = list.get(position);
        if (tran!= null) {
            Ingredient ingre = dao.getIngredientById(tran.getIngredientID());
            if(ingre != null) {
                holder.binding.tvTranIngreName.setText(ingre.getName());
                holder.binding.tvTranDate.setText(tran.getTransactionDate());
                holder.binding.tvTranQuantity.setText(String.valueOf(tran.getQuantity()));
                holder.binding.tvTranType.setText(tran.getTransactionType());
                holder.binding.tvSum.setText(String.valueOf(tran.getTotalAmount()));
                if (tran.getTransactionType().equals("Nhập"))
                    holder.binding.tvTranType.setTextColor(ContextCompat.getColor(context, R.color.tran_import_green));
            }
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        final LayoutGiaoDichAdapterBinding binding;
        public MyViewHolder(LayoutGiaoDichAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
