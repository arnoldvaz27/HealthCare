package com.arnold.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arnold.services.adapters.ServiceAdapter;
import com.arnold.services.database.ServiceDatabase;
import com.arnold.services.entities.Service;
import com.arnold.services.listeners.ServiceListeners;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"deprecation"})
public class Services extends AppCompatActivity implements ServiceListeners {
    private RecyclerView recyclerView;
    private List<Service> serviceList;
    private ServiceAdapter serviceAdapter;
    private AlertDialog dialogAddService;
    private Service service;
    private BottomSheetDialog bottomSheetDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.purple_200));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        setContentView(R.layout.services);
        ImageView addService = findViewById(R.id.addService);
        recyclerView = findViewById(R.id.RecyclerView);
        EditText inputSearch = findViewById(R.id.inputSearch);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Services.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        serviceList = new ArrayList<>();
        serviceAdapter = new ServiceAdapter(serviceList, Services.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(serviceAdapter);
        getCountries();
        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogAddService == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Services.this);
                    View view = LayoutInflater.from(Services.this).inflate(
                            R.layout.layout_add_service, (ViewGroup) findViewById(R.id.layoutAddAmbulanceContainer)
                    );
                    builder.setView(view);

                    dialogAddService = builder.create();
                    if (dialogAddService.getWindow() != null) {
                        dialogAddService.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    }

                    final EditText serviceName,startDate;
                    serviceName = view.findViewById(R.id.serviceName);
                    startDate = view.findViewById(R.id.startDate);

                    serviceName.setSelection(serviceName.getText().length());
                    serviceName.requestFocus();
                    serviceName.getShowSoftInputOnFocus();

                    view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(TextUtils.isEmpty(serviceName.getText().toString()) ||TextUtils.isEmpty(startDate.getText().toString())
                            ){
                                showToast();
                            }
                            else{
                                final Service ambulance = new Service();
                                ambulance.setServiceName(serviceName.getText().toString());
                                ambulance.setStartDate(startDate.getText().toString());
                                @SuppressLint("StaticFieldLeak")
                                class saveServiceTask extends AsyncTask<Void, Void, Void> {

                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        ServiceDatabase.getServiceDatabase(getApplicationContext()).serviceDao().insertServices(ambulance);
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        super.onPostExecute(aVoid);
                                        displayAgain();
                                        dialogAddService.cancel();
                                    }
                                }

                                new saveServiceTask().execute();
                            }
                        }
                    });

                    view.findViewById(R.id.textCancel).setOnClickListener(v1 -> {
                        displayAgain();
                        dialogAddService.dismiss();

                    });
                }

                dialogAddService.show();
            }
        });
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                serviceAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (serviceList.size() != 0) {
                    serviceAdapter.searchService(s.toString());
                }
            }
        });
    }

    @Override
    public void onServiceClicked(Service service, int position) {
        this.service = service;
        BottomSheet();
    }
    private void displayAgain() {
        serviceList = new ArrayList<>();
        serviceAdapter = new ServiceAdapter(serviceList, Services.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(serviceAdapter);
        getCountries();
    }
    @SuppressLint("SetTextI18n")
    void showToast() {
        Toast toast = new Toast(Services.this);

        @SuppressLint("InflateParams") View view = LayoutInflater.from(Services.this)
                .inflate(com.arnold.doctors.R.layout.toast_red, null);

        TextView tvMessage = view.findViewById(com.arnold.doctors.R.id.tvMessage);
        tvMessage.setText("Please enter details in all the fields");

        toast.setView(view);
        toast.show();
    }
    private void getCountries() {
        @SuppressLint("StaticFieldLeak")
        class getServiceTask extends AsyncTask<Void, Void, List<Service>> {

            @Override
            protected List<Service> doInBackground(Void... voids) {
                return ServiceDatabase.getServiceDatabase(getApplicationContext())
                        .serviceDao().getAllServices();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(List<Service> services) {
                super.onPostExecute(services);
                serviceList.addAll(services);
                serviceAdapter.notifyDataSetChanged();
            }
        }

        new getServiceTask().execute();
    }
    @SuppressWarnings({"deprecation"})
    @SuppressLint("SetTextI18n")
    private void BottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(Services.this,R.style.BottomSheetTheme);
        bottomSheetDialog.setCanceledOnTouchOutside(false);

        final View sheetView = LayoutInflater.from(Services.this).inflate(R.layout.service_bottomsheet, findViewById(R.id.layoutMoreOptions));

        final CardView Close,Delete;
        final TextView name,date;

        name = sheetView.findViewById(R.id.name);
        date = sheetView.findViewById(R.id.date);
        Close = sheetView.findViewById(R.id.close);
        Delete = sheetView.findViewById(R.id.delete);

        name.setText("Service Name: "+service.getServiceName());
        date.setText("Date:  "+service.getStartDate());

        Close.setOnClickListener(v -> bottomSheetDialog.cancel());
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Service doctors = new Service();
                doctors.setId(service.getId());

                @SuppressLint("StaticFieldLeak")
                class SaveNursesTask extends AsyncTask<Void, Void, Void> {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        ServiceDatabase.getServiceDatabase(getApplicationContext()).serviceDao().deleteServices(doctors);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        displayAgain();
                        bottomSheetDialog.cancel();

                    }
                }

                new SaveNursesTask().execute();
            }
        });


        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }
}