package com.arnold.services.adapters;

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

import com.arnold.services.R;
import com.arnold.services.entities.Service;
import com.arnold.services.listeners.ServiceListeners;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private List<Service> services;
    private final ServiceListeners serviceListeners;
    protected Timer timer;
    private final List<Service> serviceSource;

    public ServiceAdapter(List<Service> services, ServiceListeners serviceListeners) {
        this.services = services;
        this.serviceListeners = serviceListeners;
        serviceSource = services;

    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_service, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, @SuppressLint("RecyclerView")  final int position) {
        holder.setService(services.get(position));
        holder.linearLayout.setOnClickListener(v -> serviceListeners.onServiceClicked(services.get(position), position));
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView serviceName, startDate;
        RoundedImageView roundedImageView;
        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.layoutService);
            serviceName = itemView.findViewById(R.id.textServiceName);
            startDate = itemView.findViewById(R.id.textStartDate);
            roundedImageView = itemView.findViewById(R.id.imageProfile);
        }

        @SuppressLint("SetTextI18n")

        void setService(Service service) {
            serviceName.setText("Name: "+service.getServiceName());
            startDate.setText("Start Date: "+service.getStartDate());

        }
    }
    public void searchService(final String searchKeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty()) {
                    services = serviceSource;
                } else {
                    ArrayList<Service> temp = new ArrayList<>();
                    for (Service service : serviceSource) {
                        if (service.getServiceName().toLowerCase().contains(searchKeyword.toLowerCase())){
                            temp.add(service);
                        }
                    }
                    services = temp;
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


