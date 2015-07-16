package com.example.chen.osu_printer;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.List;

/**
 * Created by chen on 15/7/15.
 */
public class PrintConfigManager {

    private boolean mDuplex;
    private int copies;
    private static final char separator = (char)251;
    public static final String defaultFolder = "OSU_printer";


    public List<FileObject> getToPrintFiles() {
        return toPrintFiles;
    }

    public void setToPrintFiles(List<FileObject> toPrintFiles) {
        this.toPrintFiles = toPrintFiles;
    }

    private PrinterObject printer;
    private static PrintConfigManager mInstance;
    private List<FileObject> toPrintFiles;

    public static PrintConfigManager getInstance() {
        if (mInstance == null)
            mInstance = new PrintConfigManager();
        return mInstance;
    }

    public void reconfig() {
        mDuplex = false;
        copies = 1;
        printer = null;
    }

    private PrintConfigManager() {
        mDuplex = false;
        copies = 1;
        printer = null;
        toPrintFiles = null;
    }

    public boolean isDuplex() {
        return mDuplex;
    }

    public void setDuplex(boolean duplex) {
        this.mDuplex = duplex;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public PrinterObject getPrinter() {
        return printer;
    }

    public void setPrinter(PrinterObject printer) {
        this.printer = printer;
    }

    public void loadFromLocalXML(Context context) {      //pass application as context
        this.reconfig();
        this.mDuplex = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("config_duplex",
                false);
        this.copies = PreferenceManager.getDefaultSharedPreferences(context).getInt("config_copies",
                1);

    }

    public void saveToLocalXML(Context context){         //pass application as context

        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("config_duplex",
                this.mDuplex).commit();
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("config_copies",
                this.copies).commit();

    }

}
