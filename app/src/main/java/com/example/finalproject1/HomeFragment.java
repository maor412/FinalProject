package com.example.finalproject1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private FloatingActionButton home_fBTN_addQueue;
    private Database database;
    private AppCompatActivity activity;
    private CalendarView home_CALV_queueCalender;
    private Date selectedDate;
    private ArrayList<Queue> selectedDateQueues;
    private RecyclerView home_RCV_queues;
    private ArrayList<Queue> allQueues;
    private LayoutInflater inflater;

    //dialog add queue fields
    private TextInputLayout dialog_TXT_name;
    private TextInputLayout dialog_TXT_properties;
    private MaterialButton dialog_BTN_addQueue;
    private TimePicker dialog_TP_queueTime;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        this.inflater = inflater;
        findViews(view);
        initViews();
        return view;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public void setRecycleView(){
        AdapterQueueList queueListAdapter = new AdapterQueueList(activity, selectedDateQueues);
        queueListAdapter.setCallBackQueueAdapter(new CallBackQueueAdapter() {
            @Override
            public void removeQueue(Queue q) {
                database.removeQueue(activity, q);
            }

            @Override
            public void editQueue(Queue q) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(R.string.update_queue);
                View addQueueDialogView = inflater.inflate(R.layout.addqueuedialog, null);
                builder.setView(addQueueDialogView);
                AlertDialog dialog = builder.create();
                dialog_TXT_name = addQueueDialogView.findViewById(R.id.dialog_TXT_name);
                dialog_TXT_properties = addQueueDialogView.findViewById(R.id.dialog_TXT_properties);
                dialog_BTN_addQueue = addQueueDialogView.findViewById(R.id.dialog_BTN_addQueue);
                dialog_TP_queueTime = addQueueDialogView.findViewById(R.id.dialog_TP_queueTime);
                dialog_BTN_addQueue.setText(R.string.update_queue_BTN);
                dialog_TXT_name.getEditText().setText(q.getName());
                dialog_TP_queueTime.setHour(q.covertStringToDate().getHours());
                dialog_TP_queueTime.setMinute(q.covertStringToDate().getMinutes());
                dialog_BTN_addQueue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int hour = dialog_TP_queueTime.getCurrentHour();
                        int min = dialog_TP_queueTime.getCurrentMinute();
                        String queueDate = (selectedDate.getYear()+1900) +"-"+selectedDate.getMonth()+"-"+selectedDate.getDate()+" "+hour+":"+min;

                        Queue newQueue = new Queue()
                                .setDate(queueDate)
                                .setId(q.getId())
                                .setName(dialog_TXT_name.getEditText().getText().toString())
                                .setProperties(dialog_TXT_properties.getEditText().getText().toString());

                        database.addQueue(activity, newQueue);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        home_RCV_queues.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        home_RCV_queues.setHasFixedSize(true);
        home_RCV_queues.setItemAnimator(new DefaultItemAnimator());
        home_RCV_queues.setAdapter(queueListAdapter);

    }

    private void initViews() {
        database = new Database();
        database.setCallback_queue(this.callback_queue);
        database.getQueues();
        selectedDate = Calendar.getInstance().getTime();
        allQueues = new ArrayList<>();
        selectedDateQueues = new ArrayList<>();

        setRecycleView();
        home_CALV_queueCalender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                selectedDate.setYear(year - 1900);
                selectedDate.setMonth(month);
                selectedDate.setDate(day);
                selectedDateQueues = filterByDate(allQueues, selectedDate);
                setRecycleView();
            }
        });

        home_fBTN_addQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(R.string.add_new_queue);
                View addQueueDialogView = inflater.inflate(R.layout.addqueuedialog, null);
                builder.setView(addQueueDialogView);
                AlertDialog dialog = builder.create();
                dialog_TXT_name = addQueueDialogView.findViewById(R.id.dialog_TXT_name);
                dialog_TXT_properties = addQueueDialogView.findViewById(R.id.dialog_TXT_properties);
                dialog_BTN_addQueue = addQueueDialogView.findViewById(R.id.dialog_BTN_addQueue);
                dialog_TP_queueTime = addQueueDialogView.findViewById(R.id.dialog_TP_queueTime);

                dialog_BTN_addQueue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id = new Random().nextInt(1000000)+100000;
                        int hour = dialog_TP_queueTime.getCurrentHour();
                        int min = dialog_TP_queueTime.getCurrentMinute();
                        String queueDate = (selectedDate.getYear()+1900) +"-"+selectedDate.getMonth()+"-"+selectedDate.getDate()+" "+hour+":"+min;

                        Queue q = new Queue()
                                .setDate(queueDate)
                                .setId(id+"")
                                .setName(dialog_TXT_name.getEditText().getText().toString())
                                .setProperties(dialog_TXT_properties.getEditText().getText().toString());

                        database.addQueue(activity, q);
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });
    }

    private ArrayList<Queue> filterByDate(ArrayList<Queue> queues, Date date){
        ArrayList<Queue> lst = new ArrayList<>();
        for(Queue queue : queues){
            Date queueDate = queue.covertStringToDate();
            if(queueDate.getYear() == date.getYear()
                    && date.getDate() == queueDate.getDate()
                    && date.getMonth() == queueDate.getMonth()){
                lst.add(queue);
            }
        }

        return lst;
    }

    private void findViews(View view){
        home_fBTN_addQueue = view.findViewById(R.id.home_fBTN_addQueue);
        home_CALV_queueCalender = view.findViewById(R.id.home_CALV_queueCalender);
        home_RCV_queues = view.findViewById(R.id.home_RCV_queues);
    }

    private Callback_Queue callback_queue = new Callback_Queue() {
        @Override
        public void addQueueComplete(boolean requestStatus) {
            if(requestStatus){
                Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void getQueuesComplete(ArrayList<Queue> queues) {
            allQueues = queues;
            selectedDateQueues = filterByDate(allQueues, selectedDate);
            setRecycleView();

        }

        @Override
        public void removeQueueComplete(boolean requestStatue) {
            if(requestStatue) {
                Toast.makeText(activity, "Queue removed!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(activity, "Remove failed!", Toast.LENGTH_SHORT).show();

            }
        }
    };


}
