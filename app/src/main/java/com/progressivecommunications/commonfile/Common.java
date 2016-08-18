package com.progressivecommunications.commonfile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.progressivecommunications.R;
import com.progressivecommunications.activity.Login;

/**
 * Created by twilightuser on 19/7/16.
 */
public class Common {

    public static String ServiceURL = "http://114.69.235.57:9998/procomm";
    //public static String ServiceURL = "http://192.168.0.51:9998/procomm";

    public static double S_LATITUDE =0.0;
    public static double S_LONGITUDE=0.0;


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    // hide the soft keyboard
    public static void hideSoftKeyboard(Context context, View view) {
        InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showAlert(final Context context, final String msg) {

        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        final Dialog alertDialog = new Dialog(context);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        LayoutInflater inflater = getLayoutInflater(getArguments());
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View convertView = (View) inflater.inflate(R.layout.alert, null);
        alertDialog.setContentView(convertView);
        alertDialog.setCanceledOnTouchOutside(false);
        TextView txtContent = (TextView) convertView.findViewById(R.id.txtContent);
        Button btnOk = (Button) convertView.findViewById(R.id.btnOk);
        txtContent.setText(msg);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (msg.equals("Password has been sent to registered email id.")) {
                    Intent in = new Intent(context, Login.class);
                    ((Activity) context).startActivity(in);
                    ((Activity) context).finish();
                }
                /*else if (msg.equals("Password has been changed.")) {

                    Intent in = new Intent(context, Login.class);
                    ((Activity) context).startActivity(in);
                    ((Activity) context).finish();
                }*/

            }
        });


        alertDialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = alertDialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }


}
