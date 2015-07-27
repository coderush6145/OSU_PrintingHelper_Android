package com.example.chen.osu_printer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class PrintConfigActivity extends Activity {

    private RecyclerView mRecyclerViewPrinter;
    private RecyclerView mRecyclerViewAccounts;
    private TextView mEmptyView;
    private PrinterRecyclerViewAdapter mPrinterRecyclerViewAdapter;
    private ArrayList<PrinterObject> mAvailablePrinters;
    private AccountRecyclerViewAdapter_2 mAccountsViewAdapter;
    private Switch mDuplexSwitch;
    private EditText mCopiesEditText;
    private Button mPrintButton;
    private PopupWindow mPopupWindow;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_print_config);


        //initialization
        PrintConfigManager.getInstance().loadFromLocalXML(getApplicationContext());
        final Toast mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);


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
                if (s.toString().length() > 0 && s.toString().length() < 4 && Integer.valueOf(s.toString()) > 0) {
                    PrintConfigManager.getInstance().setCopies(Integer.valueOf(s.toString()));
                }
                if (s.toString().length() >= 4) {

                    mToast.setText("Please note that a too big number will not be accepted.");
                    mToast.show();
                }

                // you can call or do what you want with your EditText here
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        mPrintButton = (Button) findViewById(R.id.print_button);
        mPrintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCopiesEditText.getText().toString().length() != 0 && mCopiesEditText.getText().toString().length() < 4 && Integer.valueOf(mCopiesEditText.getText().toString()) >= 1) {
                    if (PrintConfigManager.getInstance().getPrinter() != null) {
                        //print the file
                        PrintConfigManager.getInstance().saveToLocalXML(getApplicationContext());
                        new PrintingProcess(PrintConfigActivity.this).execute(PrintConfigManager.getInstance());

                    } else {
                        mToast.setText("Please choose the printer before proceed.");
                        mToast.show();
                    }
                } else {
                    if (mCopiesEditText.getText().toString().length() == 0) {
                        mToast.setText("Invalid input number.");
                        mToast.show();
                    } else if (mCopiesEditText.getText().toString().length() == 0) {
                        mToast.setText("Please fill in the number of copies needed before proceed.");
                        mToast.show();

                    } else if (Integer.valueOf(mCopiesEditText.getText().toString()) < 1) {
                        mToast.setText("Please fill in a number greater than 0 in the copies field.");
                        mToast.show();
                    }
                }
            }
        });

        mRecyclerViewPrinter = (RecyclerView) findViewById(R.id.activity_print_config_recyclerview);
        mRecyclerViewPrinter.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewPrinter.setLayoutManager(layoutManager);

        mRecyclerViewPrinter.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRecyclerViewPrinter, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        if (position != ((PrinterRecyclerViewAdapter) mRecyclerViewPrinter.getAdapter()).getChosenPrinter()) {

                            int prevChoice = ((PrinterRecyclerViewAdapter) mRecyclerViewPrinter.getAdapter()).getChosenPrinter();
                            ((PrinterRecyclerViewAdapter) mRecyclerViewPrinter.getAdapter()).setCheckBox(position, true);
                            ((PrintConfigManager) PrintConfigManager.getInstance()).setPrinter(((PrinterRecyclerViewAdapter) mRecyclerViewPrinter.getAdapter()).getItem(position));

                            if (prevChoice != -1) {
                                ((PrinterRecyclerViewAdapter) mRecyclerViewPrinter.getAdapter()).setCheckBox(prevChoice, false);
                                (mRecyclerViewPrinter.getAdapter()).notifyItemChanged(prevChoice);
                            }
                            (mRecyclerViewPrinter.getAdapter()).notifyItemChanged(position);
                            ((PrinterRecyclerViewAdapter) mRecyclerViewPrinter.getAdapter()).setChosenPrinter(position);
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                    }

                    @Override
                    public void onItemSwipe(View view, int position) {
                    }
                })
        );

        if (AccountManager.getInstance().getRunningAccount() == null) {
            //handle the case when app is used by external application to open a printable file
            handleExternalIntent();
        } else {
            setupAdapter();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        handleExternalIntent();
    }


    private void showAccountSelectionPopup(View popupView){
        LayoutInflater layoutInflater= (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.print_config_popup_view, null);
        mPopupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.color.Gray);

        BitmapDrawable bmDrawable=new BitmapDrawable(getResources(), bm);
        mPopupWindow.setBackgroundDrawable(bmDrawable);

        mRecyclerViewAccounts = (RecyclerView) popupView.findViewById(R.id.activity_print_config_popup_recyclerview);
//        mRecyclerViewAccounts.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewAccounts.setLayoutManager(layoutManager);

        //setup adapter
        mAccountsViewAdapter = new AccountRecyclerViewAdapter_2(this, AccountManager.getInstance().getAccounts());
        mRecyclerViewAccounts.setAdapter(mAccountsViewAdapter);

        mRecyclerViewAccounts.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRecyclerViewAccounts, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                    }

                    @Override
                    public void onItemLongClick(View view, final int position) {
                    }

                    @Override
                    public void onItemSwipe(View view, int position) {
                    }
                })
        );


        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

    }



    //handle the case when app is used by external application to open a printable file
    public void handleExternalIntent() {
        Intent _intent = getIntent();
        String filePath = "";

        if (Intent.ACTION_VIEW.equals(_intent.getAction())) {
            filePath = _intent.getData().getPath();
            ArrayList<FileObject> toPrintFile = new ArrayList<FileObject>();
            toPrintFile.add(new FileObject(filePath));
            PrintConfigManager.getInstance().setToPrintFiles(toPrintFile);


            AccountManager.getInstance().loadFromLocalXML(getApplicationContext());
            if (AccountManager.getInstance().getAccounts().size() == 1){

                AccountManager.getInstance().setRunningAccount(AccountManager.getInstance().getAccount(0));

            } else {
                LayoutInflater layoutInflater= (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                final View popupView = layoutInflater.inflate(R.layout.print_config_popup_view, null);
                popupView.post(new Runnable() {
                    public void run() {
                        showAccountSelectionPopup(popupView);
                    }
                });

            }


            //Only the first time entering from external application need to load info from server, after the first time we only need to select the printers of the corresponding dept.
            new LoadPrinterInfoFromServer().execute();
        }

    }


    //check if need to inflate an empty view instead of the recyclerview
    private void checkEmpty(){

        if (mEmptyView == null) {
            mEmptyView = (TextView) findViewById(R.id.activity_print_config_emptyview);
        }
        if (mRecyclerViewPrinter.getAdapter() == null || mRecyclerViewPrinter.getAdapter().getItemCount() == 0) {
            mRecyclerViewPrinter.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyView.setTypeface(Typeface.MONOSPACE);
        } else {
            mRecyclerViewPrinter.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }


    //for recyclerview
    private void setupAdapter() {

        try {
            mAvailablePrinters = PrinterManager.mDeptPrintersMap.get(AccountManager.getInstance().getRunningAccount().getDepartment());

        }
        catch (Exception e) {

        }

        if (mAvailablePrinters != null) {
            mPrinterRecyclerViewAdapter = new PrinterRecyclerViewAdapter(this, mAvailablePrinters);
            mRecyclerViewPrinter.setAdapter(mPrinterRecyclerViewAdapter);
        }
        checkEmpty();
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


    public class LoadPrinterInfoFromServer extends AsyncTask<String, String, Boolean> {

        protected Boolean doInBackground(String ...args) { //parameters needed
            try {
                URL oracle = new URL("http://web.cse.ohio-state.edu/~zhante/OSU_printers.json");
                BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
                String printerInfo = "";
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    printerInfo += inputLine + "\n";
                in.close();

                ObjectMapper mapper = new ObjectMapper();
                mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                ArrayList<PrinterObject> ret = mapper.readValue(printerInfo, new TypeReference<ArrayList<PrinterObject>>(){});
                new PrinterManager().setAllPrinters(ret);

            } catch (Exception e) {
                PrintConfigActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(PrintConfigActivity.this, "Unable to fetch info from server, recheck your network connection might be helpful.", Toast.LENGTH_LONG).show();
                    }
                });

                return false;
            }
            return true;
        }


        @Override
        protected void onPostExecute(Boolean loadResult) {
            super.onPostExecute(loadResult);

            setupAdapter();
        }
    }
}
