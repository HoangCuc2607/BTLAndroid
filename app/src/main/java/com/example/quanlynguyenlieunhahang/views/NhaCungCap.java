package com.example.quanlynguyenlieunhahang.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quanlynguyenlieunhahang.R;
import com.example.quanlynguyenlieunhahang.controllers.ingredientsDAOController;
import com.example.quanlynguyenlieunhahang.controllers.suppliersDAOController;
import com.example.quanlynguyenlieunhahang.databinding.ActivityNhaCungCapBinding;
import com.example.quanlynguyenlieunhahang.fragments.RecyclerViewSuppliers;
import com.example.quanlynguyenlieunhahang.models.Supplier;

import java.util.ArrayList;
import java.util.List;

public class NhaCungCap extends RootActivity{
    private ActivityNhaCungCapBinding binding;
    private suppliersDAOController supctrl;
    private String getName, getAddress, getContactInfo;
    private int selectedSupId;
    private ingredientsDAOController inctrl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityNhaCungCapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //khoi tao
        supctrl = new suppliersDAOController(this);
        inctrl = new ingredientsDAOController(this);
        //load listview
        loadRcvSup();
        //cac btn
        btnAddSup();
        btnUpdateSup();
        btnDeleteSup();
    }
    public void btnAddSup()
    {
        binding.btnThemNcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSupplier();
            }
        });
    }
    public void btnUpdateSup()
    {
        binding.btnSuaNcc.setOnClickListener(view -> {
            getInput();
            if(checkInput())
            {
                Supplier sup = new Supplier();
                sup.setId(selectedSupId);
                sup.setName(getName);
                sup.setAddress(getAddress);
                sup.setContactInfo(getContactInfo);
                if(supctrl.updateSupplier(sup)) {
                    Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    loadRcvSup();
                    deleteIngreBySupId(sup.getId());
                }
                else Toast.makeText(this, "Cập nhật không thành công!", Toast.LENGTH_SHORT).show();
                resetEntry();

            }
        });
    }
    public void btnDeleteSup()
    {
        binding.btnXoaNcc.setOnClickListener(view -> {
            if(supctrl.deleteSupplier(selectedSupId)) {
                Toast.makeText(this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                deleteIngreBySupId(selectedSupId);
                loadRcvSup();
            }
            else Toast.makeText(this, "Xóa không thành công!", Toast.LENGTH_SHORT).show();
            resetEntry();

        });
    }

    public void deleteIngreBySupId(int id)
    {
        if(inctrl.deleteIngreBySupId(id))
            Toast.makeText(this, "Da xoa cac nguyen lieu lien quan!", Toast.LENGTH_SHORT).show();
    }
    public void loadRcvSup()
    {
        List<Supplier> list = new ArrayList<>();
        list = supctrl.getAllSupplier();
        RecyclerViewSuppliers adapter = new RecyclerViewSuppliers(list, this);
        binding.rcvSuppliers.setAdapter(adapter);
        binding.rcvSuppliers.setLayoutManager(
                new LinearLayoutManager(this)
        );
        adapter.setOnSupplierClickListener(this::toEntry);

    }
    public void toEntry(Supplier sup)
    {
        binding.edtTenNcc.setText(sup.getName());
        binding.edtDiaChi.setText(sup.getAddress());
        binding.edtTtLienhe.setText(sup.getContactInfo());
        selectedSupId = sup.getId();
    }
    public void resetEntry()
    {
        binding.edtTenNcc.setText("");
        binding.edtDiaChi.setText("");
        binding.edtTtLienhe.setText("");
    }
    public void addSupplier()
    {
        getInput();
        if(checkInput())
        {
            Supplier sup = new Supplier();
            sup.setContactInfo(getContactInfo);
            sup.setAddress(getAddress);
            sup.setName(getName);
            supctrl.addSupplier(sup);
            Toast.makeText(this, "Them thanh cong!", Toast.LENGTH_SHORT).show();
            loadRcvSup();
        }
        resetEntry();
    }
    public void getInput()
    {
        getName = binding.edtTenNcc.getText().toString().trim();
        getAddress = binding.edtDiaChi.getText().toString().trim();
        getContactInfo = binding.edtTtLienhe.getText().toString().trim();
    }
    public boolean checkInput()
    {
        if(getName.isEmpty() || getAddress.isEmpty() || getContactInfo.isEmpty())
        {
            Toast.makeText(this, "Không được để trống trường nào!", Toast.LENGTH_SHORT).show();
            return false;
        }
//        if(!isSupNameExists())
//        {
//            Toast.makeText(this, "Đã có sẵn nhà cung cấp này!", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        return true;
    }
    public boolean isSupNameExists()
    {
        if(supctrl.getAllSupplier() != null)
        {
            List<Supplier> list = supctrl.getAllSupplier();
            for(int i = 0; i < list.size(); i++)
            {
                if(getName.equals(list.get(i).getName())) return false;
            }
            return true;
        }
        return true;
    }


}
