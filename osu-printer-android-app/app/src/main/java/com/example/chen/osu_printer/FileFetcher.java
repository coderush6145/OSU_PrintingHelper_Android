package com.example.chen.osu_printer;

/**
 * Created by chen on 15/7/17.
 */
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DK Wang on 2015/7/9.
 */
public class FileFetcher {

    private final int DEFAULT_ELAPSED_TIME = 24 * 60 * 60;

    private final String DEFAULT_FILE_TYPE = "pdf";

    private final String QUERY_MODIFIED_DATE = MediaStore.Files.FileColumns.DATE_MODIFIED;

    private final String QUERY_FILE_PATH = MediaStore.Files.FileColumns.DATA;

    private final String QUERY_FILE_SIZE = MediaStore.Files.FileColumns.SIZE;

    private final String QUERY_EXTENSION = MediaStore.Files.FileColumns.MIME_TYPE;

    private final MimeTypeMap typeMap = MimeTypeMap.getSingleton();

    // Context for query
    private Context context;

    // Time range
    private int elapsedTime;

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    // Extensions you assign
    private List<String> extensions;

    public List<String> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<String> extensions) {
        this.extensions = extensions;
    }

    /**
     * Init the class with only a context, you should set elapsedTime and extensions before calling the
     * fetching functions, otherwise the elapsedTime is set to the default value, and
     * the extensions will be set to default.
     *
     * @param context Context for fetching files in Android system
     */
    public FileFetcher(Context context) {
        this.context = context;
        this.elapsedTime = DEFAULT_ELAPSED_TIME;
        this.extensions = new ArrayList<String>();
        this.extensions.add(DEFAULT_FILE_TYPE);
    }

    /**
     * Fetch all the files from currentTime - elapsedTime to currentTime, the extensions will be set to default.
     *
     * @param context     Context for fetching files in Android system
     * @param elapsedTime The given range of time, in seconds
     */
    public FileFetcher(Context context, int elapsedTime) {
        this.context = context;
        this.elapsedTime = elapsedTime;
        this.extensions = new ArrayList<String>();
        this.extensions.add(DEFAULT_FILE_TYPE);
    }

    /**
     * Fetch all the files in the given range of time and with any one of the given extensions
     *
     * @param context     Context for fetching files in Android system
     * @param elapsedTime The given range of time, in second
     * @param extensions  The given extensions, like 'pdf', 'doc', etc. There should be no leading dot.
     */
    public FileFetcher(Context context, int elapsedTime, List<String> extensions) {
        this.context = context;
        this.elapsedTime = elapsedTime;
        this.extensions = extensions;
    }

    /**
     *
     * @return Return a list of , because i don't know what you need, some properties may be null.
     *         The returned value may be null, if there is no file fit. The sorting order is automatically set
     *         to "sort by filePath asc, modifiedTime desc".
     */
    public List<FileObject> fetchFiles() {
        if(elapsedTime == 0)
            return null;

        ContentResolver cr = context.getContentResolver();
        Uri uri = MediaStore.Files.getContentUri("external");

        String[] projection = null;

        String selection = getQuerySelection();
        String[] selectionArgs = getQuerySelectionArgs();

        String sortOrder = QUERY_MODIFIED_DATE + " desc," + QUERY_FILE_PATH + " asc";
        Cursor allFiles = cr.query(uri, projection, selection, selectionArgs, sortOrder);

        List<FileObject> ret = new ArrayList<FileObject>();

        while(allFiles.moveToNext()){
            FileObject temp = new FileObject();
            int indexOfFilePath = allFiles.getColumnIndex(QUERY_FILE_PATH);
            if(indexOfFilePath != -1){
                temp.setFilePath(allFiles.getString(indexOfFilePath));
            }

            int indexOfFileSize = allFiles.getColumnIndex(QUERY_FILE_SIZE);
            if(indexOfFileSize != -1){
                temp.setSize(allFiles.getLong(indexOfFileSize));
            }

            int indexOfModifiedTime = allFiles.getColumnIndex(QUERY_MODIFIED_DATE);
            if(indexOfModifiedTime != -1){
                temp.setLastUpdateTime(allFiles.getLong(indexOfModifiedTime));
            }
            ret.add(temp);
        }

        allFiles.close();

        return ret;
    }

    /**
     *
     * @return It should look like (MIMETYPE = ? OR .... MIMETYPE = ?) AND MODIFIED_TIME >= currentTime - elapsedTime
     */
    private String getQuerySelection(){
        if(extensions == null || extensions.size() == 0)
            return null;
        String selection = "(";
        for(int i=0; i<extensions.size(); i++){
            selection += QUERY_EXTENSION + " = ?";
            if(i != extensions.size() - 1){
                selection += " OR ";
            }
        }
        selection += ") AND ";
        selection += QUERY_MODIFIED_DATE;
        selection += " >= ";
        selection += (System.currentTimeMillis() / 1000 - elapsedTime);
        return selection;
    }

    /**
     *
     * @return A String array of MIME types. Transfer the given file extensions like 'pdf' to its corresponding MIME types which can be used in the query.
     */
    private String[] getQuerySelectionArgs(){
        if(extensions == null || extensions.size() == 0)
            return null;
        String[] ret = new String[extensions.size()];
        for(int i=0; i<extensions.size(); i++)
            ret[i] = typeMap.getMimeTypeFromExtension(extensions.get(i));
        return ret;
    }

}