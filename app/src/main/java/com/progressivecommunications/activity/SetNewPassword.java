package com.progressivecommunications.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.progressivecommunications.R;
import com.progressivecommunications.commonfile.Common;
import com.progressivecommunications.commonfile.PreferenceConnector;
import com.progressivecommunications.model.ChangePasswordInput;
import com.progressivecommunications.model.ForgotpasswordResponse;
import com.progressivecommunications.services.ChangePasswordRemoteApi;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SetNewPassword extends Activity implements View.OnClickListener {

    private EditText new_password_et,confirm_password_et;
    Button pwd_submit_btn;
    String new_password ,confirm_password;
    Context context;
    ProgressDialog pd;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.setnewpassword);
        context = this;
        init();
    }

    private void init() {
        new_password_et=(EditText) findViewById(R.id.new_password_et);
        confirm_password_et=(EditText) findViewById(R.id.confirm_password_et);
        pwd_submit_btn=(Button) findViewById(R.id.pwd_submit_btn);

        sharedPref = getApplicationContext().getSharedPreferences(PreferenceConnector.PREF_NAME,0);

        confirm_password_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    confirm_password_et.setCursorVisible(false);

                }
                return false;
            }
        });

        pwd_submit_btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.pwd_submit_btn:
                Common.hideSoftKeyboard(context, v);
                getUIValues();
                validation();
                break;
        }

    }


    private void getUIValues() {
        new_password = new_password_et.getText().toString().trim();
        confirm_password = confirm_password_et.getText().toString().trim();
    }

    private void validation() {

        if (new_password.length() == 0) {

            Common.showAlert(context,"New password");

        } else if (confirm_password.length() == 0) {
            Common.showAlert(context,"Confirm Password ");
        }
        else if (new_password.length() < 6 || new_password.length() > 20) {

            Common.showAlert(context,"Password must be minimum 6 characters long");
        }
        else if (confirm_password.length() < 6 || confirm_password.length() > 20) {

            Common.showAlert(context,"Password must be minimum 6 characters long");
        }else {

            if (new_password.equals(confirm_password)) {
                boolean status = Common.isNetworkAvailable(context);
                if (status) {
                    change_password_Retrofit();
                } else {
                    Common.showAlert(context, getResources().getString(R.string.no_internet));
                }
            } else {

                new_password_et.setText("");
                confirm_password_et.setText("");
                Common.showAlert(context, "Please did not match");

            }

        }

    }

    private void change_password_Retrofit() {
        // TODO Auto-generated method stub

        // Progress Dialog
        pd = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        int id= PreferenceConnector.getUserId(getApplicationContext());
        String old_passwprd= PreferenceConnector.getOldPassword(getApplicationContext());


        ChangePasswordInput input = new ChangePasswordInput(id,old_passwprd, confirm_password);

        ChangePasswordRemoteApi.getInstance().setChangePasswordInput(input);

        // Call ChangePassword JSON
        ChangePasswordRemoteApi.getInstance().getChangePasswordData(context, new Callback<ForgotpasswordResponse>() {
            @Override
            public void success(ForgotpasswordResponse loginResponse, Response response) {
                closeProgress();

                if (loginResponse.status == 200) {

                   /* SharedPreferences.Editor editor = sharedPref.edit();

                    editor.putString("oldpassword", confirm_password);

                    editor.commit();*/

                    SharedPreferences sharedPref; //Initialize the SharedPreference class
                    SharedPreferences.Editor editor;
                    sharedPref = getApplicationContext().getSharedPreferences(PreferenceConnector.PREF_NAME,0); //we can use same Reference key or dynamic key
                    editor = sharedPref.edit();

                    editor.clear();
                    editor.commit();

                    showAlert1(context, loginResponse.message);


                } else {
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

    private void closeProgress() {
        // TODO Auto-generated method stub
        if (pd.isShowing())
            pd.cancel();
    }

    public static void showAlert1(final Context context, final String msg) {

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

                 if (msg.equals("Password has been changed.")) {

                     Intent in = new Intent(context, Login.class);
                     ((Activity) context).startActivity(in);
                     ((Activity) context).finishAffinity();
                }

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
