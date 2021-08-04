package com.arnold.doctors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arnold.doctors.adapters.DoctorsAdapter;
import com.arnold.doctors.database.DoctorsDatabase;
import com.arnold.doctors.entities.Doctors;
import com.arnold.doctors.listeners.DoctorsListeners;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"deprecation"})
public class DoctorsHome extends AppCompatActivity implements DoctorsListeners {

    private RecyclerView recyclerView;
    private List<Doctors> doctorsList;
    private DoctorsAdapter doctorsAdapter;
    private ImageView addDoctor;
    private Doctors doctors;
    private BottomSheetDialog bottomSheetDialog;
    private final Pattern pattern1 = Pattern.compile( "^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.dark_green));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        setContentView(R.layout.doctors_home);

        addDoctor = findViewById(R.id.addDoctor);
        recyclerView = findViewById(R.id.RecyclerView);
        EditText inputSearch = findViewById(R.id.inputSearch);
        addDoctor.setEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DoctorsHome.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        doctorsList = new ArrayList<>();
        doctorsAdapter = new DoctorsAdapter(this, doctorsList, DoctorsHome.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(doctorsAdapter);
        getDoctors();
        findViewById(R.id.info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Info();
            }
        });
        addDoctor.setOnClickListener(v -> {
            addDoctor.setEnabled(false);
            startActivity(new Intent(getApplicationContext(),AddDoctor.class));
            finish();
        });

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                doctorsAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (doctorsList.size() != 0) {
                    doctorsAdapter.searchNotes(s.toString());
                }
            }
        });
    }

    @Override
    public void onDoctorsClicked(Doctors doctors, int position) {
        this.doctors = doctors;
        BottomSheet();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addDoctor.setEnabled(true);

    }
    private void getDoctors() {
        @SuppressLint("StaticFieldLeak")
        class getDoctorsTask extends AsyncTask<Void, Void, List<Doctors>> {

            @Override
            protected List<Doctors> doInBackground(Void... voids) {
                return DoctorsDatabase.getDoctorsDatabase(getApplicationContext())
                        .doctorsDao().getAllDoctors();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(List<Doctors> doctors) {
                super.onPostExecute(doctors);
                doctorsList.addAll(doctors);
                doctorsAdapter.notifyDataSetChanged();
            }
        }

        new getDoctorsTask().execute();
    }

    private void BottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(DoctorsHome.this,R.style.BottomSheetTheme);
        bottomSheetDialog.setCanceledOnTouchOutside(false);

        final View sheetView = LayoutInflater.from(DoctorsHome.this).inflate(R.layout.doctor_bottomsheet, findViewById(R.id.layoutMoreOptions));

        final CardView Close,Delete,Edit,Save,Discard;
        final GridLayout editingGrid,viewingGrid;
        final TextView name,gender;
        final EditText number,designation,email,age,experience;

        name = sheetView.findViewById(R.id.name);
        number = sheetView.findViewById(R.id.number);
        designation = sheetView.findViewById(R.id.doctorDesignation);
        email = sheetView.findViewById(R.id.floor);
        age = sheetView.findViewById(R.id.age);
        gender = sheetView.findViewById(R.id.gender);
        experience = sheetView.findViewById(R.id.experience);
        Close = sheetView.findViewById(R.id.close);
        Delete = sheetView.findViewById(R.id.delete);
        Edit = sheetView.findViewById(R.id.edit);
        Save = sheetView.findViewById(R.id.save);
        Discard = sheetView.findViewById(R.id.discard);
        editingGrid = sheetView.findViewById(R.id.editingGrid);
        viewingGrid = sheetView.findViewById(R.id.viewingGrid);

        name.setText(doctors.getName());
        number.setText(doctors.getPhoneNumber());
        designation.setText(doctors.getDesignation());
        email.setText(doctors.getEmail());
        age.setText(doctors.getAge());
        gender.setText(doctors.getGender());
        experience.setText(doctors.getExperience());

        Edit.setOnClickListener(v -> {
            viewingGrid.setVisibility(View.GONE);
            editingGrid.setVisibility(View.VISIBLE);
            designation.setEnabled(true);
            number.setEnabled(true);
            email.setEnabled(true);
            age.setEnabled(true);
            experience.setEnabled(true);
            designation.setSelection(designation.getText().length());
            designation.requestFocus();
            designation.getShowSoftInputOnFocus();
        });
        Close.setOnClickListener(v -> bottomSheetDialog.cancel());
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Doctors doctors = new Doctors();
                doctors.setId(DoctorsHome.this.doctors.getId());

                @SuppressLint("StaticFieldLeak")
                class SaveNursesTask extends AsyncTask<Void, Void, Void> {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        DoctorsDatabase.getDoctorsDatabase(getApplicationContext()).doctorsDao().deleteDoctor(doctors);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        displayDoctors();
                        bottomSheetDialog.cancel();

                    }
                }

                new SaveNursesTask().execute();
            }
        });
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matcher matcher1 = pattern1.matcher(email.getText().toString());
                if(TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(designation.getText().toString()) ||
                        TextUtils.isEmpty(age.getText().toString()) || TextUtils.isEmpty(email.getText().toString()) ||
                        TextUtils.isEmpty(number.getText().toString()) || TextUtils.isEmpty(experience.getText().toString()) ||
                        TextUtils.isEmpty(gender.getText().toString())) {
                    showToast("You need to add data in all fields in order to add the doctor.");
                }else if (!matcher1.matches()) {
                    showToast("Invalid Email, please provide valid email");
                }else if(number.getText().toString().length() > 10 || number.getText().toString().length() < 10){
                    showToast("Please provide valid 10 digit phone number");
                }else{
                    final Doctors doctors = new Doctors();
                    doctors.setName(name.getText().toString());
                    doctors.setDesignation(designation.getText().toString());
                    doctors.setPhoneNumber(number.getText().toString());
                    doctors.setEmail(email.getText().toString());
                    doctors.setAge(age.getText().toString());
                    doctors.setGender(gender.getText().toString());
                    doctors.setExperience(experience.getText().toString());
                    doctors.setId(DoctorsHome.this.doctors.getId());

                    @SuppressLint("StaticFieldLeak")
                    class saveDoctorTask extends AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            DoctorsDatabase.getDoctorsDatabase(getApplicationContext()).doctorsDao().insertDoctor(doctors);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            displayDoctors();
                            bottomSheetDialog.cancel();

                        }
                    }

                    new saveDoctorTask().execute();
                }

            }

        });
        Discard.setOnClickListener(v -> bottomSheetDialog.cancel());

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }
    private void displayDoctors() {
        doctorsList = new ArrayList<>();
        doctorsAdapter = new DoctorsAdapter(this, doctorsList, DoctorsHome.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(doctorsAdapter);
        getDoctors();
    }
    void showToast(String message) {
        Toast toast = new Toast(DoctorsHome.this);

        @SuppressLint("InflateParams") View view = LayoutInflater.from(DoctorsHome.this)
                .inflate(R.layout.toast_red, null);

        TextView tvMessage = view.findViewById(R.id.tvMessage);
        tvMessage.setText(message);

        toast.setView(view);
        toast.show();
    }
    private void Info() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(DoctorsHome.this,R.style.AlertDialog);
        builder.setTitle("Note");
        builder.setCancelable(false);

        final TextView groupNameField = new TextView(DoctorsHome.this);
        groupNameField.setText("1) Click on the add button and enter the required details. \n\n2) While choosing the profile type, click on the male or female image to set the profile type. \n\n3) After clicking on the correct symbol, it will appear in the list. You can click on it to delete, edit the details of the doctor. \n\n4) You can search the doctor by their name using the search field");
        groupNameField.setPadding(20,30,20,20);
        groupNameField.setTextColor(Color.BLACK);

        groupNameField.setBackgroundColor(Color.WHITE);
        builder.setView(groupNameField);

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }
}