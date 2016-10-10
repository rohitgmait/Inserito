package com.event.inserito.dost;

/**
 * Created by rgupt on 08-10-2016.
 */
public class Crop {
    public long id;
    public String name;
    public String month;
    public String day;
    Crop(long id,String crop,String month,String day) {
        this.id = id;
        this.name = crop;
        this.month = month;
        this.day = day;
    }
}
