package com.arnold.medicines.adapters;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arnold.medicines.R;
import com.arnold.medicines.entities.Medicine;
import com.arnold.medicines.listeners.MedicinesListeners;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MedicinesAdapter extends RecyclerView.Adapter<MedicinesAdapter.MedicineViewHolder> {

    private List<Medicine> medicines;
    private final MedicinesListeners medicinesListeners;
    protected Timer timer;
    private final List<Medicine> medicineSource;

    public MedicinesAdapter(List<Medicine> medicines, MedicinesListeners medicinesListeners) {
        this.medicines = medicines;
        this.medicinesListeners = medicinesListeners;
        medicineSource = medicines;

    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MedicineViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_medicine, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, @SuppressLint("RecyclerView")  final int position) {
        holder.setMedicine(medicines.get(position));
        holder.linearLayout.setOnClickListener(v -> medicinesListeners.onMedicinesClicked(medicines.get(position), position));

    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView name, stock;
        RoundedImageView roundedImageView;
        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.layoutMedicine);
            name = itemView.findViewById(R.id.textName);
            stock = itemView.findViewById(R.id.textStock);
            roundedImageView = itemView.findViewById(R.id.imageProfile);
        }

        @SuppressLint("SetTextI18n")
        void setMedicine(Medicine medicine) {
            name.setText("Name: "+medicine.getName());
            stock.setText("Stock: "+medicine.getStock());

        }
    }
    public void searchMedicine(final String searchKeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty()) {
                    medicines = medicineSource;
                } else {
                    ArrayList<Medicine> temp = new ArrayList<>();
                    for (Medicine medicine : medicineSource) {
                        if (medicine.getName().toLowerCase().contains(searchKeyword.toLowerCase())){
                            temp.add(medicine);
                        }
                    }
                    medicines = temp;
                }
                new Handler(Looper.getMainLooper()).post(() -> notifyDataSetChanged());
            }
        }, 500);
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }
}


