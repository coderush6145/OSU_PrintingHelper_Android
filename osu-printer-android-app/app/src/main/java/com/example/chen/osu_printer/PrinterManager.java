package com.example.chen.osu_printer;

import java.util.ArrayList;
import java.util.HashMap;

/**

 * Created by chen on 15/7/20.
 */
public class PrinterManager {
    public static ArrayList<PrinterObject> mAllPrinters;
    public static HashMap<String, ArrayList<PrinterObject>> mDeptPrintersMap;

    public PrinterManager(){
        mAllPrinters = new ArrayList<PrinterObject>();
        mDeptPrintersMap = new HashMap<String, ArrayList<PrinterObject>>();
    }

    public static ArrayList<PrinterObject> getAllPrinters() {
        return mAllPrinters;
    }

    public static void setAllPrinters(ArrayList<PrinterObject> mAllPrinters) {
        PrinterManager.mAllPrinters = mAllPrinters;
        classifyPrinters();
    }

    public static HashMap<String, ArrayList<PrinterObject>> getDeptPrintersMap() {
        return mDeptPrintersMap;
    }

    public static void setDeptPrintersMap(HashMap<String, ArrayList<PrinterObject>> mDeptPrinters) {
        PrinterManager.mDeptPrintersMap = mDeptPrinters;
    }

    public static void classifyPrinters(){
        for (PrinterObject i : mAllPrinters) {
            if (mDeptPrintersMap.containsKey(i.getDepartment())) {
                mDeptPrintersMap.get(i.getDepartment()).add(i);
            } else {
                ArrayList<PrinterObject> _tmp = new ArrayList<PrinterObject>();
                _tmp.add(i);
                mDeptPrintersMap.put(i.getDepartment(), _tmp);
            }
        }

    }
}
