package com.example.chen.osu_printer;

import java.util.ArrayList;
import java.util.HashMap;

/**

 * Created by chen on 15/7/20.
 */
public class PrinterManager {
    public static ArrayList<PrinterObject> mAllPrinters;
    public static HashMap<Integer, ArrayList<PrinterObject>> mDeptPrintersMap;

    public PrinterManager(){
        mAllPrinters = new ArrayList<PrinterObject>();
        mDeptPrintersMap = new HashMap<Integer, ArrayList<PrinterObject>>();
    }

    public static ArrayList<PrinterObject> getAllPrinters() {
        return mAllPrinters;
    }

    public static void setAllPrinters(ArrayList<PrinterObject> mAllPrinters) {
        PrinterManager.mAllPrinters = mAllPrinters;
        classifyPrinters();
    }

    public static HashMap<Integer, ArrayList<PrinterObject>> getDeptPrintersMap() {
        return mDeptPrintersMap;
    }

    public static void setDeptPrintersMap(HashMap<Integer, ArrayList<PrinterObject>> mDeptPrinters) {
        PrinterManager.mDeptPrintersMap = mDeptPrinters;
    }

    public static void classifyPrinters(){
        for (PrinterObject i : mAllPrinters) {
            if (mDeptPrintersMap.containsKey(i.getDepartmentNo())) {
                mDeptPrintersMap.get(i.getDepartmentNo()).add(i);
            } else {
                ArrayList<PrinterObject> _tmp = new ArrayList<PrinterObject>();
                _tmp.add(i);
                mDeptPrintersMap.put(i.getDepartmentNo(), _tmp);
            }
        }

    }
}
