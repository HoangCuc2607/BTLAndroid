package com.example.quanlynguyenlieunhahang.views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynguyenlieunhahang.R;
import com.example.quanlynguyenlieunhahang.controllers.ingredientsDAOController;
import com.example.quanlynguyenlieunhahang.controllers.suppliersDAOController;
import com.example.quanlynguyenlieunhahang.controllers.transactionsDAOController;
import com.example.quanlynguyenlieunhahang.databinding.ActivityGiaoDichBinding;
import com.example.quanlynguyenlieunhahang.fragments.RecyclerViewTransactions;
import com.example.quanlynguyenlieunhahang.models.Ingredient;
import com.example.quanlynguyenlieunhahang.models.Supplier;
import com.example.quanlynguyenlieunhahang.models.Transaction;

import java.util.ArrayList;
import java.util.List;

public class GiaoDich extends RootActivity {
    private ActivityGiaoDichBinding binding;
    private ingredientsDAOController inctrl;
    private transactionsDAOController tranctrl;
    private suppliersDAOController supctrl;
    private String getIngreName, getTranQuantity, getTranUnit, getTranType, getTranDate, getTranNote;
    private Ingredient ingre;
    private Supplier sup;
    public List<Transaction> transactionsList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityGiaoDichBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //1. Them giao dich
        setCtrl();
        //set cac spinner
        setSpnDVD();
        setSpnNCC();
        setRdTran();
        //cac button
        ibtnSetDate();
        ibtnAddTransaction();
        setBtnThemGiaoDich();
        setBtnXemLichSuGiaoDich();

        //2. Lich su giao dich
        //set rcv
        setDefaultRcv();
        //set btn tim kiem theo ngay
        btnFindByDate();
        btnSetDateFind();



    }
    public void setBtnThemGiaoDich()
    {
        binding.btnThemGiaoDich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.viewSwitcher.setDisplayedChild(1);
            }
        });
    }
    public void setBtnXemLichSuGiaoDich()
    {
        binding.btnLichSuGiaoDich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.viewSwitcher.setDisplayedChild(0);
            }
        });
    }

    public void setCtrl()
    {
        inctrl = new ingredientsDAOController(this);
        tranctrl = new transactionsDAOController(this);
        supctrl = new suppliersDAOController(this);
    }
    public void setSpnIngreName(int supId)
    {
       List<Ingredient> listIngre = new ArrayList<>();
       listIngre = inctrl.getIngrediensBySupId(supId);
       ArrayAdapter<Ingredient> adapter = new ArrayAdapter<Ingredient>(this, android.R.layout.simple_spinner_dropdown_item, listIngre);
       binding.spnTenNguyenLieu.setAdapter(adapter);
    }
    public void setSpnNCC()
    {
        List<Supplier> listSup = supctrl.getAllSupplier();
        ArrayAdapter<Supplier> adapter = new ArrayAdapter<Supplier>(this, android.R.layout.simple_spinner_dropdown_item, listSup);
        binding.spnTenNhaCc.setAdapter(adapter);
        binding.spnTenNhaCc.setSelection(0);

        //lien ket theo spinner nguyen lieu
        binding.spnTenNhaCc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Supplier selectedSup = (Supplier) adapterView.getItemAtPosition(i);
                setSpnIngreName(selectedSup.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void ibtnAddTransaction()
    {
        binding.btnThemGd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTransaction();
            }
        });
    }
    public void addTransaction()
    {
        getValues();
        if(CheckInput())
        {
            Double newQuantity = DoiDonViDo(ingre);
            if(CheckTranSaction(newQuantity)) {
                Transaction tran = new Transaction();
                tran.setTransactionDate(getTranDate);
                tran.setNote(getTranNote);
                tran.setUnit(getTranUnit);
                tran.setQuantity(Double.parseDouble(getTranQuantity));
                tran.setTransactionType(getTranType);
                tran.setIngredientID(ingre.getId());
                Double totalAmount = totalAmount(Double.parseDouble(getTranQuantity));
                tran.setTotalAmount(totalAmount);
                //tinh tong tien giao dich
                //Them giao dich vao bang
                long id = tranctrl.addTransaction(tran);
                if(id != -1) {
                    Toast.makeText(this, "Thêm giao dịch thành công!", Toast.LENGTH_SHORT).show();

                    //Cap nhat lai so lương nguyen lieu
                    if (!inctrl.updateIngredient(ingre))
                        Toast.makeText(this, "Không thể cập nhật được số lượng nguyên liệu!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "Cập nhật số lượng nguyên liệu thành công!", Toast.LENGTH_SHORT).show();
                    setDefaultRcv();
                }
                else
                    Toast.makeText(this, "Thêm giao dịch không thành công!", Toast.LENGTH_SHORT).show();
            }
        }

    }
    public Double totalAmount(Double quantity)
    {
        //lay gia tien nguyen lieu
        Double price = ingre.getPrice();
        return quantity*price;
    }

    public void ibtnSetDate()
    {
        binding.btnChonNgayGd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDatePicker.show(GiaoDich.this, date -> binding.edtNgayGiaoDich.setText(date));
            }
        });
    }
    public void setSpnDVD()
    {
        ArrayList<String> listdvd = new ArrayList<>();
        listdvd.add("Kg");
        listdvd.add("Gram");
        listdvd.add("lít");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listdvd);
        binding.spnDonvido.setAdapter(adapter);
    }
    public void setRdTran()
    {
        binding.rdNhapXuat.check(binding.rdNhap.getId());
    }
    public void getValues()
    {
        //lay thogn tin cua nha cung cap truoc
        sup = new Supplier();
        sup = (Supplier) binding.spnTenNhaCc.getSelectedItem();

        ingre = new Ingredient();
        ingre = (Ingredient) binding.spnTenNguyenLieu.getSelectedItem();


        getTranQuantity = binding.edtSoLuongNX.getText().toString();
        getTranUnit = binding.spnDonvido.getSelectedItem().toString();

        int i = binding.rdNhapXuat.getCheckedRadioButtonId();
        if(i == binding.rdNhap.getId()) getTranType = "Nhập";
        else getTranType = "Xuất";

        getTranDate = binding.edtNgayGiaoDich.getText().toString();
        getTranNote = binding.edtNhapGhiChu.getText().toString();

    }
    public boolean CheckInput()
    {
        getValues();
        if(getTranQuantity.isEmpty() || getTranDate.isEmpty())
        {
            Toast.makeText(this, "KKhông được để trống số lượng và ngày!", Toast.LENGTH_SHORT).show();
            return false;
        }
        //kiem tra 2 spn nha cung cap va nguyen lieu
        if(sup == null || ingre == null) {
            Toast.makeText(this, "Khong duoc de trong truong nao!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean CheckTranSaction(Double newQuantity)
    {
        if (newQuantity == -1.0) {
            Toast.makeText(this, "Đơn vị đo không phù hơp!", Toast.LENGTH_SHORT).show();
            return false;
        }
        Double oldQuantity = ingre.getQuantity();
        //Check giao dich nhap hay xuat
        if(getTranType.equals("Nhập")) {
            ingre.setQuantity(oldQuantity + newQuantity);
            return true;
        }else {
            if(CheckXuat(newQuantity, ingre.getQuantity())) {
                ingre.setQuantity(oldQuantity - newQuantity);
                return true;
            }
            else return false;
        }
    }
    public boolean CheckXuat(Double quantity, Double ingreQuantity)
    {
        if(quantity > ingreQuantity)
        {
            Toast.makeText(this, "Không đủ số lượng xuất kho!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public Double DoiDonViDo(Ingredient ingre)
    {
        if (ingre.getUnit().equals("Lít"))
        {
            if(!getTranUnit.equals("Lít")) return Double.parseDouble(getTranQuantity);
            return -1.0;
        }
        else if (ingre.getUnit().equals("Gram"))
        {
            if(getTranUnit.equals("Gram")) return Double.parseDouble(getTranQuantity);
            else if(getTranUnit.equals("Kg")) return Double.parseDouble(getTranQuantity)*1000;
            return -1.0;
        }
        else{
            if(getTranUnit.equals("Kg")) return Double.parseDouble(getTranQuantity);
            else if (getTranUnit.equals("Gram")) return Double.parseDouble(getTranQuantity)/1000;
            else return -1.0;
            }
    }
    //Lich su giao dich
    public void setRcvTransactions(List<Transaction> list)
    {
        RecyclerViewTransactions adapter = new RecyclerViewTransactions(list, this);
        binding.rcvTransactions.setAdapter(adapter);
        binding.rcvTransactions.setLayoutManager(new LinearLayoutManager(this));
    }
    public void setDefaultRcv()
    {
        List<Transaction> list = new ArrayList<>();
        list = tranctrl.getAllTransaction();
        setRcvTransactions(list);
    }

    public void btnFindByDate()
    {
        binding.btnTimkiemGd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Transaction> list = new ArrayList<>();

                String getDateToFind = binding.edtTkNgayThang.getText().toString();
                Log.d("Get tran date: ", "Ngay ne"+getDateToFind);

                list = tranctrl.getTransByDate(getDateToFind);

                setRcvTransactions(list);
            }
        });
    }

    public void btnSetDateFind()
    {
        binding.btnChonNgaythangTk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDatePicker.show(GiaoDich.this, date -> binding.edtTkNgayThang.setText(date));
            }
        });
    }
    //het
}

