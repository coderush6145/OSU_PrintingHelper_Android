package com.example.chen.osu_printer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PrintFilesRecyclerViewAdapter extends RecyclerView.Adapter<PrintFilesRecyclerViewAdapter.PrintFilesViewHolder> {

    private Context mContext;
    private List<FileObject> mFiles;
    private List<Boolean> mCheckboxes;

    public PrintFilesRecyclerViewAdapter(Context context, List<FileObject> data) {
        mContext = context;
        mFiles = data;
        mCheckboxes = new ArrayList<Boolean>();
        for (int i = 0; i < data.size(); i++) mCheckboxes.add(false);
    }

    public boolean getCheckBox(int i) {
        return mCheckboxes.get(i);
    }

    public void setCheckBox(int i, boolean val) {
        mCheckboxes.set(i, val);
        return;
    }

    public ArrayList<FileObject> getToPrintFiles(){
        ArrayList<FileObject> _tmp = new ArrayList<FileObject>();
        for (int i = 0; i < mCheckboxes.size(); i++)
            if (mCheckboxes.get(i)) _tmp.add(mFiles.get(i));
        return _tmp;
    }

    public class PrintFilesViewHolder extends RecyclerView.ViewHolder {
        TextView mFileNameTextView;
        TextView mFileDescriptionTextView;
        private ImageButton mCheckButton;

        public PrintFilesViewHolder(View itemView) {
            super(itemView);
            mFileNameTextView = (TextView) itemView.findViewById(R.id.file_name);
            mFileDescriptionTextView = (TextView) itemView.findViewById(R.id.file_description);
            mCheckButton = (ImageButton) itemView.findViewById(R.id.file_checkbox);
        }
    }


    @Override
    public PrintFilesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflatedView = LayoutInflater.from(mContext).inflate(R.layout.print_file_view, viewGroup, false);
        return new PrintFilesViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(PrintFilesViewHolder viewHolder, int i) {
        FileObject File = getItem(i);
        viewHolder.mFileNameTextView.setText(File.getFileName());
        viewHolder.mFileDescriptionTextView.setText(File.getFileDescription());
        if (mCheckboxes.get(i)) {
            viewHolder.mCheckButton.setImageResource(R.drawable.ic_action_checked);
        } else {
            viewHolder.mCheckButton.setImageResource(R.drawable.ic_action_unchecked);
        }
    }

    public FileObject getItem(int position) {
        return mFiles.get(position);
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }


}
