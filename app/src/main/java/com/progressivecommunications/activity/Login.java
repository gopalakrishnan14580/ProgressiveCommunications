package com.progressivecommunications.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.progressivecommunications.R;
import com.progressivecommunications.commonfile.Common;
import com.progressivecommunications.commonfile.PreferenceConnector;
import com.progressivecommunications.model.ForgotpasswordInput;
import com.progressivecommunications.model.ForgotpasswordResponse;
import com.progressivecommunications.model.LoginInput;
import com.progressivecommunications.model.LoginResponse;
import com.progressivecommunications.services.ForgotPasswordRemoteApi;
import com.progressivecommunications.services.LoginRemoteApi;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Login extends Activity implements View.OnClickListener {

    private EditText user_name_et,password_et,forgot_email_et;
    CheckBox checkRem;
    TextView txtFPassword;
    Button submit_btn;
    String UserName,Password,forgot_email;
    Context context;
    ProgressDialog pd;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);
        context = this;
        init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 2909);
            } /*else {
                // continue with your code

            }*/
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2909: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");
                } else {
                    Log.e("Permission", "Denied");
                }
                return;
            }
        }
    }

    private void init() {

        user_name_et=(EditText) findViewById(R.id.user_name_et);
        password_et=(EditText) findViewById(R.id.password_et);
        checkRem=(CheckBox) findViewById(R.id.chkRem);
        txtFPassword=(TextView) findViewById(R.id.txtFPassword);
        submit_btn=(Button) findViewById(R.id.submit_btn);



        sharedPref = getApplicationContext().getSharedPreferences(PreferenceConnector.PREF_NAME,0);



        password_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    password_et.setCursorVisible(false);

                }
                return false;
            }
        });


        password_et.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // request your webservice here. Possible use of AsyncTask and ProgressDialog
                // show the result here - dialog or Toast
                password_et.setCursorVisible(true);
            }

        });


        txtFPassword.setOnClickListener(this);
        submit_btn.setOnClickListener(this);

        SharedPreferences sh_Pref = context.getSharedPreferences(
                "LoginUserName", Context.MODE_PRIVATE);
        UserName = sh_Pref.getString("UserName", "");
        Password = sh_Pref.getString("Password", "");

        if (Password.length() != 0) {
            checkRem.setChecked(true);
        }

        user_name_et.setText(UserName);
        password_et.setText(Password);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_btn:
                Common.hideSoftKeyboard(context, v);
                getUIValues();
                validation();
                break;
            case R.id.txtFPassword:
                forgotPassword();
                break;
        }
    }

    private void getUIValues() {
        UserName = user_name_et.getText().toString().trim();
        Password = password_et.getText().toString().trim();
    }

    private void validation() {

        if (UserName.length() == 0) {
            Common.showAlert(context, "Username");

        } else if (Password.length() == 0) {
            Common.showAlert(context, "Password");

        }
        else if (Password.length() < 6 || Password.length() > 20) {

            password_et.setText("");
            Common.showAlert(context, "Password must be minimum 6 characters long");
        } else {

            boolean status = Common.isNetworkAvailable(context);
            if (status) {
                loginRetrofit();
            } else {
                Common.showAlert(context, getResources().getString(R.string.no_internet));
            }
        }

    }

    private void SaveSharedValues(LoginResponse loginResponse) {



        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("userId", loginResponse.id);
        editor.putString("email", loginResponse.email);
        editor.putString("oldpassword", Password);

        editor.commit();


        if (checkRem.isChecked()) {

            SharedPreferences sh_Pref1 = getSharedPreferences(
                    "LoginUserName", MODE_PRIVATE);
            SharedPreferences.Editor toEdit1;

            toEdit1 = sh_Pref1.edit();
            toEdit1.putString("UserName", UserName);
            toEdit1.putString("Password", Password);
            toEdit1.commit();


            if (loginResponse.pwdreset == 0)
            {
                Intent intent = new Intent(context, SetNewPassword.class);
                startActivity(intent);
                finish();

            }
            else{
                Intent intent = new Intent(context, Dashboard.class);
                startActivity(intent);
                finish();
            }

        } else {
            SharedPreferences settings1 = getSharedPreferences("LoginUserName", 0);
            SharedPreferences.Editor editor1 = settings1.edit();
            editor1.clear();
            editor1.commit();


            if (loginResponse.pwdreset == 0)
            {
                Intent intent = new Intent(context, SetNewPassword.class);
                startActivity(intent);
                finish();

            }
            else{
                Intent intent = new Intent(context, Dashboard.class);
                startActivity(intent);
                finish();
            }

        }

    }

    private void forgotPassword()
    {
        final Dialog alertDialog = new Dialog(context);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = (View) inflater.inflate(R.layout.forgot_password, null);
        alertDialog.setContentView(convertView);
        alertDialog.setCanceledOnTouchOutside(false);

        forgot_email_et = (EditText) convertView.findViewById(R.id.forgot_email_et);



        Button done = (Button) convertView.findViewById(R.id.done_btn);


        done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // update has been clicked
                alertDialog.dismiss();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);

                forgot_email =forgot_email_et.getText().toString();

                if (forgot_email.length() == 0)
                {
                    forgot_email_et.setHintTextColor(getResources().getColor(R.color.red));
                    forgot_email_et.setHint("Email");
                    //Common.showAlert(context, "Email");
                }

                else {
                    InputMethodManager immm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    immm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);

                    boolean status = Common.isNetworkAvailable(context);
                    if (status) {
                        forgot_Password();
                    } else {
                        Common.showAlert(context, getResources().getString(R.string.no_internet));
                    }
                }



            }
        });


        alertDialog.show();

        forgot_email_et.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = alertDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    private void loginRetrofit() {
        // TODO Auto-generated method stub

        // Progress Dialog
        pd = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();


        LoginInput input = new LoginInput(UserName, Password);

        LoginRemoteApi.getInstance().setLoginInput(input);

        // Call Login JSON
        LoginRemoteApi.getInstance().getLoginData(context, new Callback<LoginResponse>() {
            @Override
            public void success(LoginResponse loginResponse, Response response) {
                closeProgress();

                if (loginResponse.status == 200) {

                    SaveSharedValues(loginResponse);

                }
                else  {

                        Common.showAlert(context, loginResponse.message);

                }


            }

            @Override
            public void failure(RetrofitError error) {
                closeProgress();
                if (error.isNetworkError()) {
                    Common.showAlert(context, getResources().getString(R.string.no_internet));
                } else {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void forgot_Password() {
        // TODO Auto-generated method stub

        // Progress Dialog
        pd = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        // Send the forgot_email

        ForgotpasswordInput input = new ForgotpasswordInput(forgot_email);

        ForgotPasswordRemoteApi.getInstance().setForgotpasswordInput(input);

        // Call Forgot Password JSON
        ForgotPasswordRemoteApi.getInstance().getForgotPasswordData(context, new Callback<ForgotpasswordResponse>() {
            @Override
            public void success(ForgotpasswordResponse forgotpasswordResponse, Response response) {
                closeProgress();

                if (forgotpasswordResponse.status == 200) {

                    Common.showAlert(context, forgotpasswordResponse.message);

                } else {
                    Common.showAlert(context, forgotpasswordResponse.message);
                }


            }

            @Override
            public void failure(RetrofitError error) {
                closeProgress();
                if (error.isNetworkError()) {
                    Common.showAlert(context, getResources().getString(R.string.no_internet));
                } else {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void closeProgress() {
        // TODO Auto-generated method stub
        if (pd.isShowing())
            pd.cancel();
    }

}
