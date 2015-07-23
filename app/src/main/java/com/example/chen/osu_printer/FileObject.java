package com.example.chen.osu_printer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chen on 15/7/10.
 */
public class FileObject {
    private String fileName;
    private long lastUpdateTime;
    private long size;
    private String filePath;
    private  String lastUpdateDate;
    private String fileType;

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    FileObject() {
        this.fileName = "";
        this.lastUpdateTime = 0;
        this.size = 0;
        this.filePath = "";
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(long pastSecs) {

        SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy HH:mm");
        String dateString = formatter.format(new Date(pastSecs * 1000L));

        this.lastUpdateDate = dateString;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
        setLastUpdateDate(lastUpdateTime);
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setFilePath(String location) {
        this.filePath = location;

        //generate filename
        String[] _tmp = filePath.split("/");
        this.setFileName(_tmp[_tmp.length -1]);

        //generate filetype
        _tmp = filePath.split("\\.");
        this.setFileType(_tmp[_tmp.length - 1].toLowerCase());
    }

    public String getFileName() {
        return fileName;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public long getSize() {
        return size;
    }

    public String getFilePath() {
        return filePath;
    }


    public String getFileDescription(){

        String strSize;
        if (size < 1024) {
            strSize = String.valueOf(size) + "B";
        } else if (size < 1024*1024) {
            strSize = String.valueOf(size/1024) + "KB";
        } else {
            strSize = String.valueOf(size/1024/1024) + "MB";
        }

        return "Modified: " + lastUpdateDate + "   " + "Size: " + strSize;

    }
}
