package com.arnold.patients.adapters;

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

import com.arnold.patients.entities.Patient;
import com.arnold.patients.listeners.PatientListeners;
import com.arnold.patients.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.PatientsViewHolder> {

    private final Context context;
    private List<Patient> doctors;
    private final PatientListeners ambulanceListeners;
    protected Timer timer;
    private final List<Patient> notesSource;

    public PatientsAdapter(Context context, List<Patient> doctors, PatientListeners doctorsListeners) {
        this.context = context;
        this.doctors = doctors;
        this.ambulanceListeners = doctorsListeners;
        notesSource = doctors;

    }

    @NonNull
    @Override
    public PatientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PatientsViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_patient, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull PatientsViewHolder holder, @SuppressLint("RecyclerView")  final int position) {
        holder.setNote(doctors.get(position));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ambulanceListeners.onPatientClicked(doctors.get(position), position);


            }
        });
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class PatientsViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView patientName, doctorAppointed;
        RoundedImageView roundedImageView;
        ImageView more;
        public PatientsViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.layoutDoctors);
            patientName = itemView.findViewById(R.id.textPatientsName);
            doctorAppointed = itemView.findViewById(R.id.textDoctorAppointed);
            roundedImageView = itemView.findViewById(R.id.imageProfile);
        }

        @SuppressLint("SetTextI18n")

        void setNote(Patient doctors) {
            patientName.setText("Patient Name: "+doctors.getPatientName());
            doctorAppointed.setText("Appointed: "+doctors.getAppointedDoctor());
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
                    ArrayList<Patient> temp = new ArrayList<>();
                    for (Patient note : notesSource) {
                        if (note.getPatientName().toLowerCase().contains(searchKeyword.toLowerCase())){
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


