package com.example.chen.osu_printer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 15/7/15.
 */

public class AccountManager {
    List<Account> mAccounts;
    private AccountManager mInstance;

    private class Account{
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

    public AccountManager getInstance(){
        if (mInstance == null) {
            mInstance = new AccountManager();
        }
       return mInstance;
    }

    private boolean addAccount(String username, String password, String dept){
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

    private Account getAccount(int i){
        return mAccounts.get(i);
    }

    private int size(){
        return mAccounts.size();
    }

    private void deleteAccount(int i){
        mAccounts.remove(i);
    }
}
