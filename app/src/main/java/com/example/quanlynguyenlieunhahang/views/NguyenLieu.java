package com.example.quanlynguyenlieunhahang.views;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quanlynguyenlieunhahang.R;
import com.example.quanlynguyenlieunhahang.controllers.ingredientsDAOController;
import com.example.quanlynguyenlieunhahang.controllers.suppliersDAOController;
import com.example.quanlynguyenlieunhahang.controllers.transactionsDAOController;
import com.example.quanlynguyenlieunhahang.databinding.ActivityGiaoDichBinding;
import com.example.quanlynguyenlieunhahang.databinding.ActivityNguyenLieuBinding;
import com.example.quanlynguyenlieunhahang.fragments.RecyclerViewIngredients;
import com.example.quanlynguyenlieunhahang.models.Ingredient;
import com.example.quanlynguyenlieunhahang.models.Supplier;

import java.util.ArrayList;
import java.util.List;

public class NguyenLieu extends RootActivity {
    private ActivityNguyenLieuBinding binding = null;
    private ingredientsDAOController inctrl;
    private suppliersDAOController supctrl;
    private transactionsDAOController tranctrl;
    EditText edtTenNguyenLieu, edtSoLuong, edtHanSuDung, edtGiaTien;
    Spinner spnDonViDo, spnNCC;
    ImageButton ibtnChonNgay, ibtnSua, ibtnThem;
    ListView lvNguyenLieu;
    List<Supplier> supplierList;
    private String getIngredientName, getQuantity, getUnit, getExpiration, getSupplierName, getPrice;
    private int ingreSelectedId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityNguyenLieuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Tai cac gia tri
        setValues();
        //load cac ibtn
        ibtnAddIngredient();
        ibtnDate();
        btnUpdateIngre();

    }

    //cac button
    public void ibtnAddIngredient() {
        ibtnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredient();
            }
        });
    }

    public void addIngredient() {
        if (checkInput() && isIngredientExists()) {
            //lay sup id
            int supplierId = getSupplierId();
            double quantity = Double.parseDouble(getQuantity);
            double price = Double.parseDouble(getPrice);

            Ingredient ing = new Ingredient();
            ing.setName(getIngredientName);
            ing.setQuantity(quantity);
            ing.setPrice(price);
            ing.setExpirationDate(getExpiration);
            ing.setLowStock(quantity < 2);
            ing.setUnit(getUnit);
            ing.setSupplierId(supplierId);

            long id = inctrl.addIngredient(ing);
            if(id != -1)
            {
                Toast.makeText(this, "Thêm thành công nguyên liệu!", Toast.LENGTH_SHORT).show();
                loadRcvIn();
            }
            else Toast.makeText(this, "Thêm không thành công!", Toast.LENGTH_SHORT).show();
        }
    }
    public int getSupplierId()
    {
            List<Supplier> listsup = new ArrayList<>();
            listsup = supctrl.getAllSupplier();
            //check ten tuong ung voi id
            for(int i = 0; i < listsup.size(); i++)
            {
                if(getSupplierName.equals(listsup.get(i).getName())) return listsup.get(i).getId();
            }
        return -1;
    }

    public void setValues() {
        edtTenNguyenLieu = binding.tenNguyenLieu;
        edtGiaTien = binding.giaTien;
        edtHanSuDung = binding.hanSuDung;
        edtSoLuong = binding.soLuong;
        spnDonViDo = binding.spDonViDo;
        spnNCC = binding.spNhaCungCap;
        ibtnSua = binding.btnSua;
        ibtnThem = binding.btnThem;
        ibtnChonNgay = binding.btnDate;
        inctrl = new ingredientsDAOController(this);
        supctrl = new suppliersDAOController(this);
        tranctrl = new transactionsDAOController(this);
        loadSpnDonViDo();
        loadSpnNCC();
        loadRcvIn();


    }
    public void getInput()
    {
        getIngredientName = edtTenNguyenLieu.getText().toString().trim();
        getQuantity = edtSoLuong.getText().toString().trim();
        getExpiration = edtHanSuDung.getText().toString().trim();
        getPrice = edtGiaTien.getText().toString().trim();
        getUnit = spnDonViDo.getSelectedItem().toString();
        getSupplierName = null;
        getSupplierName = spnNCC.getSelectedItem().toString();
    }

    public void loadSpnDonViDo() {
        ArrayList<String> listDVD = new ArrayList<>();
        listDVD.add("Kg");
        listDVD.add("Gram");
        listDVD.add("Lít");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, listDVD);
        spnDonViDo.setAdapter(adapter);
        spnDonViDo.setSelection(0);
    }

    public void loadSpnNCC() {
        supplierList = supctrl.getAllSupplier();
        ArrayAdapter<Supplier> adapter = new ArrayAdapter<Supplier>(this,
                android.R.layout.simple_spinner_dropdown_item, supplierList);
        spnNCC.setAdapter(adapter);
        spnNCC.setSelection(0);
    }

    public boolean checkInput() {
        //lay thong tin dau vao
        getInput();
        //kiem tra rong
        if (getIngredientName.isEmpty() || getQuantity.isEmpty() || getPrice.isEmpty() || getExpiration.isEmpty() || getSupplierName.isEmpty()) {
            Toast.makeText(this, "Không được để trống trường nào! Nếu chưa có nhà cung cấp, hãy tạo trước!", Toast.LENGTH_SHORT).show();
            return false;
        }
        //kiem tra kieu du lieu
        try {
            Double.parseDouble(getPrice);
            Double.parseDouble(getQuantity);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "Vui lòng nhập đúng định dạng số", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public boolean isIngredientExists()
    {
        List<Ingredient> list = null;
        if(inctrl.getAllIngredient() != null)
        {
            list = inctrl.getAllIngredient();
            for(int i = 0; i < list.size(); i++) {
                if (getIngredientName.equals(list.get(i).getName())
                        && Double.parseDouble(getPrice) == list.get(i).getPrice()) {
                    Toast.makeText(this, "Nguyên liệu đã có sẵn!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            return true;
        }
        return true;
    }
    public void ibtnDate()
    {
        binding.btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDatePicker.show(NguyenLieu.this, date -> edtHanSuDung.setText(date));
            }
        });
    }
    //loadlistview
    public void loadRcvIn()
    {
        List<Ingredient> list = inctrl.getAllIngredient();
        RecyclerViewIngredients adapter = new RecyclerViewIngredients(this, list);
        binding.rcvIngredient.setAdapter(adapter);
        binding.rcvIngredient.setLayoutManager(new LinearLayoutManager(this));
        //set su kien click
        adapter.setOnClickIngreListener(new RecyclerViewIngredients.OnClickIngreListener() {
            @Override
            public void OnClick(Ingredient ingre) {
                toEditText(ingre);
            }

            @Override
            public void OnLongClick(int ingreId) {
                onDeleteIngre(ingreId);
            }
        });

    }
    public void toEditText(Ingredient ingre)
    {
        binding.tenNguyenLieu.setText(ingre.getName());
        binding.soLuong.setText(String.valueOf(ingre.getQuantity()));
        binding.hanSuDung.setText(ingre.getExpirationDate());
        binding.giaTien.setText(String.valueOf(ingre.getPrice()));
        //set spn dvd
        if(ingre.getUnit().equals("Kg")) spnDonViDo.setSelection(0);
        else if (ingre.getUnit().equals("Gram")) spnDonViDo.setSelection(1);
        else spnDonViDo.setSelection(2);
        //set spn nha cung cap
        if(findSupplierPositionById(ingre.getSupplierId()) != -1)
        {
            int pos = findSupplierPositionById(ingre.getSupplierId());
            binding.spNhaCungCap.setSelection(pos);
        }
        ingreSelectedId = ingre.getId();
    }

    public void btnUpdateIngre()
    {
        binding.btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInput())
                {
                    Ingredient in = new Ingredient();
                    in.setId(ingreSelectedId);
                    in.setQuantity(Double.parseDouble(getQuantity));
                    in.setName(getIngredientName);
                    in.setUnit(getUnit);
                    in.setExpirationDate(getExpiration);
                    in.setPrice(Double.parseDouble(getPrice));
                    //lay nha cc
                    Supplier sup = new Supplier();
                    sup = (Supplier) binding.spNhaCungCap.getSelectedItem();
                    in.setSupplierId(sup.getId());
                    //cap nhat
                    if(inctrl.updateIngredient(in))
                    {
                        Toast.makeText(NguyenLieu.this, "Cap nhat thanh cong!", Toast.LENGTH_SHORT).show();
                        resetEditText();
                        loadRcvIn();
                        deleteTransactionByIngreId(in.getId());
                    }
                    else
                        Toast.makeText(NguyenLieu.this, "Cap nhat khong thanh cong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onDeleteIngre(int ingreId)
    {
        new AlertDialog.Builder(this)
                .setTitle("Xac nhan xoa")
                .setMessage("Ban co chac chan muon xoa?")
                .setPositiveButton("Xoa", (dialog, which) ->{
                    if(inctrl.deleteIngredient(ingreId))
                    {
                        Toast.makeText(this, "Xoa thanh cong!", Toast.LENGTH_SHORT).show();
                        loadRcvIn();
                        deleteTransactionByIngreId(ingreId);
                    }
                    else Toast.makeText(this, "Xoa khong thanh cong!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Huy", null)
                .show();
    }

    public void deleteTransactionByIngreId(int id)
    {
        if(tranctrl.deleteTransactionsByIngreId(id))
            Toast.makeText(this, "Xoa cac giao dich lien quan thanh cong!", Toast.LENGTH_SHORT).show();
    }
    private int findSupplierPositionById(int supplierId) {
        for (int i = 0; i < supplierList.size(); i++) {
            if (supplierList.get(i).getId() == supplierId) {
                return i;
            }
        }
        return -1;
    }
    public void resetEditText()
    {
        binding.tenNguyenLieu.setText("");
        binding.soLuong.setText("");
        binding.hanSuDung.setText("");
        binding.giaTien.setText("");
    }


}

