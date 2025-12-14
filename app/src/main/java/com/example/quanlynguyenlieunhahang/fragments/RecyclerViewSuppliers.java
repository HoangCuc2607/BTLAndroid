package com.example.quanlynguyenlieunhahang.fragments;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynguyenlieunhahang.databinding.SupplierItemBinding;
import com.example.quanlynguyenlieunhahang.models.Supplier;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewSuppliers
        extends RecyclerView.Adapter<RecyclerViewSuppliers.MyHolderView> {

    private List<Supplier> list;
    private Context context;
    private OnSupplierClickListener listener;

    public RecyclerViewSuppliers(List<Supplier> list, Context context) {
        this.list = list != null ? list : new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SupplierItemBinding binding = SupplierItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new MyHolderView(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolderView holder, int position) {
        Supplier supplier = list.get(position);
        if(supplier != null) {
            bindData(holder, supplier);
            onClickItem(holder, supplier);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void bindData(MyHolderView holder, Supplier supplier) {
        holder.binding.tvSupplierName.setText(supplier.getName());
        holder.binding.tvSupplierAddress.setText(supplier.getAddress());
        holder.binding.tvSupplierContact.setText(supplier.getContactInfo());
    }
    public interface OnSupplierClickListener {
        void setListener(Supplier supplier);
    }
    public void setOnSupplierClickListener(OnSupplierClickListener listener)
    {
        this.listener = listener;
    }
    public void onClickItem(MyHolderView holder, Supplier supplier)
    {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setListener(supplier);
            }
        });
    }


    public static class MyHolderView extends RecyclerView.ViewHolder {
        final SupplierItemBinding binding;

        public MyHolderView(SupplierItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
