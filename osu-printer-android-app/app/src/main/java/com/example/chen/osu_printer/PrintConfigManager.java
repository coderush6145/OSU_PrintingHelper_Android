package com.example.chen.osu_printer;

import java.util.List;

/**
 * Created by chen on 15/7/15.
 */
public class PrintConfigManager {

    private boolean duplex;
    private int copies;
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

    public static PrintConfigManager getInstance(){
        if (mInstance == null)
            mInstance = new PrintConfigManager();
        return mInstance;
    }

    public void restart(){
        duplex = false;
        copies = 1;
        printer = null;
    }

    public void reconfig(){
        duplex = false;
        copies = 1;
        printer = null;
        toPrintFiles = null;
    }

    private PrintConfigManager(){
        duplex = false;
        copies = 1;
        printer = null;
        toPrintFiles = null;
    }

    public boolean isDuplex() {
        return duplex;
    }

    public void setDuplex(boolean duplex) {
        this.duplex = duplex;
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
}
