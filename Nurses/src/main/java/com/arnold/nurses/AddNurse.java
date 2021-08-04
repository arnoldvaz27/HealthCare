package com.arnold.nurses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arnold.nurses.database.NurseDatabase;
import com.arnold.nurses.entities.Nurse;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"deprecation"})
public class AddNurse extends AppCompatActivity{

    private EditText name,designation,age,email,phoneNumber,dateJoining,gender;
    private CardView male,female;
    private ImageView addNurse;
    ImageView dateOfJoinImage;
    private final Pattern pattern1 = Pattern.compile( "^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+");

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.yellow));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        setContentView(R.layout.add_nurse);

        initView();

        name.setSelection(name.getText().length());
        name.requestFocus();
        name.getShowSoftInputOnFocus();

        male.setOnClickListener(v -> gender.setText("Male"));

        female.setOnClickListener(v -> gender.setText("Female"));
        addNurse.setOnClickListener(v -> {
            Matcher matcher1 = pattern1.matcher(email.getText().toString());

            if(TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(designation.getText().toString()) ||
                    TextUtils.isEmpty(age.getText().toString()) || TextUtils.isEmpty(email.getText().toString()) ||
                    TextUtils.isEmpty(phoneNumber.getText().toString()) || TextUtils.isEmpty(dateJoining.getText().toString()) ||
                    TextUtils.isEmpty(gender.getText().toString())) {
                showToast("You need to add data in all fields in order to add the doctor.");
            }else if (!matcher1.matches()) {
                showToast("Invalid Email, please provide valid email");
            }else if(phoneNumber.getText().toString().length() > 10 || phoneNumber.getText().toString().length() < 10){
                showToast("Please provide valid 10 digit phone number");
            }else if(gender.getText().toString().equals("Choose Profile Type")){
                showToast("Provide valid profile type");
            }else{
                saveNurse();
            }
        });
        dateOfJoinImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(dateOfJoinImage);
            }
        });

    }

    public void Click(View v) {

        if (v == dateOfJoinImage) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            dateJoining.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

    }

    private void saveNurse() {
        final Nurse nurse = new Nurse();
        nurse.setName(name.getText().toString());
        nurse.setDesignation(designation.getText().toString());
        nurse.setAge(age.getText().toString());
        nurse.setEmail(email.getText().toString());
        nurse.setPhoneNumber(phoneNumber.getText().toString());
        nurse.setDateJoining(dateJoining.getText().toString());
        nurse.setGender(gender.getText().toString());

        @SuppressLint("StaticFieldLeak")
        class SaveNursesTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                NurseDatabase.getNurseDatabase(getApplicationContext()).nursesDao().insertNurse(nurse);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent(AddNurse.this, Nurses.class);
                startActivity(intent);
                finish();

            }
        }

        new SaveNursesTask().execute();
    }

    private void initView() {
        name = findViewById(R.id.name);
        designation = findViewById(R.id.designation);
        age = findViewById(R.id.age);
        email = findViewById(R.id.floor);
        phoneNumber = findViewById(R.id.stockArrive);
        dateJoining = findViewById(R.id.dateJoining);
        dateOfJoinImage = findViewById(R.id.dateOfJoiningImage);
        gender = findViewById(R.id.gender);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        addNurse = findViewById(R.id.addNurse);
    }
    void showToast(String message) {
        Toast toast = new Toast(AddNurse.this);

        @SuppressLint("InflateParams") View view = LayoutInflater.from(AddNurse.this)
                .inflate(R.layout.toast_red, null);

        TextView tvMessage = view.findViewById(R.id.tvMessage);
        tvMessage.setText(message);

        toast.setView(view);
        toast.show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddNurse.this, Nurses.class);
        startActivity(intent);
        finish();
    }
}