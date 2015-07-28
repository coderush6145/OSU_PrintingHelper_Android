package com.chen.OSU_Printing_Helper;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

import java.util.ArrayList;

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
            String username = AccountManager.getInstance().getRunningAccount().getUsername();
            String password = AccountManager.getInstance().getRunningAccount().getPassword();
            String ipAddress = GlobalParameters.departmentServerIp[AccountManager.getInstance().getRunningAccount().getDepartment()];  //need to be changed
            String defaultFolder = config.defaultFolder;

            SSHManager instance = new SSHManager(username, password, ipAddress, "");
            context.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(context, "Connecting...", Toast.LENGTH_LONG).show();
                }
            });
            final String errorMessage = instance.connect();


            if(errorMessage != null)
            {
                context.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context, "Failed when trying to talk to server. Please recheck your accounts and network connection", Toast.LENGTH_LONG).show();
                    }
                });
                return null;
            } else{
                context.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context, "File uploading...", Toast.LENGTH_LONG).show();
                    }
                });
            }

            //make the default folder for storing files
            instance.sendCommand("mkdir " + defaultFolder);


            //upload file
            try {
                Session sesConnection = instance.getSession();
                Channel channel = sesConnection.openChannel("sftp");
                channel.connect();

                ChannelSftp sftpChannel = (ChannelSftp) channel;
                sftpChannel.cd(defaultFolder);

                for (FileObject _f : config.getToPrintFiles()) {


                    Log.d("Uploading", _f.getFilePath());

                    sftpChannel.put(_f.getFilePath(), ".");
                }

                channel.disconnect();

            } catch (Exception e) {
                Log.e("stackTrace", Log.getStackTraceString(e));
                System.exit(0);
            }

            Log.d("Other processing", "");

            ArrayList<String> nonPdfFilesName = new ArrayList<String>();
            for (FileObject i: config.getToPrintFiles()) {
                if (i.getFileType() != "pdf") {

                    nonPdfFilesName.add(i.getFileName());
                    instance.sendCommand(makeConvertCommand(config, i));
                    i.setFilePath(i.getFilePath().substring(0, i.getFilePath().lastIndexOf(".")) + ".pdf");
                }
            }


            instance.sendCommand(makePrintCommand(config));
            instance.sendCommand(makeDeleteCommand(config, nonPdfFilesName));


            Log.d("Print Command", makePrintCommand(config));
            Log.d("Delete Command", makeDeleteCommand(config, nonPdfFilesName));

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

    public String makePrintCommand(PrintConfigManager config){
        String ret = "lp ";
        ret += "-d " + PrintConfigManager.getInstance().getPrinter().getPrinterName() + " ";


        if (config.isDuplex())
            ret += "-o sides=two-sided-long-edge ";

        ret += "-n " + String.valueOf(config.getCopies()) + " ";


        for (FileObject _f: config.getToPrintFiles())
            ret = ret + config.defaultFolder + "/" +  "\"" +_f.getFileName() + "\" ";

        return ret;
    }

    public String makeDeleteCommand(PrintConfigManager config, ArrayList<String> otherTypeFIles){
        String ret = "rm ";

        for (FileObject _f: config.getToPrintFiles())
            ret = ret + config.defaultFolder + "/" +  "\"" +_f.getFileName() + "\" ";
        for (String f: otherTypeFIles)
            ret = ret + config.defaultFolder + "/" +  "\"" + f + "\" ";

        return ret;
    }

    public String makeConvertCommand(PrintConfigManager config, FileObject f){

        String ret = "cd " + config.defaultFolder+ "; " + "oowriter -convert-to pdf:writer_pdf_Export ";
        ret = ret +  "\"" +f.getFileName() + "\" ";

        return ret;
    }




}
