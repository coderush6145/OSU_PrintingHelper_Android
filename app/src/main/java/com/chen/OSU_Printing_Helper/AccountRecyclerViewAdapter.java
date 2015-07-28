package com.chen.OSU_Printing_Helper;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chen.OSU_Printing_Helper.R;

import java.util.List;

/**
 * Created by chen on 15/7/15.
 */
public class AccountRecyclerViewAdapter extends RecyclerView.Adapter<AccountRecyclerViewAdapter.AccountsViewHolder> {
    private Context mContext;
    private List<AccountManager.Account> mAccounts;

    public AccountRecyclerViewAdapter(Context context, List<AccountManager.Account> data) {
        mContext = context;
        mAccounts = data;
    }


    public class AccountsViewHolder extends RecyclerView.ViewHolder {
        TextView mAccountNameTextView;
        TextView mDepartmentTextView;

        public AccountsViewHolder(View itemView) {
            super(itemView);
            mAccountNameTextView = (TextView) itemView.findViewById(R.id.account_textView);
            mDepartmentTextView = (TextView) itemView.findViewById(R.id.dept_textView);
            mAccountNameTextView.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC);
            mDepartmentTextView.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC);
        }
    }


    @Override
    public AccountsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflatedView = LayoutInflater.from(mContext).inflate(R.layout.account_view, viewGroup, false);
        return new AccountsViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(AccountsViewHolder viewHolder, int i) {
        AccountManager.Account account = getItem(i);
        viewHolder.mAccountNameTextView.setText(account.getName());
        viewHolder.mDepartmentTextView.setText(GlobalParameters.department[account.getDepartment()]);
    }

    public AccountManager.Account getItem(int position) {
        return mAccounts.get(position);
    }

    @Override
    public int getItemCount() {

        return mAccounts.size();
    }
}
