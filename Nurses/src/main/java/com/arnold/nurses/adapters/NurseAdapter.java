package com.arnold.nurses.adapters;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arnold.nurses.R;
import com.arnold.nurses.entities.Nurse;
import com.arnold.nurses.listeners.NurseListeners;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NurseAdapter extends RecyclerView.Adapter<NurseAdapter.NurseViewHolder> {

    private List<Nurse> nurses;
    private final NurseListeners nurseListeners;
    protected Timer timer;
    private final List<Nurse> nurseSource;

    public NurseAdapter(List<Nurse> nurses, NurseListeners nurseListeners) {
        this.nurses = nurses;
        this.nurseListeners = nurseListeners;
        nurseSource = nurses;
    }

    @NonNull
    @Override
    public NurseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NurseViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_nurses, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NurseViewHolder holder,  @SuppressLint("RecyclerView") final int position) {
        holder.setNurse(nurses.get(position));
        holder.linearLayout.setOnClickListener(v -> nurseListeners.onNurseClicked(nurses.get(position), position));
        if(holder.gender.equals("Male")){
            holder.roundedImageView.setImageResource(R.drawable.male);
        }else if(holder.gender.equals("Female")){
            holder.roundedImageView.setImageResource(R.drawable.female);
        }

    }


    @Override
    public int getItemCount() {
        return nurses.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NurseViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView name,designation;
        RoundedImageView roundedImageView;
        String gender;
        public NurseViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.layoutNurse);
            name = itemView.findViewById(R.id.textName);
            designation = itemView.findViewById(R.id.textDesignation);
            roundedImageView = itemView.findViewById(R.id.imageProfile);
        }

        void setNurse(Nurse nurse) {
            name.setText(nurse.getName());
            designation.setText(nurse.getDesignation());
            gender = nurse.getGender();

        }
    }
    public void searchNurse(final String searchKeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty()) {
                    nurses = nurseSource;
                } else {
                    ArrayList<Nurse> temp = new ArrayList<>();
                    for (Nurse nurse : nurseSource) {
                        if (nurse.getName().toLowerCase().contains(searchKeyword.toLowerCase())){
                            temp.add(nurse);
                        }
                    }
                    nurses = temp;
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


