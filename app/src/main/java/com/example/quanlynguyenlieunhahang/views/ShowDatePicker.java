package com.example.quanlynguyenlieunhahang.views;

import android.app.DatePickerDialog;
import android.content.Context;

import java.util.Calendar;

public class ShowDatePicker {

    // ✅ 1. Interface callback để trả ngày về cho nơi gọi
    public interface OnDateSelectedListener {
        void onDateSelected(String date);
    }

    // ✅ 2. Hàm hiển thị DatePicker
    public static void show(Context context, OnDateSelectedListener listener) {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, selectedYear, selectedMonth, selectedDay) -> {

                    String selectedDate = String.format(
                            "%02d/%02d/%d",
                            selectedDay,
                            selectedMonth + 1,
                            selectedYear
                    );
                    listener.onDateSelected(selectedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }
}
