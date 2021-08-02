package com.arnold.healthcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;

import com.arnold.ambulances.Ambulances;
import com.arnold.beds.Beds;
import com.arnold.canteen.Canteen;
import com.arnold.doctors.DoctorsHome;
import com.arnold.medicines.Medicines;
import com.arnold.nurses.Nurses;
import com.arnold.patients.Patients;
import com.arnold.services.Services;

public class Home extends AppCompatActivity {

    CardView medicines,doctors,patients,services,beds,ambulances,nurses,canteen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.red));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        setContentView(R.layout.home);

        medicines = findViewById(R.id.medicines);
        doctors = findViewById(R.id.doctors);
        patients = findViewById(R.id.patients);
        services = findViewById(R.id.services);
        beds = findViewById(R.id.beds);
        ambulances = findViewById(R.id.ambulances);
        nurses = findViewById(R.id.nurses);
        canteen = findViewById(R.id.canteen);

        medicines.setEnabled(true);
        doctors.setEnabled(true);
        patients.setEnabled(true);
        services.setEnabled(true);
        beds.setEnabled(true);
        ambulances.setEnabled(true);
        nurses.setEnabled(true);
        canteen.setEnabled(true);

        medicines.setOnClickListener(v -> {
            medicines.setEnabled(false);
            startActivity(new Intent(getApplicationContext(),Medicines.class));
        });
        doctors.setOnClickListener(v -> {
            doctors.setEnabled(false);
            startActivity(new Intent(getApplicationContext(), DoctorsHome.class));
        });
        patients.setOnClickListener(v -> {
            patients.setEnabled(false);
            startActivity(new Intent(getApplicationContext(),Patients.class));
        });
        services.setOnClickListener(v -> {
            services.setEnabled(false);
            startActivity(new Intent(getApplicationContext(),Services.class));
        });
        beds.setOnClickListener(v -> {
            beds.setEnabled(false);
            startActivity(new Intent(getApplicationContext(),Beds.class));
        });
        ambulances.setOnClickListener(v -> {
            ambulances.setEnabled(false);
            startActivity(new Intent(getApplicationContext(),Ambulances.class));
        });
        nurses.setOnClickListener(v -> {
            nurses.setEnabled(false);
            startActivity(new Intent(getApplicationContext(), Nurses.class));
        });
        canteen.setOnClickListener(v -> {
            canteen.setEnabled(false);
            startActivity(new Intent(getApplicationContext(),Canteen.class));
        });
        findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(Home.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.settings_file, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.logout) {
                        final SharedPreferences fieldsVisibility1 = getSharedPreferences("login", MODE_PRIVATE);
                        final SharedPreferences.Editor fieldsVisibility2 = fieldsVisibility1.edit();
                        fieldsVisibility2.putString("username", null);
                        fieldsVisibility2.putString("password", null);
                        fieldsVisibility2.apply();
                        startActivity(new Intent(getApplicationContext(),Login.class));
                        finishAffinity();
                    }
                    return true;
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        medicines.setEnabled(true);
        doctors.setEnabled(true);
        patients.setEnabled(true);
        services.setEnabled(true);
        beds.setEnabled(true);
        ambulances.setEnabled(true);
        nurses.setEnabled(true);
        canteen.setEnabled(true);
    }
}