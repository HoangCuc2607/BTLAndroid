package com.example.quanlynguyenlieunhahang.views;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlynguyenlieunhahang.R;
import com.example.quanlynguyenlieunhahang.controllers.ingredientsDAOController;
import com.example.quanlynguyenlieunhahang.controllers.notificationsDAOController;
import com.example.quanlynguyenlieunhahang.databinding.ActivityNhaCungCapBinding;
import com.example.quanlynguyenlieunhahang.databinding.ActivityThongBaoBinding;
import com.example.quanlynguyenlieunhahang.fragments.ListViewNotifications;
import com.example.quanlynguyenlieunhahang.models.Ingredient;
import com.example.quanlynguyenlieunhahang.models.Notification;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ThongBao extends RootActivity{
    private ActivityThongBaoBinding binding;
    private List<Notification> listNotifications;
    private ingredientsDAOController inctrl;
    private notificationsDAOController notictrl;
    static final String EXPRIRING_2_DAYS = "EXPRIRING_2_DAYS";
    static final String EXPRIRED = "EXPRIRED";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityThongBaoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setCtrl();
        CheckNoticed();
        setSuitNotice();
        //btn xoa
        deleteAllNoti();
        //btn reload
        btnReLoad();
    }
    public void setCtrl()
    {
        inctrl = new ingredientsDAOController(this);
        notictrl = new notificationsDAOController(this);
    }

    public static String getTodayString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }
    public static String minusTwoDays(String dateStr) {
        try {
            SimpleDateFormat sdf =
                    new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            Date date = sdf.parse(dateStr);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DAY_OF_MONTH, -2);

            return sdf.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static long daysBetween(String date1, String date2) {
        try {
            SimpleDateFormat sdf =
                    new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            Date d1 = sdf.parse(date1);
            Date d2 = sdf.parse(date2);

            long diffMillis = d2.getTime() - d1.getTime();
            return diffMillis / (1000 * 60 * 60 * 24);

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }



    public void CheckNoticed()
    {
        List<Ingredient> ingredients = inctrl.getAllIngredient();


        for(int i = 0; i < ingredients.size(); i++)
        {
            Ingredient ingre = ingredients.get(i);
            long daysLeft = daysBetween(getTodayString(), ingredients.get(i).getExpirationDate());
            if(daysLeft > 0 && daysLeft <= 2)
            {
                if (!notictrl.isNotificationExists(ingre.getId(), EXPRIRING_2_DAYS)) {
                    Notification newNoti = new Notification();
                    newNoti.setType(EXPRIRING_2_DAYS);
                    newNoti.setCreatedDate(getTodayString());
                    newNoti.setIngredientId(ingre.getId());
                    newNoti.setIsRead(0);
                    notictrl.addNotification(newNoti);
                }
            }
            if(daysLeft <= 0)
            {
                if(!notictrl.isNotificationExists(ingre.getId(), EXPRIRED))
                {
                    Notification newNoti = new Notification();
                    newNoti.setType(EXPRIRED);
                    newNoti.setCreatedDate(getTodayString());
                    newNoti.setIngredientId(ingre.getId());
                    newNoti.setIsRead(0);
                    notictrl.addNotification(newNoti);
                }
            }
        }


    }
    public void btnReLoad()
    {
        binding.btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSuitNotice();
            }
        });
    }
    public void deleteAllNoti()
    {
        binding.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listNotifications = new ArrayList<>();
                setLvNoti();
            }
        });
    }
    public void setSuitNotice()
    {
        listNotifications = notictrl.getNotificationsByDate(getTodayString());
        setLvNoti();

    }
    public void setLvNoti()
    {
        ListViewNotifications adapter = new ListViewNotifications(this, listNotifications);
        binding.lvNotifications.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //khi da thong bao thi set isread = 1;
        for(int i = 0; i < listNotifications.size(); i++)
        {
            if(notictrl.setIsReadTrue(listNotifications.get(i).getId()))
            {
                listNotifications.get(i).setIsRead(1);
            }
        }
    }

}
