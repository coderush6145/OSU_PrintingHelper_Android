package com.example.chen.osu_printer;

/**
 * Created by chen on 15/7/15.
 */
public class PrinterObject {
    String mPrinterName;

    PrinterObject(String str){
        this.mPrinterName = str;
    }

    public void setPrinterName(String mPrinterName) {
        this.mPrinterName = mPrinterName;
    }

    public String getPrinterName() {
        return mPrinterName;
    }
}
