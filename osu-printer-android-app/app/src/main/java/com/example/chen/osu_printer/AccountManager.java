package com.example.chen.osu_printer;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 15/7/15.
 */

public class AccountManager {
    List<Account> mAccounts;
    private static AccountManager mInstance;
    private static final char separator = (char)251;

    public class Account{
        private String mUsername;
        private String mPassword;
        private String department;

        public String getUsername() {
            return mUsername;
        }

        public void setUsername(String mUsername) {
            this.mUsername = mUsername;
        }

        public String getPassword() {
            return mPassword;
        }

        public void setPassword(String mPassword) {
            this.mPassword = mPassword;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }
    }

    private AccountManager(){
        mAccounts = new ArrayList<Account>();
    }

    public static AccountManager getInstance(){
        if (mInstance == null) {
            mInstance = new AccountManager();
        }
       return mInstance;
    }

    public void reset() {
        mAccounts = new ArrayList<Account>();
    }

    public boolean addAccount(String username, String password, String dept){
        Account _account = new Account();
        _account.setUsername(username);
        _account.setPassword(password);
        switch (dept) {
            case "ECE":
                _account.setDepartment("ECE");
                break;

            case "CSE":
                _account.setDepartment("CSE");
                break;

            default:
                return false;

        }
        mAccounts.add(_account);

        return true;
    }

    public Account getAccount(int i){
        return mAccounts.get(i);
    }

    public int size(){
        return mAccounts.size();
    }

    public void deleteAccount(int i){
        mAccounts.remove(i);
    }

    public void loadFromLocalXML(Context context) {      //pass application as context

        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("account_amount",
                this.size()).commit();
        for (int _iter = 0; _iter < AccountManager.getInstance().size(); _iter++) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString("account_" + String.valueOf(_iter),
                    this.getAccount(_iter).getUsername() + separator +
                    this.getAccount(_iter).getPassword() + separator +
                    this.getAccount(_iter).getDepartment() + separator).commit();
        }
    }

    public void saveToLocalXML(Context context){         //pass application as context

        AccountManager.getInstance().reset();
        int _cnt = PreferenceManager.getDefaultSharedPreferences(context).getInt("account_amount", 0);
        for (int _iter = 0; _iter < _cnt; _iter++) {
            String[] _tmp;
            _tmp = PreferenceManager.getDefaultSharedPreferences(context).getString("account_" + String.valueOf(_iter), "").split(String.valueOf(separator));
            this.addAccount(_tmp[0], _tmp[1], _tmp[2]);
        }
    }
}
