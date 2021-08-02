package com.arnold.ambulances.adapters;

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

import com.arnold.ambulances.entities.Ambulance;
import com.arnold.ambulances.listeners.AmbulanceListeners;
import com.arnold.ambulances.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AmbulanceAdapter extends RecyclerView.Adapter<AmbulanceAdapter.AmbulanceViewHolder> {

    private List<Ambulance> ambulances;
    private final AmbulanceListeners ambulanceListeners;
    protected Timer timer;
    private final List<Ambulance> ambulanceSource;

    public AmbulanceAdapter(List<Ambulance> ambulances, AmbulanceListeners ambulanceListeners) {
        this.ambulances = ambulances;
        this.ambulanceListeners = ambulanceListeners;
        ambulanceSource = ambulances;

    }

    @NonNull
    @Override
    public AmbulanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AmbulanceViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_ambulance, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AmbulanceViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.setAmbulance(ambulances.get(position));
        holder.linearLayout.setOnClickListener(v -> ambulanceListeners.onAmbulanceClicked(ambulances.get(position), position));
    }

    @Override
    public int getItemCount() {
        return ambulances.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class AmbulanceViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView numberPlate,phoneNumber;
        RoundedImageView roundedImageView;
        public AmbulanceViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.layoutAmbulance);
            numberPlate = itemView.findViewById(R.id.textNumberPlate);
            phoneNumber = itemView.findViewById(R.id.textPhoneNumber);
            roundedImageView = itemView.findViewById(R.id.imageProfile);
        }

        @SuppressLint("SetTextI18n")
        void setAmbulance(Ambulance ambulance) {
            numberPlate.setText("No. Plate: "+ambulance.getNumberPlate());
            phoneNumber.setText("Mobile Number: "+ambulance.getPhoneNumber());

        }
    }
    public void searchAmbulance(final String searchKeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty()) {
                    ambulances = ambulanceSource;
                } else {
                    ArrayList<Ambulance> temp = new ArrayList<>();
                    for (Ambulance ambulance : ambulanceSource) {
                        if (ambulance.getNumberPlate().toLowerCase().contains(searchKeyword.toLowerCase())){
                            temp.add(ambulance);
                        }
                    }
                    ambulances = temp;
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


