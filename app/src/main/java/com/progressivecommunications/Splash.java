package com.progressivecommunications;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.progressivecommunications.activity.Dashboard;
import com.progressivecommunications.activity.Login;
import com.progressivecommunications.commonfile.PreferenceConnector;

public class Splash extends Activity {

    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {
            public void run() {

                    /*startActivity(new Intent(Splash.this, Login.class));
                    finish();*/

                if(!PreferenceConnector.getEmail(getApplicationContext()).isEmpty()){
                    Intent newIntent = new Intent(getApplicationContext(), Dashboard.class);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(newIntent);
                    finish();

                }else{
                    Intent newIntent = new Intent(getApplicationContext(), Login.class);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(newIntent);
                    finish();
                }
            }
        }, 4000);
    }
}
