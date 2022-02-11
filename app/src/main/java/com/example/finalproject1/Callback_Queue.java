package com.example.finalproject1;

import java.util.ArrayList;

public interface Callback_Queue {
    void addQueueComplete(boolean requestStatus);
    void getQueuesComplete(ArrayList<Queue> queues);
    void removeQueueComplete(boolean requestStatue);
}
