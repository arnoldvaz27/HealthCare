package com.arnold.medicines;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arnold.medicines.adapters.MedicinesAdapter;
import com.arnold.medicines.database.MedicinesDatabase;
import com.arnold.medicines.entities.Medicine;
import com.arnold.medicines.listeners.MedicinesListeners;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SuppressWarnings({"deprecation"})
public class Medicines extends AppCompatActivity implements MedicinesListeners {
    private RecyclerView RecyclerView;
    private List<Medicine> medicineList;
    private MedicinesAdapter medicinesAdapter;
    private AlertDialog dialogAddMedicines;
    private Medicine medicine;
    private BottomSheetDialog bottomSheetDialog;
    EditText name, stock, stockExpiry, stockArrived;
    ImageView stockExpiryImage,stockArriveImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.dark_blue));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        setContentView(R.layout.medicines);

        ImageView addMedicine = findViewById(R.id.addMedicines);
        RecyclerView = findViewById(R.id.RecyclerView);
        EditText inputSearch = findViewById(R.id.inputSearch);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Medicines.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        RecyclerView.setLayoutManager(linearLayoutManager);
        medicineList = new ArrayList<>();
        medicinesAdapter = new MedicinesAdapter(medicineList, Medicines.this);
        RecyclerView.setHasFixedSize(true);
        RecyclerView.setAdapter(medicinesAdapter);
        getMedicine();
        findViewById(R.id.info).setOnClickListener(v -> Info());
        addMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogAddMedicines == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Medicines.this);
                    View view = LayoutInflater.from(Medicines.this).inflate(
                            R.layout.layout_add_medicines, findViewById(R.id.layoutAddMedicinesContainer)
                    );
                    builder.setView(view);

                    dialogAddMedicines = builder.create();
                    if (dialogAddMedicines.getWindow() != null) {
                        dialogAddMedicines.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    }


                    name = view.findViewById(R.id.inputName);
                    stock = view.findViewById(R.id.stock);
                    stockExpiry = view.findViewById(R.id.stockExpiry);
                    stockArrived = view.findViewById(R.id.stockArrive);
                    stockExpiryImage = view.findViewById(R.id.stockExpiryImage);
                    stockArriveImage = view.findViewById(R.id.stockArriveImage);

                    name.setSelection(name.getText().length());
                    name.requestFocus();

                    stockExpiryImage.setOnClickListener(v12 -> Click(stockExpiryImage));
                    stockArriveImage.setOnClickListener(v13 -> Click(stockArriveImage));


                    view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(stock.getText().toString()) ||
                                    TextUtils.isEmpty(stockExpiry.getText().toString()) || TextUtils.isEmpty(stockArrived.getText().toString())) {
                                showToast();
                            } else {
                                final Medicine ambulance = new Medicine();
                                ambulance.setName(name.getText().toString());
                                ambulance.setStock(stock.getText().toString());
                                ambulance.setStockExpiry(stockExpiry.getText().toString());
                                ambulance.setStockArrived(stockArrived.getText().toString());

                                @SuppressLint("StaticFieldLeak")
                                class saveMedicineTask extends AsyncTask<Void, Void, Void> {

                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        MedicinesDatabase.getMedicinesDatabase(getApplicationContext()).medicinesDao().insertMedicine(ambulance);
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        super.onPostExecute(aVoid);
                                        displayAgain();
                                        dialogAddMedicines.cancel();
                                    }
                                }
                                new saveMedicineTask().execute();
                                name.setText("");
                                stock.setText("");
                                stockExpiry.setText("");
                                stockArrived.setText("");
                            }
                        }
                    });

                    view.findViewById(R.id.textCancel).setOnClickListener(v1 -> {
                        displayAgain();
                        dialogAddMedicines.dismiss();

                    });
                }
                dialogAddMedicines.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                dialogAddMedicines.show();
            }
        });
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                medicinesAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (medicineList.size() != 0) {
                    medicinesAdapter.searchMedicine(s.toString());
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    void showToast() {
        Toast toast = new Toast(Medicines.this);

        @SuppressLint("InflateParams") View view = LayoutInflater.from(Medicines.this)
                .inflate(com.arnold.doctors.R.layout.toast_red, null);

        TextView tvMessage = view.findViewById(com.arnold.doctors.R.id.tvMessage);
        tvMessage.setText("Please enter details in all the fields");

        toast.setView(view);
        toast.show();
    }

    @Override
    public void onMedicinesClicked(Medicine note, int position) {
        medicine = note;
        BottomSheet();

    }

    @SuppressLint("SetTextI18n")
    private void BottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(Medicines.this, R.style.BottomSheetTheme);
        bottomSheetDialog.setCanceledOnTouchOutside(false);

        final View sheetView = LayoutInflater.from(Medicines.this).inflate(R.layout.medicine_bottomsheet, findViewById(R.id.layoutMoreOptions));

        final CardView Close, Delete, Edit, Save, Discard;
        final GridLayout editingGrid, viewingGrid;
        final TextView name, stockExpiry, stockArrived;
        final EditText stock;
        name = sheetView.findViewById(R.id.name);
        stock = sheetView.findViewById(R.id.stock);
        stockExpiry = sheetView.findViewById(R.id.stockExpiry);
        stockArrived = sheetView.findViewById(R.id.stockArrive);
        Close = sheetView.findViewById(R.id.close);
        Delete = sheetView.findViewById(R.id.delete);
        Edit = sheetView.findViewById(R.id.edit);
        Save = sheetView.findViewById(R.id.save);
        Discard = sheetView.findViewById(R.id.discard);
        editingGrid = sheetView.findViewById(R.id.editingGrid);
        viewingGrid = sheetView.findViewById(R.id.viewingGrid);

        name.setText("Medicine Name: " + medicine.getName());
        stock.setText("Stock: " + medicine.getStock());
        stockExpiry.setText("Stock Expiry: " + medicine.getStockExpiry());
        stockArrived.setText("Stock Arrived: " + medicine.getStockArrived());

        Edit.setOnClickListener(v -> {
            stock.setText(medicine.getStock());
            name.setText(medicine.getName());
            stockExpiry.setText(medicine.getStockExpiry());
            stockArrived.setText(medicine.getStockArrived());
            viewingGrid.setVisibility(View.GONE);
            editingGrid.setVisibility(View.VISIBLE);
            stock.setEnabled(true);
            stock.setSelection(stock.getText().length());
            stock.setShowSoftInputOnFocus(true);
            stock.requestFocus();
        });
        Close.setOnClickListener(v -> bottomSheetDialog.cancel());
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Medicine doctors = new Medicine();
                doctors.setId(medicine.getId());

                @SuppressLint("StaticFieldLeak")
                class saveMedicineTask extends AsyncTask<Void, Void, Void> {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        MedicinesDatabase.getMedicinesDatabase(getApplicationContext()).medicinesDao().deleteMedicine(doctors);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        displayAgain();
                        bottomSheetDialog.cancel();
                    }
                }

                new saveMedicineTask().execute();
            }
        });
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(stock.getText().toString()) ||
                        TextUtils.isEmpty(stockExpiry.getText().toString()) || TextUtils.isEmpty(stockArrived.getText().toString())) {
                    showToast();
                } else {
                    final Medicine medicine = new Medicine();
                    medicine.setName(name.getText().toString());
                    medicine.setStock(stock.getText().toString());
                    medicine.setStockArrived(stockArrived.getText().toString());
                    medicine.setStockExpiry(stockExpiry.getText().toString());
                    medicine.setId(Medicines.this.medicine.getId());

                    @SuppressLint("StaticFieldLeak")
                    class saveMedicineTask extends AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            MedicinesDatabase.getMedicinesDatabase(getApplicationContext()).medicinesDao().insertMedicine(medicine);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            displayAgain();
                            bottomSheetDialog.cancel();

                        }
                    }

                    new saveMedicineTask().execute();
                }

            }

        });
        Discard.setOnClickListener(v -> bottomSheetDialog.cancel());


        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    private void displayAgain() {
        medicineList = new ArrayList<>();
        medicinesAdapter = new MedicinesAdapter(medicineList, Medicines.this);
        RecyclerView.setHasFixedSize(true);
        RecyclerView.setAdapter(medicinesAdapter);
        getMedicine();
    }

    private void getMedicine() {
        @SuppressLint("StaticFieldLeak")
        class getMedicineTask extends AsyncTask<Void, Void, List<Medicine>> {

            @Override
            protected List<Medicine> doInBackground(Void... voids) {
                return MedicinesDatabase.getMedicinesDatabase(getApplicationContext())
                        .medicinesDao().getAllMedicine();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(List<Medicine> medicines) {
                super.onPostExecute(medicines);
                medicineList.addAll(medicines);
                medicinesAdapter.notifyDataSetChanged();
            }
        }

        new getMedicineTask().execute();
    }

    public void Click(View v) {

        if (v == stockExpiryImage) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);


            @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> stockExpiry.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year), mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == stockArriveImage) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);


            @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> stockArrived.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year), mYear, mMonth, mDay);
            datePickerDialog.show();
        }

    }
    private void Info() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Medicines.this,R.style.AlertDialog);
        builder.setTitle("Note");
        builder.setCancelable(false);

        final TextView groupNameField = new TextView(Medicines.this);
        groupNameField.setText("1) Click on the add button and enter the required details, to enter the stocks arrived and stock expiry click on the calendar icon in front the fields and select the required date. \n\n2) After adding the medicine, it will appear in the list. You can click on it to delete, edit stock of medicine. \n\n3) You can search the medicine by the name using the search field");
        groupNameField.setPadding(20,30,20,20);
        groupNameField.setTextColor(Color.BLACK);

        groupNameField.setBackgroundColor(Color.WHITE);
        builder.setView(groupNameField);

        builder.setPositiveButton("Done", (dialogInterface, i) -> dialogInterface.cancel());

        builder.show();
    }

}