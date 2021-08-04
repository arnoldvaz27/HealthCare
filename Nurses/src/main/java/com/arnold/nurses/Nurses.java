package com.arnold.nurses;

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

import com.arnold.doctors.DoctorsHome;
import com.arnold.nurses.adapters.NurseAdapter;
import com.arnold.nurses.database.NurseDatabase;
import com.arnold.nurses.entities.Nurse;
import com.arnold.nurses.listeners.NurseListeners;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"deprecation"})
public class Nurses extends AppCompatActivity implements NurseListeners {
    private RecyclerView recyclerView;
    private List<Nurse> nurseList;
    private NurseAdapter nurseAdapter;
    private ImageView addNurse;
    private Nurse nurse;
    private BottomSheetDialog bottomSheetDialog;
    private final Pattern pattern1 = Pattern.compile( "^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.yellow));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        setContentView(R.layout.nurses);
        addNurse = findViewById(R.id.addNurse);
        recyclerView = findViewById(R.id.RecyclerView);
        EditText inputSearch = findViewById(R.id.inputSearch);
        addNurse.setEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Nurses.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        nurseList = new ArrayList<>();
        nurseAdapter = new NurseAdapter(nurseList, Nurses.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(nurseAdapter);
        getNurse();
        findViewById(com.arnold.doctors.R.id.info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Info();
            }
        });
        addNurse.setOnClickListener(v -> {
            addNurse.setEnabled(false);
            startActivity(new Intent(getApplicationContext(), AddNurse.class));
            finish();
        });
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nurseAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (nurseList.size() != 0) {
                    nurseAdapter.searchNurse(s.toString());
                }
            }
        });
    }

    @Override
    public void onNurseClicked(Nurse note, int position) {
        nurse = note;
        BottomSheet();
    }
    private void getNurse() {
        @SuppressLint("StaticFieldLeak")
        class getNurseTask extends AsyncTask<Void, Void, List<Nurse>> {

            @Override
            protected List<Nurse> doInBackground(Void... voids) {
                return NurseDatabase.getNurseDatabase(getApplicationContext())
                        .nursesDao().getAllNurses();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(List<Nurse> nurses) {
                super.onPostExecute(nurses);
                nurseList.addAll(nurses);
                nurseAdapter.notifyDataSetChanged();
            }
        }

        new getNurseTask().execute();
    }
    private void BottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(Nurses.this,R.style.BottomSheetTheme);
        bottomSheetDialog.setCanceledOnTouchOutside(false);

        final View sheetView = LayoutInflater.from(Nurses.this).inflate(R.layout.nurse_bottomsheet, findViewById(R.id.layoutMoreOptions));

        final CardView Close,Delete,Edit,Save,Discard;
        final GridLayout editingGrid,viewingGrid;
        final TextView name,gender;
        final EditText number,designation,email,age,dateJoined;

        name = sheetView.findViewById(R.id.nurseName);
        number = sheetView.findViewById(R.id.number);
        designation = sheetView.findViewById(R.id.nurseDesignation);
        email = sheetView.findViewById(R.id.floor);
        age = sheetView.findViewById(R.id.age);
        gender = sheetView.findViewById(R.id.gender);
        dateJoined = sheetView.findViewById(R.id.dateJoin);
        Close = sheetView.findViewById(R.id.close);
        Delete = sheetView.findViewById(R.id.delete);
        Edit = sheetView.findViewById(com.arnold.doctors.R.id.edit);
        Save = sheetView.findViewById(com.arnold.doctors.R.id.save);
        Discard = sheetView.findViewById(com.arnold.doctors.R.id.discard);
        editingGrid = sheetView.findViewById(com.arnold.doctors.R.id.editingGrid);
        viewingGrid = sheetView.findViewById(com.arnold.doctors.R.id.viewingGrid);

        name.setText(nurse.getName());
        number.setText(nurse.getPhoneNumber());
        designation.setText(nurse.getDesignation());
        email.setText(nurse.getEmail());
        age.setText(nurse.getAge());
        gender.setText(nurse.getGender());
        dateJoined.setText(nurse.getDateJoining());

        Edit.setOnClickListener(v -> {
            viewingGrid.setVisibility(View.GONE);
            editingGrid.setVisibility(View.VISIBLE);
            designation.setEnabled(true);
            number.setEnabled(true);
            email.setEnabled(true);
            age.setEnabled(true);
            dateJoined.setEnabled(true);
            designation.setSelection(designation.getText().length());
            designation.requestFocus();
            designation.getShowSoftInputOnFocus();
        });
        Close.setOnClickListener(v -> bottomSheetDialog.cancel());
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Nurse nurse = new Nurse();
                nurse.setId(Nurses.this.nurse.getId());

                @SuppressLint("StaticFieldLeak")
                class SaveNursesTask extends AsyncTask<Void, Void, Void> {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        NurseDatabase.getNurseDatabase(getApplicationContext()).nursesDao().deleteNurse(nurse);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        displayNurse();
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
                        TextUtils.isEmpty(number.getText().toString()) || TextUtils.isEmpty(dateJoined.getText().toString()) ||
                        TextUtils.isEmpty(gender.getText().toString())) {
                    showToast("You need to add data in all fields in order to add the doctor.");
                }else if (!matcher1.matches()) {
                    showToast("Invalid Email, please provide valid email");
                }else if(number.getText().toString().length() > 10 || number.getText().toString().length() < 10){
                    showToast("Please provide valid 10 digit phone number");
                }else{

                    final Nurse nurse = new Nurse();
                    nurse.setName(name.getText().toString());
                    nurse.setDesignation(designation.getText().toString());
                    nurse.setPhoneNumber(number.getText().toString());
                    nurse.setEmail(email.getText().toString());
                    nurse.setAge(age.getText().toString());
                    nurse.setGender(gender.getText().toString());
                    nurse.setDateJoining(dateJoined.getText().toString());
                    nurse.setId(Nurses.this.nurse.getId());

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
                            displayNurse();
                            bottomSheetDialog.cancel();

                        }
                    }

                    new SaveNursesTask().execute();
                }
            }

        });
        Discard.setOnClickListener(v -> bottomSheetDialog.cancel());

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }
    private void displayNurse() {
        nurseList = new ArrayList<>();
        nurseAdapter = new NurseAdapter(nurseList, Nurses.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(nurseAdapter);
        getNurse();
    }
    void showToast(String message) {
        Toast toast = new Toast(Nurses.this);

        @SuppressLint("InflateParams") View view = LayoutInflater.from(Nurses.this)
                .inflate(R.layout.toast_red, null);

        TextView tvMessage = view.findViewById(R.id.tvMessage);
        tvMessage.setText(message);

        toast.setView(view);
        toast.show();
    }
    private void Info() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Nurses.this, com.arnold.doctors.R.style.AlertDialog);
        builder.setTitle("Note");
        builder.setCancelable(false);

        final TextView groupNameField = new TextView(Nurses.this);
        groupNameField.setText("1) Click on the add button and enter the required details. \n\n2) While choosing the profile type, click on the male or female image to set the profile type. \n\n3) After clicking on the correct symbol, it will appear in the list. You can click on it to delete, edit the details of the nurses. \n\n4) You can search the nurses by their name using the search field");
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