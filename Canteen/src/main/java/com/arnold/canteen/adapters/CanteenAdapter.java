package com.arnold.canteen.adapters;

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

import com.arnold.canteen.R;
import com.arnold.canteen.entities.Canteens;
import com.arnold.canteen.listeners.CanteenListeners;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CanteenAdapter extends RecyclerView.Adapter<CanteenAdapter.CanteenViewHolder> {

    private List<Canteens> canteens;
    private final CanteenListeners canteenListeners;
    protected Timer timer;
    private final List<Canteens> canteenSource;

    public CanteenAdapter(List<Canteens> canteens, CanteenListeners canteenListeners) {
        this.canteens = canteens;
        this.canteenListeners = canteenListeners;
        canteenSource = canteens;

    }

    @NonNull
    @Override
    public CanteenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CanteenViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_canteen, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CanteenViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.setFood(canteens.get(position));
        holder.linearLayout.setOnClickListener(v -> canteenListeners.onCanteenClicked(canteens.get(position), position));

    }

    @Override
    public int getItemCount() {
        return canteens.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class CanteenViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView food, price;
        RoundedImageView roundedImageView;
        public CanteenViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.layoutDoctors);
            food = itemView.findViewById(R.id.textFood);
            price = itemView.findViewById(R.id.textPrice);
            roundedImageView = itemView.findViewById(R.id.imageProfile);
        }

        @SuppressLint("SetTextI18n")

        void setFood(Canteens canteens) {
            food.setText("Food Name: "+canteens.getFoodName());
            price.setText("Price: Rs. "+canteens.getPrice()+" /-");

        }
    }
    public void searchNotes(final String searchKeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty()) {
                    canteens = canteenSource;
                } else {
                    ArrayList<Canteens> temp = new ArrayList<>();
                    for (Canteens canteens : canteenSource) {
                        if (canteens.getFoodName().toLowerCase().contains(searchKeyword.toLowerCase())){
                            temp.add(canteens);
                        }
                    }
                    canteens = temp;
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


