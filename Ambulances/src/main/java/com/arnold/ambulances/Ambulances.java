package com.arnold.ambulances;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arnold.ambulances.adapters.AmbulanceAdapter;
import com.arnold.ambulances.database.AmbulanceDatabase;
import com.arnold.ambulances.entities.Ambulance;
import com.arnold.ambulances.listeners.AmbulanceListeners;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"deprecation"})
public class Ambulances extends AppCompatActivity implements AmbulanceListeners {
    private RecyclerView ambulanceRecyclerView;
    private List<Ambulance> ambulanceList;
    private AmbulanceAdapter ambulanceAdapter;
    private AlertDialog dialogAddAmbulance;
    private Ambulance ambulance;
    private BottomSheetDialog bottomSheetDialog;
    private String ambulanceStatusItem;
    String ambulanceStatusString;
    private final Pattern pattern1 = Pattern.compile( "^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.purple));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        setContentView(R.layout.ambulances);

        ImageView addAmbulance = findViewById(R.id.addAmbulance);
        ambulanceRecyclerView = findViewById(R.id.RecyclerView);
        EditText inputSearch = findViewById(R.id.inputSearch);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Ambulances.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        ambulanceRecyclerView.setLayoutManager(linearLayoutManager);
        ambulanceList = new ArrayList<>();
        ambulanceAdapter = new AmbulanceAdapter(ambulanceList, Ambulances.this);
        ambulanceRecyclerView.setHasFixedSize(true);
        ambulanceRecyclerView.setAdapter(ambulanceAdapter);
        getAmbulance();
        findViewById(R.id.info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Info();
            }
        });
        addAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogAddAmbulance == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Ambulances.this);
                    View view = LayoutInflater.from(Ambulances.this).inflate(
                            R.layout.layout_add_ambulance, findViewById(R.id.layoutAddAmbulanceContainer)
                    );
                    builder.setView(view);

                    dialogAddAmbulance = builder.create();
                    if (dialogAddAmbulance.getWindow() != null) {
                        dialogAddAmbulance.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    }

                    final EditText inputNumberPlate,persons,email,phoneNumber;
                    final TextView status;
                    String[] AmbulanceStatus;
                    Spinner AmbulanceChooseStatus;
                    inputNumberPlate = view.findViewById(R.id.inputNumberPlate);
                    persons = view.findViewById(R.id.persons);
                    email = view.findViewById(R.id.floor);
                    phoneNumber = view.findViewById(R.id.phone);
                    status = view.findViewById(R.id.status);
                    AmbulanceChooseStatus = view.findViewById(R.id.statusChoose);
                    AmbulanceStatus = getResources().getStringArray(R.array.AmbulanceStatus);

                    inputNumberPlate.setSelection(inputNumberPlate.getText().length());
                    inputNumberPlate.requestFocus();
                    inputNumberPlate.getShowSoftInputOnFocus();
                    ArrayAdapter<String> adapterStream2 = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, AmbulanceStatus);
                    adapterStream2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    AmbulanceChooseStatus.setAdapter(adapterStream2);
                    AmbulanceChooseStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            AmbulanceChooseStatus.setSelection(i);
                            ambulanceStatusItem = adapterView.getItemAtPosition(i).toString();
                            ambulanceStatusString = ambulanceStatusItem;
                            status.setText(ambulanceStatusString);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Matcher matcher1 = pattern1.matcher(email.getText().toString());

                            if(TextUtils.isEmpty(inputNumberPlate.getText().toString()) ||TextUtils.isEmpty(persons.getText().toString()) ||
                            TextUtils.isEmpty(email.getText().toString()) ||TextUtils.isEmpty(phoneNumber.getText().toString())){
                                showToast("Please enter details in all the fields");
                            }else if(status.getText().toString().equals("Choose Status")){
                                showToast("Status Invalid, Please select the correct status");
                            }else if (!matcher1.matches()) {
                                showToast("Invalid Email, please provide valid email");
                            }else if(phoneNumber.getText().toString().length() > 10 || phoneNumber.getText().toString().length() < 10){
                                showToast("Please provide valid 10 digit phone number");
                            }
                            else{
                                final Ambulance ambulance = new Ambulance();
                                ambulance.setNumberPlate(inputNumberPlate.getText().toString());
                                ambulance.setPersons(persons.getText().toString());
                                ambulance.setEmail(email.getText().toString());
                                ambulance.setPhoneNumber(phoneNumber.getText().toString());
                                ambulance.setStatus(status.getText().toString());

                                @SuppressLint("StaticFieldLeak")
                                class SaveAmbulanceTask extends AsyncTask<Void, Void, Void> {

                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        AmbulanceDatabase.getAmbulanceDatabase(getApplicationContext()).ambulanceDao().insertAmbulance(ambulance);
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        super.onPostExecute(aVoid);
                                        displayAgain();
                                        dialogAddAmbulance.cancel();
                                    }
                                }

                                new SaveAmbulanceTask().execute();
                            }
                        }
                    });

                    view.findViewById(R.id.textCancel).setOnClickListener(v1 -> {
                        displayAgain();
                        dialogAddAmbulance.dismiss();

                    });
                }
                dialogAddAmbulance.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                dialogAddAmbulance.show();
            }
        });
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ambulanceAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ambulanceList.size() != 0) {
                    ambulanceAdapter.searchAmbulance(s.toString());
                }
            }
        });
    }

    private void displayAgain() {
        ambulanceList = new ArrayList<>();
        ambulanceAdapter = new AmbulanceAdapter(ambulanceList, Ambulances.this);
        ambulanceRecyclerView.setHasFixedSize(true);
        ambulanceRecyclerView.setAdapter(ambulanceAdapter);
        getAmbulance();
    }

    @Override
    public void onAmbulanceClicked(Ambulance note, int position) {
        ambulance = note;
        BottomSheet();
    }
    void showToast(String message) {
        Toast toast = new Toast(Ambulances.this);

        @SuppressLint("InflateParams") View view = LayoutInflater.from(Ambulances.this)
                .inflate(R.layout.toast_red, null);

        TextView tvMessage = view.findViewById(R.id.tvMessage);
        tvMessage.setText(message);

        toast.setView(view);
        toast.show();
    }
    private void getAmbulance() {
        @SuppressLint("StaticFieldLeak")
        class GetAmbulanceTask extends AsyncTask<Void, Void, List<Ambulance>> {

            @Override
            protected List<Ambulance> doInBackground(Void... voids) {
                return AmbulanceDatabase.getAmbulanceDatabase(getApplicationContext())
                        .ambulanceDao().getAllAmbulance();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(List<Ambulance> countries) {
                super.onPostExecute(countries);
                ambulanceList.addAll(countries);
                ambulanceAdapter.notifyDataSetChanged();
            }
        }

        new GetAmbulanceTask().execute();
    }
    @SuppressLint("SetTextI18n")
    private void BottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(Ambulances.this,R.style.BottomSheetTheme);
        bottomSheetDialog.setCanceledOnTouchOutside(false);

        final View sheetView = LayoutInflater.from(Ambulances.this).inflate(R.layout.ambulance_bottomsheet, findViewById(R.id.layoutMoreOptions));

        final CardView Close,Delete,Edit,Save,Discard;
        final GridLayout editingGrid,viewingGrid;
        final TextView numberPlate,persons,email,phone,status;
        final LinearLayout statusLayout;
        String[] AmbulanceStatus;
        Spinner AmbulanceStatusChoose;

        numberPlate = sheetView.findViewById(R.id.numberPlate);
        persons = sheetView.findViewById(R.id.persons);
        email = sheetView.findViewById(R.id.floor);
        phone = sheetView.findViewById(R.id.phone);
        status = sheetView.findViewById(R.id.status);
        Close = sheetView.findViewById(R.id.close);
        Delete = sheetView.findViewById(R.id.delete);
        Edit = sheetView.findViewById(R.id.edit);
        Save = sheetView.findViewById(R.id.save);
        Discard = sheetView.findViewById(R.id.discard);
        editingGrid = sheetView.findViewById(R.id.editingGrid);
        viewingGrid = sheetView.findViewById(R.id.viewingGrid);
        statusLayout = sheetView.findViewById(R.id.statusLayout);
        AmbulanceStatusChoose = sheetView.findViewById(R.id.statusChoose);

        numberPlate.setText(ambulance.getNumberPlate());
        persons.setText(ambulance.getPersons());
        email.setText(ambulance.getEmail());
        phone.setText(ambulance.getPhoneNumber());
        status.setText(ambulance.getStatus());

        AmbulanceStatus = getResources().getStringArray(R.array.AmbulanceStatus);

        ArrayAdapter<String> adapterStream2 = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, AmbulanceStatus);
        adapterStream2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AmbulanceStatusChoose.setAdapter(adapterStream2);
        AmbulanceStatusChoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AmbulanceStatusChoose.setSelection(i);
                ambulanceStatusItem = adapterView.getItemAtPosition(i).toString();
                ambulanceStatusString = ambulanceStatusItem;
                status.setText(ambulanceStatusString);

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
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Ambulance ambulance = new Ambulance();
                ambulance.setId(Ambulances.this.ambulance.getId());

                @SuppressLint("StaticFieldLeak")
                class SaveAmbulanceTask extends AsyncTask<Void, Void, Void> {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        AmbulanceDatabase.getAmbulanceDatabase(getApplicationContext()).ambulanceDao().deleteAmbulance(ambulance);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        displayAgain();
                        bottomSheetDialog.cancel();

                    }
                }

                new SaveAmbulanceTask().execute();
            }
        });
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matcher matcher1 = pattern1.matcher(email.getText().toString());
                if(TextUtils.isEmpty(numberPlate.getText().toString()) || TextUtils.isEmpty(persons.getText().toString()) ||
                        TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(phone.getText().toString()) ||
                        TextUtils.isEmpty(status.getText().toString())) {
                    showToast("You need to add data in all fields in order to add the ambulance.");
                }else if(status.getText().toString().equals("Choose Status")){
                    showToast("Status Invalid, Please select the correct status");
                }else if(status.getText().toString().equals("Choose Status")){
                    showToast("Status Invalid, Please select the correct status");
                }else if (!matcher1.matches()) {
                    showToast("Invalid Email, please provide valid email");
                }else if(phone.getText().toString().length() > 10 || phone.getText().toString().length() < 10){
                    showToast("Please provide valid 10 digit phone number");
                }else{
                    final Ambulance ambulance = new Ambulance();
                    ambulance.setNumberPlate(numberPlate.getText().toString());
                    ambulance.setPersons(persons.getText().toString());
                    ambulance.setEmail(email.getText().toString());
                    ambulance.setPhoneNumber(phone.getText().toString());
                    ambulance.setStatus(status.getText().toString());
                    ambulance.setId(Ambulances.this.ambulance.getId());

                    @SuppressLint("StaticFieldLeak")
                    class SaveAmbulanceTask extends AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            AmbulanceDatabase.getAmbulanceDatabase(getApplicationContext()).ambulanceDao().insertAmbulance(ambulance);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            displayAgain();
                            bottomSheetDialog.cancel();

                        }
                    }

                    new SaveAmbulanceTask().execute();
                }
            }
        });
        Discard.setOnClickListener(v -> bottomSheetDialog.cancel());


        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }
    private void Info() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Ambulances.this,R.style.AlertDialog);
        builder.setTitle("Note");
        builder.setCancelable(false);

        final TextView groupNameField = new TextView(Ambulances.this);
        groupNameField.setText("1) Click on the add button and enter the required details. \n\n2) While setting the status, click on the red field and then select the status type. \n\n3) After adding the ambulance, it will appear in the list. You can click on it to delete, edit status. \n\n4) You can search the ambulance by the vehicle's number plate using the search field");
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