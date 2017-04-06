package com.gopetting.rekha;


import java.io.Serializable;

public class ObjectsBean implements Serializable{
    public String endDate="";
    public String name="";
    public String icon="";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}