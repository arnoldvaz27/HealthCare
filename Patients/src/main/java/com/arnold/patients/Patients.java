package com.arnold.patients;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arnold.patients.adapters.PatientsAdapter;
import com.arnold.patients.database.PatientsDatabase;
import com.arnold.patients.entities.Patient;
import com.arnold.patients.listeners.PatientListeners;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"deprecation"})
public class Patients extends AppCompatActivity implements PatientListeners {
    private RecyclerView recyclerView;
    private List<Patient> patientList;
    private PatientsAdapter patientsAdapter;
    private ImageView addPatient;
    private Patient patient;
    private BottomSheetDialog bottomSheetDialog;
    private String PatientItem;
    String patientString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.teal_700));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        setContentView(R.layout.patients);
        addPatient = findViewById(R.id.addPatients);
        recyclerView = findViewById(R.id.RecyclerView);
        EditText inputSearch = findViewById(R.id.inputSearch);
        addPatient.setEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Patients.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        displayPatients();
        addPatient.setOnClickListener(v -> {
            addPatient.setEnabled(false);
            startActivity(new Intent(getApplicationContext(), AddPatient.class));
            finish();
        });
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                patientsAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (patientList.size() != 0) {
                    patientsAdapter.searchNotes(s.toString());
                }
            }
        });
    }

    private void displayPatients() {
        patientList = new ArrayList<>();
        patientsAdapter = new PatientsAdapter(this, patientList, Patients.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(patientsAdapter);
        getCountries();
    }

    @Override
    public void onPatientClicked(Patient patient, int position) {
        this.patient = patient;
        BottomSheet();
    }

    private void BottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(Patients.this,R.style.BottomSheetTheme);
        bottomSheetDialog.setCanceledOnTouchOutside(false);

        final View sheetView = LayoutInflater.from(Patients.this).inflate(R.layout.patients_bottomsheet, findViewById(R.id.layoutMoreOptions));

        final CardView Edit,Discard,Close,Save,Delete;
        final GridLayout editingGrid,viewingGrid;
        final TextView name,number,diseaseName,appointedDoctor,status;
        final LinearLayout statusLayout;
        String[] Subject;
        Spinner StreamChooseSubject;

        Edit = sheetView.findViewById(R.id.edit);
        Discard = sheetView.findViewById(R.id.discard);
        Close = sheetView.findViewById(R.id.close);
        Save = sheetView.findViewById(R.id.save);
        Delete = sheetView.findViewById(R.id.delete);
        editingGrid = sheetView.findViewById(R.id.editingGrid);
        viewingGrid = sheetView.findViewById(R.id.viewingGrid);
        name = sheetView.findViewById(R.id.name);
        number = sheetView.findViewById(R.id.phoneNumber);
        diseaseName = sheetView.findViewById(R.id.diseaseName);
        appointedDoctor = sheetView.findViewById(R.id.doctorAppoint);
        status = sheetView.findViewById(R.id.status);
        statusLayout = sheetView.findViewById(R.id.statusLayout);
        StreamChooseSubject = sheetView.findViewById(R.id.statusChoose);

        name.setText(patient.getPatientName());
        number.setText(patient.getPatientNumber());
        diseaseName.setText(patient.getDisease());
        appointedDoctor.setText(patient.getAppointedDoctor());
        status.setText(patient.getStatus());

        Subject = getResources().getStringArray(R.array.PatientStatus);
        
        ArrayAdapter<String> adapterStream2 = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, Subject);
        adapterStream2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        StreamChooseSubject.setAdapter(adapterStream2);
        StreamChooseSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                StreamChooseSubject.setSelection(i);
                PatientItem = adapterView.getItemAtPosition(i).toString();
                patientString = PatientItem;
                status.setText(patientString);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Edit.setOnClickListener(v -> {
            viewingGrid.setVisibility(View.GONE);
            editingGrid.setVisibility(View.VISIBLE);
            statusLayout.setVisibility(View.VISIBLE);
        });
        Close.setOnClickListener(v -> bottomSheetDialog.cancel());
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(number.getText().toString()) ||
                        TextUtils.isEmpty(diseaseName.getText().toString()) || TextUtils.isEmpty(appointedDoctor.getText().toString()) ||
                        TextUtils.isEmpty(status.getText().toString())) {
                    showToast("You need to add data in all fields in order to add the doctor.");
                }else if(number.getText().toString().length() > 10 || number.getText().toString().length() < 10){
                    showToast("Please provide valid 10 digit phone number");
                }else if(status.getText().toString().equals("Choose Status")){
                    showToast("Status Invalid, Please select the correct status");
                }else{
                    final Patient patient = new Patient();
                    patient.setPatientName(name.getText().toString());
                    patient.setPatientNumber(number.getText().toString());
                    patient.setDisease(diseaseName.getText().toString());
                    patient.setAppointedDoctor(appointedDoctor.getText().toString());
                    patient.setStatus(status.getText().toString());
                    patient.setId(Patients.this.patient.getId());

                    @SuppressLint("StaticFieldLeak")
                    class savePatientTask extends AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            PatientsDatabase.getPatientsDatabase(getApplicationContext()).patientDao().insertPatient(patient);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            displayPatients();
                            bottomSheetDialog.cancel();

                        }
                    }

                    new savePatientTask().execute();
                }
            }
        });
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Patient patient = new Patient();
                patient.setId(Patients.this.patient.getId());

                @SuppressLint("StaticFieldLeak")
                class savePatientTask extends AsyncTask<Void, Void, Void> {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        PatientsDatabase.getPatientsDatabase(getApplicationContext()).patientDao().deletePatient(patient);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        displayPatients();
                        bottomSheetDialog.cancel();

                    }
                }

                new savePatientTask().execute();
            }
        });
        Discard.setOnClickListener(v -> bottomSheetDialog.cancel());

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    private void getCountries() {
        @SuppressLint("StaticFieldLeak")
        class getPatientTask extends AsyncTask<Void, Void, List<Patient>> {

            @Override
            protected List<Patient> doInBackground(Void... voids) {
                return PatientsDatabase.getPatientsDatabase(getApplicationContext())
                        .patientDao().getAllPatient();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(List<Patient> patients) {
                super.onPostExecute(patients);
                patientList.addAll(patients);
                patientsAdapter.notifyDataSetChanged();
            }
        }

        new getPatientTask().execute();
    }
    void showToast(String message) {
        Toast toast = new Toast(Patients.this);

        @SuppressLint("InflateParams") View view = LayoutInflater.from(Patients.this)
                .inflate(com.arnold.doctors.R.layout.toast_red, null);

        TextView tvMessage = view.findViewById(R.id.tvMessage);
        tvMessage.setText(message);

        toast.setView(view);
        toast.show();
    }
}