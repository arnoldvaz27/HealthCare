package com.arnold.doctors.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.arnold.doctors.entities.Doctors;
import com.arnold.doctors.listeners.DoctorsListeners;
import com.arnold.doctors.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.DoctorsViewHolder> {

    private final Context context;
    private List<Doctors> doctors;
    private final DoctorsListeners doctorsListeners;
    protected Timer timer;
    private final List<Doctors> notesSource;

    public DoctorsAdapter(Context context, List<Doctors> doctors, DoctorsListeners doctorsListeners) {
        this.context = context;
        this.doctors = doctors;
        this.doctorsListeners = doctorsListeners;
        notesSource = doctors;
    }

    @NonNull
    @Override
    public DoctorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DoctorsViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_doctors, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorsViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.setNote(doctors.get(position));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctorsListeners.onDoctorsClicked(doctors.get(position), position);


            }
        });
        if(holder.gender.equals("Male")){
            holder.roundedImageView.setImageResource(R.drawable.male);
        }else if(holder.gender.equals("Female")){
            holder.roundedImageView.setImageResource(R.drawable.female);
        }
    }

    private void Delete(Doctors doctors) {
    }

    private void Edit(Doctors doctors) {
    }

    private void Open(Doctors doctors) {
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class DoctorsViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView name,designation;
        RoundedImageView roundedImageView;
        ImageView more;
        String gender;
        public DoctorsViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.layoutDoctors);
            name = itemView.findViewById(R.id.textName);
            designation = itemView.findViewById(R.id.textDesignation);
            roundedImageView = itemView.findViewById(R.id.imageProfile);
        }

        void setNote(Doctors doctors) {
            name.setText(doctors.getName());
            designation.setText(doctors.getDesignation());
            gender = doctors.getGender();

        }
    }
    public void searchNotes(final String searchKeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty()) {
                    doctors = notesSource;
                } else {
                    ArrayList<Doctors> temp = new ArrayList<>();
                    for (Doctors note : notesSource) {
                        if (note.getName().toLowerCase().contains(searchKeyword.toLowerCase())){
                            temp.add(note);
                        }
                    }
                    doctors = temp;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }, 500);
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }
}


