package com.example.quanlynguyenlieunhahang.fragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynguyenlieunhahang.controllers.suppliersDAOController;
import com.example.quanlynguyenlieunhahang.databinding.LayoutRcvIngredientsBinding;
import com.example.quanlynguyenlieunhahang.models.Ingredient;
import com.example.quanlynguyenlieunhahang.models.Supplier;
import com.example.quanlynguyenlieunhahang.views.GiaoDich;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewIngredients extends RecyclerView.Adapter<RecyclerViewIngredients.MyViewHolder>{
    private suppliersDAOController supctrl;
    private Context context;
    private List<Ingredient> listins;
    private OnClickIngreListener listener;
    public RecyclerViewIngredients( Context context, List<Ingredient> ings)
    {
        this.context = context;
        this.listins = ings;
    }
    public interface OnClickIngreListener
    {
        void OnClick(Ingredient ingre);
        void OnLongClick(int ingreId);
    }
    public void setOnClickIngreListener(OnClickIngreListener listener)
    {
        this.listener = listener;
    }
    @NonNull
    @Override
    public RecyclerViewIngredients.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutRcvIngredientsBinding binding = LayoutRcvIngredientsBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false
        );
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewIngredients.MyViewHolder holder, int position) {
        if(listins != null) {
            Ingredient in = listins.get(position);
            holder.binding.tvInName.setText(in.getName());
            holder.binding.tvLastUpdated.setText(in.getExpirationDate());
            holder.binding.tvPrice.setText("$" + String.valueOf(in.getPrice()));
            holder.binding.tvQuantity.setText(String.valueOf(in.getQuantity()) + " " + in.getUnit());
            //LoadNCC
            Supplier sup = null;
            supctrl = new suppliersDAOController(context);
            if(supctrl.getSupplierById(in.getSupplierId()) != null) {
                sup = new Supplier();
                sup = supctrl.getSupplierById(in.getSupplierId());
                holder.binding.tvSupplier.setText("NCC: " + sup.getName());
            }
            //goi khi click item
            setListener(holder, in);
            setLongClickIngre(holder, in.getId());
        }
    }
    public void setListener(MyViewHolder holder, Ingredient ingre)
    {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnClick(ingre);
            }
        });
    }
    public void setLongClickIngre(MyViewHolder holder, int ingreId)
    {
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.OnLongClick(ingreId);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listins == null ? 0: listins.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        final LayoutRcvIngredientsBinding binding;
        public MyViewHolder(LayoutRcvIngredientsBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
