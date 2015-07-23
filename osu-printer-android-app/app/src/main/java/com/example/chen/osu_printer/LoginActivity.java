package com.example.chen.osu_printer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class LoginActivity extends Activity {

    private RecyclerView mRecyclerView;
    private AccountRecyclerViewAdapter mAccountsViewAdapter;
    private Button mConfirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        AccountManager.getInstance().loadFromLocalXML(getApplicationContext());

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        mRecyclerView = (RecyclerView) findViewById(R.id.activity_login_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        setupAdapter();

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        AccountManager.getInstance().setRunningAccount(AccountManager.getInstance().getAccount(position));
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        AccountManager.getInstance().saveToLocalXML(getApplicationContext());
                        startActivity(intent);
                    }
                })
        );

        new LoadPrinterInfoFromServer().execute();

    }

    private void setupAdapter() {
        if (AccountManager.getInstance().size() == 0) {
            AccountManager.getInstance().addAccount("zhante", "123456", "CSE");
            AccountManager.getInstance().addAccount("wangdin", "123456", "CSE");
            AccountManager.getInstance().addAccount("yuzhen", "123456", "CSE");
        }
        mAccountsViewAdapter = new AccountRecyclerViewAdapter(this, AccountManager.getInstance().getAccounts());
        mRecyclerView.setAdapter(mAccountsViewAdapter);
    }

    public class LoadPrinterInfoFromServer extends AsyncTask<String, String, String> {



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

//                ret = mapper.readValue(oracle.openStream(), new TypeReference<ArrayList<Printer>>(){});
//                String inputLine;
//                while ((inputLine = in.readLine()) != null)
//                    System.out.println(inputLine);
//                in.close();
            } catch (Exception e) {
                Log.e("json", Log.getStackTraceString(e));
            }
            return null;
        }
    }


}
