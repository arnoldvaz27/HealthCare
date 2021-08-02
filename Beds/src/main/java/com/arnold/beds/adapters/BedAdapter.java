package com.arnold.beds.adapters;

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

import com.arnold.beds.R;
import com.arnold.beds.entities.Bed;
import com.arnold.beds.listeners.BedListeners;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BedAdapter extends RecyclerView.Adapter<BedAdapter.BedViewHolder> {

    private List<Bed> beds;
    private final BedListeners bedListeners;
    protected Timer timer;
    private final List<Bed> bedSource;

    public BedAdapter(List<Bed> beds, BedListeners bedListeners) {
        this.beds = beds;
        this.bedListeners = bedListeners;
        bedSource = beds;

    }

    @NonNull
    @Override
    public BedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BedViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_bed, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull BedViewHolder holder,  @SuppressLint("RecyclerView") final int position) {
        holder.setBed(beds.get(position));
        holder.linearLayout.setOnClickListener(v -> bedListeners.onBedClicked(beds.get(position), position));
    }


    @Override
    public int getItemCount() {
        return beds.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class BedViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView bedNumber, floorNumber;
        RoundedImageView roundedImageView;
        public BedViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.layoutDoctors);
            bedNumber = itemView.findViewById(R.id.textBedNumber);
            floorNumber = itemView.findViewById(R.id.textFloor);
            roundedImageView = itemView.findViewById(R.id.imageProfile);
        }

        @SuppressLint("SetTextI18n")

        void setBed(Bed bed) {
            bedNumber.setText("Bed Number: "+bed.getBedNumber());
            floorNumber.setText("Floor: "+bed.getFloor());
        }
    }
    public void searchBed(final String searchKeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty()) {
                    beds = bedSource;
                } else {
                    ArrayList<Bed> temp = new ArrayList<>();
                    for (Bed bed : bedSource) {
                        if (bed.getBedNumber().toLowerCase().contains(searchKeyword.toLowerCase())){
                            temp.add(bed);
                        }
                    }
                    beds = temp;
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


