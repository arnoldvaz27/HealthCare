package com.arnold.doctors;

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

import com.arnold.doctors.database.DoctorsDatabase;
import com.arnold.doctors.entities.Doctors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"deprecation"})
public class AddDoctor extends AppCompatActivity {

    private EditText name,designation,age,email,phoneNumber,experience,gender;
    private CardView male,female;
    private ImageView addDoctor;
    private final Pattern pattern1 = Pattern.compile( "^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+");

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.dark_green));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        setContentView(R.layout.add_doctor);

        initView();

        name.setSelection(name.getText().length());
        name.requestFocus();
        name.getShowSoftInputOnFocus();

        male.setOnClickListener(v -> gender.setText("Male"));

        female.setOnClickListener(v -> gender.setText("Female"));
        addDoctor.setOnClickListener(v -> {
            Matcher matcher1 = pattern1.matcher(email.getText().toString());

            if(TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(designation.getText().toString()) ||
            TextUtils.isEmpty(age.getText().toString()) || TextUtils.isEmpty(email.getText().toString()) ||
            TextUtils.isEmpty(phoneNumber.getText().toString()) || TextUtils.isEmpty(experience.getText().toString()) ||
            TextUtils.isEmpty(gender.getText().toString())) {
                showToast("You need to add data in all fields in order to add the doctor.");
            }else if (!matcher1.matches()) {
                showToast("Invalid Email, please provide valid email");
            }else if(phoneNumber.getText().toString().length() > 10 || phoneNumber.getText().toString().length() < 10){
                showToast("Please provide valid 10 digit phone number");
            }else if(gender.getText().toString().equals("Choose Profile Type")){
                showToast("Provide valid profile type");
            }else{
                saveDoctor();
            }
        });
    }

    private void initView() {
        name = findViewById(R.id.name);
        designation = findViewById(R.id.designation);
        age = findViewById(R.id.age);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        experience = findViewById(R.id.experience);
        gender = findViewById(R.id.gender);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        addDoctor = findViewById(R.id.addDoctor);
    }
    void showToast(String message) {
        Toast toast = new Toast(AddDoctor.this);

        @SuppressLint("InflateParams") View view = LayoutInflater.from(AddDoctor.this)
                .inflate(R.layout.toast_red, null);

        TextView tvMessage = view.findViewById(R.id.tvMessage);
        tvMessage.setText(message);

        toast.setView(view);
        toast.show();
    }
    private void saveDoctor() {

        final Doctors doctors = new Doctors();
        doctors.setName(name.getText().toString());
        doctors.setDesignation(designation.getText().toString());
        doctors.setAge(age.getText().toString());
        doctors.setEmail(email.getText().toString());
        doctors.setPhoneNumber(phoneNumber.getText().toString());
        doctors.setExperience(experience.getText().toString());
        doctors.setGender(gender.getText().toString());

        @SuppressLint("StaticFieldLeak")
        class SaveDoctorTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DoctorsDatabase.getDoctorsDatabase(getApplicationContext()).doctorsDao().insertDoctor(doctors);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent(AddDoctor.this, DoctorsHome.class);
                startActivity(intent);
                finish();

            }
        }

        new SaveDoctorTask().execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddDoctor.this, DoctorsHome.class);
        startActivity(intent);
        finish();
    }
}