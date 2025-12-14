package com.example.quanlynguyenlieunhahang.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.core.content.ContextCompat;

import com.example.quanlynguyenlieunhahang.R;
import com.example.quanlynguyenlieunhahang.controllers.ingredientsDAOController;
import com.example.quanlynguyenlieunhahang.databinding.LayoutLvNotificationsBinding;
import com.example.quanlynguyenlieunhahang.models.Ingredient;
import com.example.quanlynguyenlieunhahang.models.Notification;
import com.example.quanlynguyenlieunhahang.views.ThongBao;

import java.util.List;

public class ListViewNotifications extends BaseAdapter {
    private ingredientsDAOController incrl;
    private List<Notification> list;
    private Context context;
    public ListViewNotifications(Context context, List<Notification> list)
    {
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        LayoutLvNotificationsBinding binding;
        incrl = new ingredientsDAOController(context);
        Notification noti = list.get(i);
        if (view == null) {
            binding = LayoutLvNotificationsBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
            );
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (LayoutLvNotificationsBinding) view.getTag();
        }
        //
        BindView(binding, noti);
        return view;
    }

    private void BindView(LayoutLvNotificationsBinding binding, Notification noti)
    {
        Ingredient ingre = new Ingredient();
        ingre = incrl.getIngredientById(noti.getIngredientId());

        binding.tvIngreName.setText(ingre.getName());
        binding.tvcreatedDate.setText(noti.getCreatedDate());

        if(noti.getType().equals("EXPRIRING_2_DAYS"))
        {
            binding.tvTitleNoti.setText("Nguyên liệu sắp hết hạn (>= 2 ngày)!");
            binding.tvTitleNoti.setTextColor(ContextCompat.getColor(context, R.color.tran_import_green));
        }else {
            binding.tvTitleNoti.setText("Nguyên liệu đã hết hạn!");
            binding.tvTitleNoti.setTextColor(ContextCompat.getColor(context, R.color.unresolved_text));
        }

    }
}
