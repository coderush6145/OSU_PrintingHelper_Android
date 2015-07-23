package com.example.chen.osu_printer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 15/7/14.
 */
public class PrinterGenerator {

    private List<PrinterObject> mPrinters;
    private int mAmount;

    PrinterGenerator(int amnt){
        mAmount = amnt;
    }

    private PrinterObject randomPrinter(int index){

        PrinterObject _Printer = new PrinterObject("printer" + String.valueOf(index));
        return _Printer;
    }

    public List<PrinterObject> getPrinters(){
        mPrinters = new ArrayList<PrinterObject>();
        for (int i = 0; i < mAmount; i++) {
            mPrinters.add(randomPrinter(i));
        }
        return mPrinters;
    }

}
