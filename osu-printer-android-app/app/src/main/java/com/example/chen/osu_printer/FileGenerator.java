package com.example.chen.osu_printer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 15/7/14.
 */
public class FileGenerator {

    private List<FileObject> mFiles;
    private int mAmount;

    FileGenerator(int amnt){
        mAmount = amnt;
    }

    private FileObject randomFile(int index){

        FileObject _file = new FileObject();
        _file.setFileName("testFile" + String.valueOf(index) + ".pdf");
        _file.setLastUpdateTime("Apr 19");
        _file.setFilePath("");
        _file.setSize(2);
        return _file;
    }

    public List<FileObject> getFiles(){
        mFiles = new ArrayList<FileObject>();
        for (int i = 0; i < mAmount; i++) {
            mFiles.add(randomFile(i));
        }
        return mFiles;
    }

}
