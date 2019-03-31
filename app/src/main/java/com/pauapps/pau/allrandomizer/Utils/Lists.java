package com.pauapps.pau.allrandomizer.Utils;


/**
 * Created by Pau on 14/10/2018.
 */

public class Lists {

    protected String name = "list";
    public String title;
    public String item;
    public int max = 1;

    public Lists(String title, String item) {
        this.title = title;
        this.item = item;
    }

    public Lists() {

    }

    public int getMax() {
        return max;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
