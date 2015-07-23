package com.example.chen.osu_printer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private PrintFilesRecyclerViewAdapter mPrintFilesRecyclerViewAdapter;
    private Button mConfirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
        setContentView(R.layout.activity_main);



        mConfirmButton = (Button) findViewById(R.id.confirm_button);
        mConfirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (((PrintFilesRecyclerViewAdapter) mRecyclerView.getAdapter()).getToPrintFiles().size() > 0) {
                    PrintConfigManager.getInstance().setToPrintFiles((
                            (PrintFilesRecyclerViewAdapter) mRecyclerView.getAdapter()).getToPrintFiles());

                    Intent intent = new Intent(MainActivity.this, PrintConfigActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please select at least one file to print",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.activity_main_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        setupAdapter();

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        if (((PrintFilesRecyclerViewAdapter)mRecyclerView.getAdapter()).getCheckBox(position) == false) {
                            ((PrintFilesRecyclerViewAdapter)mRecyclerView.getAdapter()).setCheckBox(position, true);
                        } else {
                            ((PrintFilesRecyclerViewAdapter)mRecyclerView.getAdapter()).setCheckBox(position, false);
                        }
                        ((PrintFilesRecyclerViewAdapter)mRecyclerView.getAdapter()).notifyItemRangeChanged(position, 1);
                    }
                })
        );

        mSwipeRefreshLayout.setColorSchemeResources(R.color.Orange, R.color.Green, R.color.Blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setupAdapter();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                },1000);
            }
        });
    }

    private void setupAdapter() {

        //test

        ArrayList<String> ext = new ArrayList<String>();
        ext.add("pdf");
        ext.add("doc");
        FileFetcher fetcher = new FileFetcher(MainActivity.this, 1000000000, ext);
        ArrayList<FileObject> files = (ArrayList<FileObject>)fetcher.fetchFiles();

//        if (! files.isEmpty()) {
            mPrintFilesRecyclerViewAdapter = new PrintFilesRecyclerViewAdapter(this, files);
            mRecyclerView.setAdapter(mPrintFilesRecyclerViewAdapter);
//        } else {
//            mRecyclerView.setVisibility(View.GONE);
//            TextView _text = (TextView) findViewById(R.id.empty_view);
//            _text.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(getApplicationContext(), "this is my Toast message!!! search!!",
                        Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_settings:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
