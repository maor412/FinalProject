package com.example.finalproject1;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Queue implements Comparable<Queue>{
    private String id;
    private String name;
    private String date;
    private String properties;

    public Queue() {

    }
    public Queue(String name, String date, String properties) {
        this.name = name;
        this.date = date;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public Queue setName(String name) {
        this.name = name;
        return this;
    }

    public String getDate() {
        return date;
    }


    public Queue setDate(String date) {
        this.date = date;
        return this;
    }

    public String getProperties() {
        return properties;
    }

    public Queue setProperties(String properties) {
        this.properties = properties;
        return this;
    }

    public String getId() {
        return id;
    }

    public Queue setId(String id) {
        this.id = id;
        return this;
    }

    public Date covertStringToDate(){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        try {
            Date d = format.parse(this.date);
            d.setMonth(d.getMonth() + 1);
            return d;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int compareTo(Queue queue) {
        Date d1 = this.covertStringToDate();
        Date d2 = queue.covertStringToDate();
        return d1.compareTo(d2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Queue queue = (Queue) o;
        return Objects.equals(id, queue.id);
    }


}
