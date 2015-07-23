package com.example.chen.osu_printer;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by chen on 15/7/15.
 */
public class PrinterObject {

    @JsonProperty("Location")
    String mLocation;
    @JsonProperty("Name")
    String mPrinterName;
    @JsonProperty("Type")
    String mType;
    @JsonProperty("Department")
    String mDepartment;

    public int getDepartmentNo() {
        for (int i = 0; i < GlobalParameters.department.length; i++) {
            if (GlobalParameters.department[i].equals(mDepartment)) {
                return  i;
            }
        }
        return -1;
    }

    PrinterObject(){
    }

    PrinterObject(String str){
        this.mPrinterName = str;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public String getPrinterName() {
        return mPrinterName;
    }

    public void setPrinterName(String mPrinterName) {
        this.mPrinterName = mPrinterName;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public String getDepartment() {
        return mDepartment;
    }

    public void setDepartment(String mDepartment) {
        this.mDepartment = mDepartment;
    }
}
