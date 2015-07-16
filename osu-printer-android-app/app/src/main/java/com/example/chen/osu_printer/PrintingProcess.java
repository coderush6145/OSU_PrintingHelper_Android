package com.example.chen.osu_printer;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

/**
 * Created by chen on 15/7/15.
 */
public class PrintingProcess extends AsyncTask<PrintConfigManager, String, String> {

    Activity context;

    PrintingProcess(Activity context){
        super();
        this.context = context;
    }

    protected String doInBackground(PrintConfigManager... args) { //parameters needed
        try {
            final PrintConfigManager config = args[0];

            //establish ssh connection
            String username = "zhante";
            String password = "zt1993@CSE";
            String ipAddress = "gamma.cse.ohio-state.edu";
            String defaultFolder = config.defaultFolder;

            SSHManager instance = new SSHManager(username, password, ipAddress, "");
            final String errorMessage = instance.connect();
            if(errorMessage != null)
            {
                context.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
                return null;
            }

            //make the default folder for storing files
            instance.sendCommand("mkdir " + defaultFolder);



            Log.e("stack??", makeCommand(config));

            context.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(context, makeCommand(config), Toast.LENGTH_LONG).show();
                }
            });

            instance.sendCommand(makeCommand(config));
            //upload file
            try {
                Session sesConnection = instance.getSession();
                Channel channel = sesConnection.openChannel("sftp");
                channel.connect();
                ChannelSftp sftpChannel = (ChannelSftp) channel;
                sftpChannel.cd(defaultFolder);
                for (FileObject _f : config.getToPrintFiles())
                    sftpChannel.put(_f.getFilePath(), ".");

            } catch (Exception e) {
                Log.e("stack??",Log.getStackTraceString(e));
                System.exit(0);
            }


            //close ssh connection
            instance.close();

            context.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(context, R.string.succeed_message, Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Error", e.getMessage());
            return null;
        }

        return null;
    }

    public String makeCommand(PrintConfigManager config){
        String ret = "lp ";

        for (FileObject _f: config.getToPrintFiles())
            ret = ret + _f.getFilePath() + " ";

        if (config.isDuplex())
            ret += "-o two-sided-long-edge ";

        ret += "-n " + String.valueOf(config.getCopies()) + " ";
        return ret;
    }




}
