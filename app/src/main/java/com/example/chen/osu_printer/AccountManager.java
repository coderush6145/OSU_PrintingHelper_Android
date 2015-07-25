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

    public Account getRunningAccount() {
        return mRunningAccount;
    }

    public void setRunningAccount(Account account) {
        this.mRunningAccount = account;
    }

    private Account mRunningAccount;

    public class Account{
        private String mUsername;
        private String mPassword;
        private int department;
        private String mName;

        public String getName() {
            return mName;
        }

        public void setName(String mName) {
            this.mName = mName;
        }

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

        public int getDepartment() {
            return department;
        }

        public void setDepartment(int department) {
            this.department = department;
        }
    }

    private AccountManager(){
        mAccounts = new ArrayList<Account>();
        mRunningAccount = null;
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

    public boolean addAccount(String name, String username, String password, int dept){
        Account _account = new Account();
        _account.setUsername(username);
        _account.setPassword(password);
        _account.setName(name);
        _account.setDepartment(dept);
        mAccounts.add(_account);

        return true;
    }

    public Account getAccount(int i){
        return mAccounts.get(i);
    }

    public List<Account> getAccounts(){
        return mAccounts;
    }

    public int size(){
        return mAccounts.size();
    }

    public void deleteAccount(int i){
        mAccounts.remove(i);
    }

    public void loadFromLocalXML(Context context) {      //pass application as context

        AccountManager.getInstance().reset();
        int _cnt = PreferenceManager.getDefaultSharedPreferences(context).getInt("Account_amount", 0);
        for (int _iter = 0; _iter < _cnt; _iter++) {
            String[] _tmp;
            _tmp = PreferenceManager.getDefaultSharedPreferences(context).getString("Account_" + String.valueOf(_iter), "").split(String.valueOf(separator));
            this.addAccount(_tmp[0], _tmp[1], _tmp[2], Integer.valueOf(_tmp[3]));
        }

    }

    public void saveToLocalXML(Context context){         //pass application as context

        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("Account_amount",
                this.size()).commit();
        for (int _iter = 0; _iter < AccountManager.getInstance().size(); _iter++) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString("Account_" + String.valueOf(_iter),
                    this.getAccount(_iter).getName() + separator +
                    this.getAccount(_iter).getUsername() + separator +
                    this.getAccount(_iter).getPassword() + separator +
                    String.valueOf(this.getAccount(_iter).getDepartment())).commit();
        }
    }
}
