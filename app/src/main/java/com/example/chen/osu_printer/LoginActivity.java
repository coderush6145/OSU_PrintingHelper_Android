package com.example.chen.osu_printer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class LoginActivity extends Activity {

    private final int ADD_NEW_ACCOUNT = 0;
    private final int MODIFY_EXISTING_ACCOUNT = 1;

    private RecyclerView mRecyclerView;
    private AccountRecyclerViewAdapter mAccountsViewAdapter;
    private PopupWindow mPopupWindow = null;
    private Button mTutorialButton;
    private Button mNewAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        AccountManager.getInstance().loadFromLocalXML(getApplicationContext());

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        mTutorialButton = (Button) findViewById(R.id.tutorial_button);
        mTutorialButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //add tutorial pages
            }
        });

        mNewAccountButton = (Button) findViewById(R.id.add_account_button);
        mNewAccountButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showPopup(null, -1, ADD_NEW_ACCOUNT);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.activity_login_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        setupAdapter();

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        AccountManager.getInstance().setRunningAccount(AccountManager.getInstance().getAccount(position));
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, final int position) {

                        Vibrator v = (Vibrator) LoginActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(50);
                        showPopup(view, position, MODIFY_EXISTING_ACCOUNT);

                    }

                    @Override
                    public void onItemSwipe(View view, int position) {
                    }
                })
        );

        new LoadPrinterInfoFromServer().execute();

    }


    private void setupAdapter() {

        mAccountsViewAdapter = new AccountRecyclerViewAdapter(this, AccountManager.getInstance().getAccounts());
        mRecyclerView.setAdapter(mAccountsViewAdapter);
    }


    private void showPopup(View view, final int position, final int type){
        LayoutInflater layoutInflater= (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.login_popup_view, null);
        mPopupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.color.Gray);

        BitmapDrawable bmDrawable=new BitmapDrawable(getResources(), bm);
        mPopupWindow.setBackgroundDrawable(bmDrawable);


        final EditText nameText = (EditText)popupView.findViewById(R.id.popup_name_text);
        final EditText usernameText = (EditText)popupView.findViewById(R.id.popup_username_text);
        final EditText passwordText = (EditText)popupView.findViewById(R.id.popup_password_text);


        final NumberPicker deptPicker = (NumberPicker) popupView.findViewById(R.id.dept_picker);
        deptPicker.setMaxValue(GlobalParameters.department.length - 1);
        deptPicker.setMinValue(0);
        deptPicker.setDisplayedValues(GlobalParameters.department);
        deptPicker.setWrapSelectorWheel(false);

        if (type == MODIFY_EXISTING_ACCOUNT) {
            nameText.setText(AccountManager.getInstance().getAccount(position).getName());
            usernameText.setText(AccountManager.getInstance().getAccount(position).getUsername());
            passwordText.setText("******");
            deptPicker.setValue(AccountManager.getInstance().getAccount(position).getDepartment());
        }

        Button saveButton = (Button)popupView.findViewById(R.id.popup_save_button);
        saveButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                if (!(nameText.getText().toString().length() == 0 || usernameText.toString().length() == 0 || passwordText.toString().length() == 0)) {

                    if (type == MODIFY_EXISTING_ACCOUNT) {
                        AccountManager.getInstance().getAccount(position).setName(nameText.getText().toString());
                        AccountManager.getInstance().getAccount(position).setUsername(usernameText.getText().toString());
                        AccountManager.getInstance().getAccount(position).setPassword(passwordText.getText().toString());
                        AccountManager.getInstance().getAccount(position).setDepartment(deptPicker.getValue());
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                        mPopupWindow.dismiss();

                    } else if (type == ADD_NEW_ACCOUNT){
                        AccountManager.getInstance().addAccount(nameText.getText().toString(),
                                                                usernameText.getText().toString(),
                                                                passwordText.getText().toString(),
                                                                deptPicker.getValue());
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                        mPopupWindow.dismiss();
                    }
                    AccountManager.getInstance().saveToLocalXML(getApplicationContext());

                } else {
                    Toast.makeText(getApplicationContext(), "Please fill in all of the fields before saving.", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button delButton = (Button)popupView.findViewById(R.id.popup_delete_button);
        delButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (type == MODIFY_EXISTING_ACCOUNT) {
                    AccountManager.getInstance().deleteAccount(position);
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                }
                mPopupWindow.dismiss();
                AccountManager.getInstance().saveToLocalXML(getApplicationContext());
            }
        });

        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

    }


    @Override
    public void onBackPressed() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        } else {
            super.onBackPressed();
        }
    }


    public class LoadPrinterInfoFromServer extends AsyncTask<String, String, String> {
        private Context mContext;

        protected String doInBackground(String ...args) { //parameters needed
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
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(((Activity)mContext), "Unable to fetch info from server, recheck your network connection might be helpful.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }
    }



}
