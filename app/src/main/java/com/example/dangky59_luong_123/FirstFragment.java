package com.example.dangky59_luong_123;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Calendar;

public class FirstFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Spinner spnChonDichVu;
    Button btnDangKy;
    ImageButton imgNgaySinh;
    EditText edtNgaySinh, edtTenKhachHang, edtSDT, edtDiaChi;
    RadioGroup rdgPhuongThuc;
    RadioButton rbTienMat, rbNganHang, rbViDienTu;

    NavController navController;

    String[] danhSachDichVu;
    String dichVuDaChon;
    Calendar calendar = Calendar.getInstance();

    public static final String sharePrefName = "my_share_preferense";
    public static final String key_str  = "ten_str";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addViews();
        setViews();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        navController = NavHostFragment.findNavController(this);
        ((MainActivity)getActivity()).navController = navController;
    }

    private void setViews() {
        btnDangKy.setOnClickListener(this);
        imgNgaySinh.setOnClickListener(this);
        danhSachDichVu = getResources().getStringArray(R.array.ds_dich_vu);
        ArrayAdapter<String> adapter = new ArrayAdapter(
                ((MainActivity)getActivity()),
                android.R.layout.simple_spinner_item,
                danhSachDichVu
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnChonDichVu.setAdapter(adapter);
        spnChonDichVu.setOnItemSelectedListener(this);
    }

    private void addViews() {
        btnDangKy = ((MainActivity)getActivity()).findViewById(R.id.btnDangKy);
        spnChonDichVu = ((MainActivity)getActivity()).findViewById(R.id.spnChonDichVu);
        imgNgaySinh = ((MainActivity)getActivity()).findViewById(R.id.imgNgaySinh);
        edtTenKhachHang = ((MainActivity)getActivity()).findViewById(R.id.edtTenKhachHang);
        edtSDT = ((MainActivity)getActivity()).findViewById(R.id.edtSDT);
        edtDiaChi = ((MainActivity)getActivity()).findViewById(R.id.edtDiaChi);
        rdgPhuongThuc = ((MainActivity)getActivity()).findViewById(R.id.rdgPhuongThuc);
        edtNgaySinh = ((MainActivity)getActivity()).findViewById(R.id.edtNgaySinh);
        rbTienMat = ((MainActivity)getActivity()).findViewById(R.id.rbTienMat);
        rbNganHang = ((MainActivity)getActivity()).findViewById(R.id.rbNganHang);
        rbViDienTu = ((MainActivity)getActivity()).findViewById(R.id.rbViDienTu);
        setTextNgaySinh(calendar);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.btnDangKy: dangKy(); break;
            case R.id.imgNgaySinh: ChonNgaySinh(); break;
        }
    }

    private void ChonNgaySinh() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                setTextNgaySinh(calendar);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(((MainActivity)getActivity()), listener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void setTextNgaySinh(Calendar calendar) {
        StringBuilder builder = new StringBuilder();
        builder.append(calendar.get(Calendar.DAY_OF_MONTH)).append("/").
                append(calendar.get(Calendar.MONTH) + 1).append("/").
                append(calendar.get(Calendar.YEAR));
        edtNgaySinh.setText(builder);
    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        dichVuDaChon = danhSachDichVu[position];
    }

    public void onNothingSelected(AdapterView<?> parent) {}

    private void dangKy() {
        NavHostFragment.findNavController(this).navigate(R.id.action_FirstFragment_to_SecondFragment);
        SharedPreferences sharedPreferences = ((MainActivity)getActivity()).getSharedPreferences(sharePrefName, Context.MODE_PRIVATE);
        if(sharedPreferences != null)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String str = String.format(
                    "Chúc mừng khách hàng: %s\n" +
                            "Sinh ngày: %s\n" +
                            "Đã đăng ký thành công dịch vụ: %s\n" +
                            "Hình thức thanh toán: %s\n" +
                            "Chúng tôi sẽ liên lạc với bạn theo số điện thoại: %s",
                    edtTenKhachHang.getText().toString(),
                    edtNgaySinh.getText().toString(),
                    dichVuDaChon,
                    hinhThucThanhToan(),
                    edtSDT.getText().toString()
            );
            editor.putString(key_str, str);
            editor.commit();
        }
    }

    private String hinhThucThanhToan()
    {
        String str = "";
        if (rbViDienTu.isChecked()) {
            str = rbViDienTu.getText().toString();
        } else if (rbNganHang.isChecked()) {
            str = rbNganHang.getText().toString();
        } else if (rbTienMat.isChecked()) {
            str = rbTienMat.getText().toString();
        }
        return str;
    }
}