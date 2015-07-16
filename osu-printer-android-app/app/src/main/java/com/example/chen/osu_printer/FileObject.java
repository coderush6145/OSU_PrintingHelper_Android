package com.example.chen.osu_printer;

/**
 * Created by chen on 15/7/10.
 */
public class FileObject {
    private String fileName;
    private String lastUpdateTime;
    private int size;
    private String filePath;

    FileObject() {
        this.fileName = "";
        this.lastUpdateTime = "";
        this.size = 0;
        this.filePath = "";
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setFilePath(String location) {
        this.filePath = location;
    }

    public String getFileName() {

        return fileName;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public int getSize() {
        return size;
    }

    public String getFilePath() {
        return filePath;
    }


    public String getFileDescription(){
        return "Modified: " + lastUpdateTime + "   " + "Size: " + String.valueOf(size) + "MB";

    }
}
