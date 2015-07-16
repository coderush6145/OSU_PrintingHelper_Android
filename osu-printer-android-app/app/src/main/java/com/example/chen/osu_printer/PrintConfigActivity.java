package com.example.chen.osu_printer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PrintConfigActivity extends Activity {

    private RecyclerView mRecyclerView;
    private PrinterRecyclerViewAdapter mPrinterRecyclerViewAdapter;
    private Switch mDuplexSwitch;
    private EditText mCopiesEditText;
    private Button mPrintButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.print_config_view);

        PrintConfigManager.getInstance().loadFromLocalXML(getApplicationContext());

        mDuplexSwitch = (Switch) findViewById(R.id.doublePageSwitch);
        mDuplexSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PrintConfigManager.getInstance().setDuplex(true);
                } else {
                    PrintConfigManager.getInstance().setDuplex(false);
                }
            }
        });
        mDuplexSwitch.setChecked(PrintConfigManager.getInstance().isDuplex());

        mCopiesEditText = (EditText) findViewById(R.id.copiesEditText);
        mCopiesEditText.setText(String.valueOf(PrintConfigManager.getInstance().getCopies()));
        mCopiesEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                PrintConfigManager.getInstance().setCopies(Integer.valueOf(s.toString()));

                // you can call or do what you want with your EditText here
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        mPrintButton = (Button) findViewById(R.id.print_button);
        mPrintButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (PrintConfigManager.getInstance().getPrinter() != null) {
                    //print the file
                    Toast.makeText(getApplicationContext(), "Printing...",
                            Toast.LENGTH_LONG).show();
                    PrintConfigManager.getInstance().saveToLocalXML(getApplicationContext());
                    new PrintingProcess(PrintConfigActivity.this).execute(PrintConfigManager.getInstance());

                } else {
                    Toast.makeText(getApplicationContext(), "Please choose the printer before proceed.",
                            Toast.LENGTH_LONG).show();

                }
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.activity_print_config_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        setupAdapter();

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        if (position != ((PrinterRecyclerViewAdapter) mRecyclerView.getAdapter()).getChosenPrinter()) {

                            int prevChoice = ((PrinterRecyclerViewAdapter) mRecyclerView.getAdapter()).getChosenPrinter();
                            ((PrinterRecyclerViewAdapter) mRecyclerView.getAdapter()).setCheckBox(position, true);
                            ((PrintConfigManager) PrintConfigManager.getInstance()).setPrinter(((PrinterRecyclerViewAdapter) mRecyclerView.getAdapter()).getItem(position));

                            if (prevChoice != -1) {
                                ((PrinterRecyclerViewAdapter) mRecyclerView.getAdapter()).setCheckBox(prevChoice, false);
                                (mRecyclerView.getAdapter()).notifyItemChanged(prevChoice);
                            }
                            (mRecyclerView.getAdapter()).notifyItemChanged(position);
                            ((PrinterRecyclerViewAdapter) mRecyclerView.getAdapter()).setChosenPrinter(position);
                        }
                    }
                })
        );
    }

    private void setupAdapter() {
        List<Boolean> Checkboxes = new ArrayList<Boolean>();
        for (int i = 0; i < 20; i++) Checkboxes.add(false);
        mPrinterRecyclerViewAdapter = new PrinterRecyclerViewAdapter(this, new PrinterGenerator(20).getPrinters(), Checkboxes);
        mRecyclerView.setAdapter(mPrinterRecyclerViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_print_config_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
