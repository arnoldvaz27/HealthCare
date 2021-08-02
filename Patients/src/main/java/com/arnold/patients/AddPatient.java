package com.arnold.patients;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arnold.patients.database.PatientsDatabase;
import com.arnold.patients.entities.Patient;

@SuppressWarnings({"deprecation"})
public class AddPatient extends AppCompatActivity {
    private EditText name,phoneNumber,diseaseType,doctorAppointed,status;
    private CardView cured,notCured;
    private ImageView addPatient;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.teal_700));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        setContentView(R.layout.add_patient);

        initView();

        name.setSelection(name.getText().length());
        name.requestFocus();
        name.getShowSoftInputOnFocus();

        cured.setOnClickListener(v -> status.setText("Cured"));

        notCured.setOnClickListener(v -> status.setText("Under Treatment"));
        addPatient.setOnClickListener(v -> {

            if(TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(phoneNumber.getText().toString()) ||
                    TextUtils.isEmpty(diseaseType.getText().toString()) || TextUtils.isEmpty(doctorAppointed.getText().toString()) ||
                    TextUtils.isEmpty(status.getText().toString())) {
                showToast("You need to add data in all fields in order to add the doctor.");
            }else if(phoneNumber.getText().toString().length() > 10 || phoneNumber.getText().toString().length() < 10){
                showToast("Please provide valid 10 digit phone number");
            }else{
                savePatient();
            }
        });
    }
    private void savePatient() {
        final Patient patient = new Patient();
        patient.setPatientName(name.getText().toString());
        patient.setPatientNumber(phoneNumber.getText().toString());
        patient.setDisease(diseaseType.getText().toString());
        patient.setAppointedDoctor(doctorAppointed.getText().toString());
        patient.setStatus(status.getText().toString());

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
                Intent intent = new Intent(AddPatient.this, Patients.class);
                startActivity(intent);
                finish();

            }
        }

        new savePatientTask().execute();
    }

    private void initView() {
        name = findViewById(R.id.name);
        phoneNumber = findViewById(R.id.phoneNumber);
        diseaseType = findViewById(R.id.diseaseType);
        doctorAppointed = findViewById(R.id.doctorAppointed);
        status = findViewById(R.id.status);
        cured = findViewById(R.id.cured);
        notCured = findViewById(R.id.notCured);
        addPatient = findViewById(R.id.addDoctor);
    }
    void showToast(String message) {
        Toast toast = new Toast(AddPatient.this);

        @SuppressLint("InflateParams") View view = LayoutInflater.from(AddPatient.this)
                .inflate(R.layout.toast_red, null);

        TextView tvMessage = view.findViewById(R.id.tvMessage);
        tvMessage.setText(message);

        toast.setView(view);
        toast.show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddPatient.this, Patients.class);
        startActivity(intent);
        finish();
    }
}