package com.example.finalproject1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class AdapterQueueList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private AppCompatActivity activity;
    private ArrayList<Queue> queues;
    private CallBackQueueAdapter callBackQueueAdapter;

    public AdapterQueueList(AppCompatActivity activity, ArrayList<Queue> queues){
        Collections.sort(queues);
        this.queues = queues;
        this.activity = activity;

    }

    public void setCallBackQueueAdapter(CallBackQueueAdapter callBackQueueAdapter){
        this.callBackQueueAdapter = callBackQueueAdapter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.queue_object, viewGroup, false);
        return new QueueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        QueueViewHolder queueViewHolder = (QueueViewHolder) holder;
        Queue queue = getItem(position);
        Date d = queue.covertStringToDate();
        String time = "";
        if(d.getHours() < 10) {
            time = "0" + d.getHours();
        }else{
            time = d.getHours() + "";
        }
        time += ":";
        if(d.getMinutes() < 10) {
            time += "0" + d.getMinutes();
        }else{
            time += d.getMinutes() + "";
        }
        queueViewHolder.queue_TXT_time.setText(time);
        queueViewHolder.queue_TXT_name.setText(queue.getName());
        queueViewHolder.queue_TXT_properties.setText(queue.getProperties());

        queueViewHolder.queue_IMG_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        queueViewHolder.queue_IMG_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBackQueueAdapter.removeQueue(queue);
            }
        });

        queueViewHolder.queue_IMG_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBackQueueAdapter.editQueue(queue);
            }
        });
    }

    @Override
    public int getItemCount() {
        return queues.size();
    }

    private Queue getItem(int position) {
        return queues.get(position);
    }

    public class QueueViewHolder extends RecyclerView.ViewHolder{

        public TextView queue_TXT_time;
        public TextView queue_TXT_name;
        public TextView queue_TXT_properties;
        public AppCompatImageView queue_IMG_edit;
        public AppCompatImageView queue_IMG_remove;
        public QueueViewHolder(@NonNull View itemView) {
            super(itemView);
            this.queue_TXT_name = itemView.findViewById(R.id.queue_TXT_name);
            this.queue_TXT_time = itemView.findViewById(R.id.queue_TXT_time);
            this.queue_IMG_edit = itemView.findViewById(R.id.queue_IMG_edit);
            this.queue_IMG_remove = itemView.findViewById(R.id.queue_IMG_remove);
            this.queue_TXT_properties = itemView.findViewById(R.id.queue_TXT_properties);

        }
    }
}
