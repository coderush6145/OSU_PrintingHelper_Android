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

/**
 * Created by chen on 15/7/15.
 */
public class PrinterRecyclerViewAdapter extends RecyclerView.Adapter<PrinterRecyclerViewAdapter.PrintersViewHolder> {
    private Context mContext;
    private List<PrinterObject> mPrinters;
    private List<Boolean> mCheckboxes;
    private int mChosenPrinter;

    public PrinterRecyclerViewAdapter(Context context, List<PrinterObject> data) {
        mContext = context;
        mPrinters = data;
        mCheckboxes = new ArrayList<Boolean>();
        for (int _i = 0; _i < data.size(); _i++) mCheckboxes.add(false);
        mChosenPrinter = -1;
    }

    public void setChosenPrinter(int i){
        mChosenPrinter = i;
    }

    public int getChosenPrinter() {
        return mChosenPrinter;
    }

    public boolean getCheckBox(int i) {
        return mCheckboxes.get(i);
    }

    public void setCheckBox(int i, boolean val) {
        mCheckboxes.set(i, val);
        return;
    }

    public class PrintersViewHolder extends RecyclerView.ViewHolder {
        TextView mPrinterNameTextView;
        private ImageButton mCheckButton;

        public PrintersViewHolder(View itemView) {
            super(itemView);
            mPrinterNameTextView = (TextView) itemView.findViewById(R.id.printer_name);
            mCheckButton = (ImageButton) itemView.findViewById(R.id.printer_checkbox);
        }
    }


    @Override
    public PrintersViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflatedView = LayoutInflater.from(mContext).inflate(R.layout.printer_candidates_view, viewGroup, false);
        return new PrintersViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(PrintersViewHolder viewHolder, int i) {
        PrinterObject Printer = getItem(i);
        viewHolder.mPrinterNameTextView.setText(Printer.getPrinterName());
        if (mCheckboxes.get(i)) {
            viewHolder.mCheckButton.setImageResource(R.drawable.ic_action_checked);
        } else {
            viewHolder.mCheckButton.setImageResource(R.drawable.ic_action_unchecked);
        }
    }

    public PrinterObject getItem(int position) {
        return mPrinters.get(position);
    }

    @Override
    public int getItemCount() {
        return mPrinters.size();
    }
}
