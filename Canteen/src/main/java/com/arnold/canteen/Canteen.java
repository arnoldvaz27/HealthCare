package com.arnold.canteen;

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
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arnold.canteen.adapters.CanteenAdapter;
import com.arnold.canteen.database.CanteenDatabase;
import com.arnold.canteen.entities.Canteens;
import com.arnold.canteen.listeners.CanteenListeners;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"deprecation"})
public class Canteen extends AppCompatActivity implements CanteenListeners {
    private RecyclerView RecyclerView;
    private List<Canteens> canteensList;
    private CanteenAdapter canteenAdapter;
    private AlertDialog dialogAddFood;
    private Canteens canteens;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.pink));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        setContentView(R.layout.canteen);
        ImageView addFood = findViewById(R.id.addFood);
        RecyclerView = findViewById(R.id.RecyclerView);
        EditText inputSearch = findViewById(R.id.inputSearch);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Canteen.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        RecyclerView.setLayoutManager(linearLayoutManager);
        canteensList = new ArrayList<>();
        canteenAdapter = new CanteenAdapter(canteensList, Canteen.this);
        RecyclerView.setHasFixedSize(true);
        RecyclerView.setAdapter(canteenAdapter);
        getFood();
        findViewById(R.id.info).setOnClickListener(v -> Info());
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogAddFood == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Canteen.this);
                    View view = LayoutInflater.from(Canteen.this).inflate(
                            R.layout.layout_add_canteen, findViewById(R.id.layoutAddFoodContainer)
                    );
                    builder.setView(view);

                    dialogAddFood = builder.create();
                    if (dialogAddFood.getWindow() != null) {
                        dialogAddFood.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    }

                    final EditText foodName,price;
                    foodName = view.findViewById(R.id.name);
                    price = view.findViewById(R.id.price);

                    foodName.setSelection(foodName.getText().length());
                    foodName.requestFocus();
                    foodName.getShowSoftInputOnFocus();


                    view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(TextUtils.isEmpty(foodName.getText().toString()) ||TextUtils.isEmpty(price.getText().toString())
                            ){
                                showToast("Please enter details in all the fields");
                            }
                            else{
                                final Canteens canteens = new Canteens();
                                canteens.setFoodName(foodName.getText().toString());
                                canteens.setPrice(price.getText().toString());
                                @SuppressLint("StaticFieldLeak")
                                class saveFoodTask extends AsyncTask<Void, Void, Void> {

                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        CanteenDatabase.getCanteenDatabase(getApplicationContext()).canteenDao().insertCanteens(canteens);
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        super.onPostExecute(aVoid);
                                        displayAgain();
                                        dialogAddFood.cancel();
                                    }
                                }

                                new saveFoodTask().execute();
                                foodName.setText("");
                                price.setText("");
                            }
                        }
                    });

                    view.findViewById(R.id.textCancel).setOnClickListener(v1 -> {
                        displayAgain();
                        dialogAddFood.dismiss();

                    });
                }
                dialogAddFood.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                dialogAddFood.show();
            }
        });
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                canteenAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (canteensList.size() != 0) {
                    canteenAdapter.searchNotes(s.toString());
                }
            }
        });
    }

    @Override
    public void onCanteenClicked(Canteens note, int position) {
        canteens = note;
        BottomSheet();
    }
    private void displayAgain() {
        canteensList = new ArrayList<>();
        canteenAdapter = new CanteenAdapter(canteensList, Canteen.this);
        RecyclerView.setHasFixedSize(true);
        RecyclerView.setAdapter(canteenAdapter);
        getFood();
    }
    void showToast(String message) {
        Toast toast = new Toast(Canteen.this);

        @SuppressLint("InflateParams") View view = LayoutInflater.from(Canteen.this)
                .inflate(com.arnold.doctors.R.layout.toast_red, null);

        TextView tvMessage = view.findViewById(com.arnold.doctors.R.id.tvMessage);
        tvMessage.setText(message);

        toast.setView(view);
        toast.show();
    }
    private void getFood() {
        @SuppressLint("StaticFieldLeak")
        class getFoodTask extends AsyncTask<Void, Void, List<Canteens>> {

            @Override
            protected List<Canteens> doInBackground(Void... voids) {
                return CanteenDatabase.getCanteenDatabase(getApplicationContext())
                        .canteenDao().getAllFood();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(List<Canteens> canteens) {
                super.onPostExecute(canteens);
                canteensList.addAll(canteens);
                canteenAdapter.notifyDataSetChanged();
            }
        }

        new getFoodTask().execute();
    }
    @SuppressLint("SetTextI18n")
    private void BottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(Canteen.this,R.style.BottomSheetTheme);
        bottomSheetDialog.setCanceledOnTouchOutside(false);

        final View sheetView = LayoutInflater.from(Canteen.this).inflate(R.layout.canteen_bottomsheet, findViewById(R.id.layoutMoreOptions));

        final CardView Close,Delete,Edit,Save,Discard;
        final GridLayout editingGrid,viewingGrid;
        final TextView name;
        final EditText price;
        name = sheetView.findViewById(R.id.name);
        price = sheetView.findViewById(R.id.price);
        Close = sheetView.findViewById(R.id.close);
        Delete = sheetView.findViewById(R.id.delete);
        Edit = sheetView.findViewById(R.id.edit);
        Save = sheetView.findViewById(R.id.save);
        Discard = sheetView.findViewById(R.id.discard);
        editingGrid = sheetView.findViewById(R.id.editingGrid);
        viewingGrid = sheetView.findViewById(R.id.viewingGrid);

        name.setText("Food Name: "+ canteens.getFoodName());
        price.setText("Price: Rs. "+ canteens.getPrice()+" /-");

        Edit.setOnClickListener(v -> {
            name.setText(canteens.getFoodName());
            price.setText(canteens.getPrice());
            viewingGrid.setVisibility(View.GONE);
            editingGrid.setVisibility(View.VISIBLE);
            price.setEnabled(true);
            price.setSelection(price.getText().length());
            price.requestFocus();
            price.getShowSoftInputOnFocus();
        });
        Close.setOnClickListener(v -> bottomSheetDialog.cancel());
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Canteens doctors = new Canteens();
                doctors.setId(canteens.getId());

                @SuppressLint("StaticFieldLeak")
                class saveFoodTask extends AsyncTask<Void, Void, Void> {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        CanteenDatabase.getCanteenDatabase(getApplicationContext()).canteenDao().deleteCanteens(doctors);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        displayAgain();
                        bottomSheetDialog.cancel();

                    }
                }

                new saveFoodTask().execute();
            }
        });
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(price.getText().toString())) {
                    showToast("You need to add data in all fields in order to add the doctor.");
                }else {
                    final Canteens canteens = new Canteens();
                    canteens.setFoodName(name.getText().toString());
                    canteens.setPrice(price.getText().toString());
                    canteens.setId(Canteen.this.canteens.getId());
                    @SuppressLint("StaticFieldLeak")
                    class saveFoodTask extends AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            CanteenDatabase.getCanteenDatabase(getApplicationContext()).canteenDao().insertCanteens(canteens);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            displayAgain();
                            bottomSheetDialog.cancel();

                        }
                    }

                    new saveFoodTask().execute();

                }


            }

        });
        Discard.setOnClickListener(v -> bottomSheetDialog.cancel());



        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    private void Info() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Canteen.this,R.style.AlertDialog);
        builder.setTitle("Note");
        builder.setCancelable(false);

        final TextView groupNameField = new TextView(Canteen.this);
        groupNameField.setText("1) Click on the add button and enter the required details. \n\n2) After adding the food, it will appear in the list. You can click on it to delete, edit food price. \n\n3) You can search the food by the name using the search field");
        groupNameField.setPadding(20,30,20,20);
        groupNameField.setTextColor(Color.BLACK);

        groupNameField.setBackgroundColor(Color.WHITE);
        builder.setView(groupNameField);

        builder.setPositiveButton("Done", (dialogInterface, i) -> dialogInterface.cancel());

        builder.show();
    }
}