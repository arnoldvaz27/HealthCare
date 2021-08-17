package com.arnold.beds;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arnold.beds.adapters.BedAdapter;
import com.arnold.beds.database.BedDatabase;
import com.arnold.beds.entities.Bed;
import com.arnold.beds.listeners.BedListeners;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"deprecation"})
public class Beds extends AppCompatActivity implements BedListeners {
    private RecyclerView RecyclerView;
    private List<Bed> bedList;
    private BedAdapter bedAdapter;
    private AlertDialog dialogAddBed;
    private BottomSheetDialog bottomSheetDialog;
    private String BedItem;
    String BedString;
    private Bed bed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.dark_red));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        setContentView(R.layout.beds);
        ImageView addBed = findViewById(R.id.addBed);
        RecyclerView = findViewById(R.id.RecyclerView);
        EditText inputSearch = findViewById(R.id.inputSearch);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Beds.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        RecyclerView.setLayoutManager(linearLayoutManager);
        bedList = new ArrayList<>();
        bedAdapter = new BedAdapter(bedList, Beds.this);
        RecyclerView.setHasFixedSize(true);
        RecyclerView.setAdapter(bedAdapter);
        getBed();
        findViewById(R.id.info).setOnClickListener(v -> Info());
        addBed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogAddBed == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Beds.this);
                    View view = LayoutInflater.from(Beds.this).inflate(
                            R.layout.layout_add_bed, findViewById(R.id.layoutAddBedContainer)
                    );
                    builder.setView(view);

                    dialogAddBed = builder.create();
                    if (dialogAddBed.getWindow() != null) {
                        dialogAddBed.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    }

                    final EditText bedNumber,roomNumber,floor;
                    final TextView status;
                    String[] BedStrings;
                    Spinner BedChoose;
                    bedNumber = view.findViewById(R.id.name);
                    roomNumber = view.findViewById(R.id.roomNumber);
                    floor = view.findViewById(R.id.floor);
                    status = view.findViewById(R.id.status);
                    BedChoose = view.findViewById(R.id.statusChoose);
                    BedStrings = getResources().getStringArray(R.array.BedStatus);
                    bedNumber.setSelection(bedNumber.getText().length());
                    bedNumber.requestFocus();
                    bedNumber.getShowSoftInputOnFocus();
                    ArrayAdapter<String> adapterStream2 = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, BedStrings);
                    adapterStream2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    BedChoose.setAdapter(adapterStream2);
                    BedChoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            BedChoose.setSelection(i);
                            BedItem = adapterView.getItemAtPosition(i).toString();
                            BedString = BedItem;
                            status.setText(BedString);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(TextUtils.isEmpty(bedNumber.getText().toString()) ||TextUtils.isEmpty(roomNumber.getText().toString()) ||
                            TextUtils.isEmpty(floor.getText().toString())){
                                showToast("Please enter details in all the fields");
                            }
                            else if(status.getText().toString().equals("Choose Status")){
                                showToast("Status Invalid, Please select the correct status");
                            }
                            else{
                                final Bed bed = new Bed();
                                bed.setBedNumber(bedNumber.getText().toString());
                                bed.setRoomNumber(roomNumber.getText().toString());
                                bed.setFloor(floor.getText().toString());
                                bed.setStatus(status.getText().toString());

                                @SuppressLint("StaticFieldLeak")
                                class saveBedTask extends AsyncTask<Void, Void, Void> {

                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        BedDatabase.getBedDatabase(getApplicationContext()).bedDao().insertBed(bed);
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        super.onPostExecute(aVoid);
                                        displayAgain();
                                        dialogAddBed.cancel();
                                    }
                                }

                                new saveBedTask().execute();
                                bedNumber.setText("");
                                roomNumber.setText("");
                                floor.setText("");
                                status.setText("");
                            }
                        }
                    });

                    view.findViewById(R.id.textCancel).setOnClickListener(v1 -> {
                        displayAgain();
                        dialogAddBed.dismiss();

                    });
                }
                dialogAddBed.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                dialogAddBed.show();
            }
        });
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bedAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (bedList.size() != 0) {
                    bedAdapter.searchBed(s.toString());
                }
            }
        });
    }

    @Override
    public void onBedClicked(Bed note, int position) {
        bed = note;
        BottomSheet();
    }
    private void displayAgain() {
        bedList = new ArrayList<>();
        bedAdapter = new BedAdapter(bedList, Beds.this);
        RecyclerView.setHasFixedSize(true);
        RecyclerView.setAdapter(bedAdapter);
        getBed();
    }
    void showToast(String message) {
        Toast toast = new Toast(Beds.this);

        @SuppressLint("InflateParams") View view = LayoutInflater.from(Beds.this)
                .inflate(R.layout.toast_red, null);

        TextView tvMessage = view.findViewById(R.id.tvMessage);
        tvMessage.setText(message);

        toast.setView(view);
        toast.show();
    }
    private void getBed() {
        @SuppressLint("StaticFieldLeak")
        class getBedTask extends AsyncTask<Void, Void, List<Bed>> {

            @Override
            protected List<Bed> doInBackground(Void... voids) {
                return BedDatabase.getBedDatabase(getApplicationContext())
                        .bedDao().getAllBed();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(List<Bed> countries) {
                super.onPostExecute(countries);
                bedList.addAll(countries);
                bedAdapter.notifyDataSetChanged();
            }
        }

        new getBedTask().execute();
    }
    @SuppressLint("SetTextI18n")
    private void BottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(Beds.this,R.style.BottomSheetTheme);
        bottomSheetDialog.setCanceledOnTouchOutside(false);

        final View sheetView = LayoutInflater.from(Beds.this).inflate(R.layout.bed_bottomsheet, findViewById(R.id.layoutMoreOptions));

        final CardView Edit,Discard,Close,Save,Delete;
        final GridLayout editingGrid,viewingGrid;
        final TextView bedNumber,roomNumber,floor,status;
        final LinearLayout statusLayout;
        String[] BedStrings;
        Spinner BedChoose;

        Edit = sheetView.findViewById(R.id.edit);
        Discard = sheetView.findViewById(R.id.discard);
        Close = sheetView.findViewById(R.id.close);
        Save = sheetView.findViewById(R.id.save);
        Delete = sheetView.findViewById(R.id.delete);
        editingGrid = sheetView.findViewById(R.id.editingGrid);
        viewingGrid = sheetView.findViewById(R.id.viewingGrid);
        bedNumber = sheetView.findViewById(R.id.name);
        roomNumber = sheetView.findViewById(R.id.roomNumber);
        floor = sheetView.findViewById(R.id.floor);
        status = sheetView.findViewById(R.id.status);
        statusLayout = sheetView.findViewById(R.id.statusLayout);
        BedChoose = sheetView.findViewById(R.id.statusChoose);

        bedNumber.setText("Bed Number: "+bed.getBedNumber());
        roomNumber.setText("Room Number: "+bed.getRoomNumber());
        floor.setText("Floor Number: "+bed.getFloor());
        status.setText("Status: "+bed.getStatus());

        BedStrings = getResources().getStringArray(R.array.BedStatus);

        ArrayAdapter<String> adapterStream2 = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, BedStrings);
        adapterStream2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        BedChoose.setAdapter(adapterStream2);
        BedChoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                BedChoose.setSelection(i);
                BedItem = adapterView.getItemAtPosition(i).toString();
                BedString = BedItem;
                status.setText(BedString);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Edit.setOnClickListener(v -> {
            bedNumber.setText(bed.getBedNumber());
            roomNumber.setText(bed.getRoomNumber());
            floor.setText(bed.getFloor());
            status.setText(bed.getStatus());
            viewingGrid.setVisibility(View.GONE);
            editingGrid.setVisibility(View.VISIBLE);
            statusLayout.setVisibility(View.VISIBLE);
        });
        Close.setOnClickListener(v -> bottomSheetDialog.cancel());
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(bedNumber.getText().toString()) || TextUtils.isEmpty(roomNumber.getText().toString()) ||
                        TextUtils.isEmpty(floor.getText().toString()) || TextUtils.isEmpty(status.getText().toString())) {
                    showToast("You need to add data in all fields in order to add Bed.");
                }
                else if(status.getText().toString().equals("Choose Status")){
                    showToast("Status Invalid, Please select the correct status");
                }else{
                    final Bed bed = new Bed();
                    bed.setBedNumber(bedNumber.getText().toString());
                    bed.setRoomNumber(roomNumber.getText().toString());
                    bed.setFloor(floor.getText().toString());
                    bed.setStatus(status.getText().toString());
                    bed.setId(Beds.this.bed.getId());

                    @SuppressLint("StaticFieldLeak")
                    class SaveBedTask extends AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            BedDatabase.getBedDatabase(getApplicationContext()).bedDao().insertBed(bed);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            displayAgain();
                            bottomSheetDialog.cancel();

                        }
                    }

                    new SaveBedTask().execute();
                }
            }
        });
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bed beds = new Bed();
                beds.setId(bed.getId());

                @SuppressLint("StaticFieldLeak")
                class SaveBedTask extends AsyncTask<Void, Void, Void> {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        BedDatabase.getBedDatabase(getApplicationContext()).bedDao().deleteBed(beds);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        displayAgain();
                        bottomSheetDialog.cancel();

                    }
                }

                new SaveBedTask().execute();
            }
        });
        Discard.setOnClickListener(v -> bottomSheetDialog.cancel());

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    private void Info() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Beds.this,R.style.AlertDialog);
        builder.setTitle("Note");
        builder.setCancelable(false);

        final TextView groupNameField = new TextView(Beds.this);
        groupNameField.setText("1) Click on the add button and enter the required details. \n\n2) While setting the status, click on the red field and then select the status type. \n\n3) After adding the bed, it will appear in the list. You can click on it to delete, edit status. \n\n4) You can search the bed by the bed number using the search field");
        groupNameField.setPadding(20,30,20,20);
        groupNameField.setTextColor(Color.BLACK);

        groupNameField.setBackgroundColor(Color.WHITE);
        builder.setView(groupNameField);

        builder.setPositiveButton("Done", (dialogInterface, i) -> dialogInterface.cancel());

        builder.show();
    }
}