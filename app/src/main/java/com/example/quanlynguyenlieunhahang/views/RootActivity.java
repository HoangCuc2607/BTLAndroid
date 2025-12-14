package com.example.quanlynguyenlieunhahang.views;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynguyenlieunhahang.MainActivity;
import com.example.quanlynguyenlieunhahang.R;

public class RootActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.opGiaoDich) {
            startActivity(new Intent(this, GiaoDich.class));
            finish();
        }
        if (id == R.id.opNguyenLieu) {
            startActivity(new Intent(this, NguyenLieu.class));
            finish();
        }
        if (id == R.id.opNhaCungCap) {
            startActivity(new Intent(this, NhaCungCap.class));
            finish();
        }
        if (id == R.id.opThongBao) {
            startActivity(new Intent(this, ThongBao.class));
            finish();
        }
        if (id == R.id.opTrangChu) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        if (id == R.id.opHinhAnh) {
            startActivity(new Intent(this, HinhAnh.class));
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
