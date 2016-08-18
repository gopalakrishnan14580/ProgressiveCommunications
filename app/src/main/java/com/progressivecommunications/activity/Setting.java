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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.progressivecommunications.R;
import com.progressivecommunications.commonfile.Common;
import com.progressivecommunications.commonfile.PreferenceConnector;
import com.progressivecommunications.model.ChangePasswordInput;
import com.progressivecommunications.model.ForgotpasswordResponse;
import com.progressivecommunications.model.OnsiteInput;
import com.progressivecommunications.model.OnsiteResponse;
import com.progressivecommunications.services.ChangePasswordRemoteApi;
import com.progressivecommunications.services.SiteRemoteApi;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Setting extends Activity implements View.OnClickListener {

    private TextView change_password,logout,background_line,background_line1,background_line2,
            background_line3,background_line4;
    private EditText current_password,new_password,con_new_password;
    private Button set_submit_btn;
    private ImageView change_password_down,change_password_up,settings_back;
    Context context;
    ProgressDialog pd;
    String cur_pwd,new_pwd,con_new_pwd;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        context = this;
            init();
    }

    private void init() {

        change_password=(TextView) findViewById(R.id.change_password);
        logout=(TextView) findViewById(R.id.logout);
        background_line=(TextView) findViewById(R.id.background_line);
        background_line1=(TextView) findViewById(R.id.background_line1);
        background_line2=(TextView) findViewById(R.id.background_line2);
        background_line3=(TextView) findViewById(R.id.background_line3);
        background_line4=(TextView) findViewById(R.id.background_line4);

        current_password=(EditText) findViewById(R.id.current_password);
        new_password=(EditText) findViewById(R.id.new_password);
        con_new_password=(EditText) findViewById(R.id.con_new_password);

        set_submit_btn=(Button) findViewById(R.id.set_submit_btn);

        change_password_down=(ImageView) findViewById(R.id.change_password_down);
        change_password_up=(ImageView) findViewById(R.id.change_password_up);
        settings_back=(ImageView) findViewById(R.id.settings_back);


       sharedPref = getApplicationContext().getSharedPreferences(PreferenceConnector.PREF_NAME,0);

        con_new_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    con_new_password.setCursorVisible(false);

                }
                return false;
            }
        });

        con_new_password.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // request your webservice here. Possible use of AsyncTask and ProgressDialog
                // show the result here - dialog or Toast
                con_new_password.setCursorVisible(true);
            }

        });

        settings_back.setOnClickListener(this);
        set_submit_btn.setOnClickListener(this);
        logout.setOnClickListener(this);
        change_password_down.setOnClickListener(this);
        change_password_up.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_submit_btn:
                getUIValues();
                validation();
                break;
            case R.id.logout:
                logout();
                break;
            case R.id.settings_back:
                finish();
                break;
            case R.id.change_password_down:
                getUIValuesVisible();
                break;
            case R.id.change_password_up:
                getUIValuesInVisible();
                break;
        }

    }

    private void logout() {


        final Dialog alertDialog = new Dialog(context);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = (View) inflater.inflate(R.layout.alert, null);
        alertDialog.setContentView(convertView);
        alertDialog.setCanceledOnTouchOutside(false);
        TextView txtHeader = (TextView) convertView.findViewById(R.id.txtHeader);
        TextView txtContent = (TextView) convertView.findViewById(R.id.txtContent);
        Button btnCancel = (Button) convertView.findViewById(R.id.btnCancel);
        Button btnOk = (Button) convertView.findViewById(R.id.btnOk);
        View view = (View) convertView.findViewById(R.id.viewSep);
        btnCancel.setVisibility(View.VISIBLE);
        view.setVisibility(View.VISIBLE);
        txtHeader.setVisibility(View.GONE);
        txtContent.setVisibility(View.VISIBLE);

        txtContent.setText("Are you sure you want to logout?");
        btnOk.setText("Yes");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                logout_off_site();

                Intent intent = new Intent(context, Login.class);
                startActivity(intent);
                finishAffinity();

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

    private void getUIValues() {

        cur_pwd=current_password.getText().toString().trim();
        new_pwd=new_password.getText().toString().trim();
        con_new_pwd=con_new_password.getText().toString().trim();

    }

    private void validation() {

        if (cur_pwd.length() == 0) {
            Common.showAlert(context,getResources().getString(R.string.current_password));

        } else if (new_pwd.length() == 0) {
            Common.showAlert(context,getResources().getString(R.string.new_password));

        } else if (con_new_pwd.length() == 0) {
            Common.showAlert(context,getResources().getString(R.string.con_password));

        }
        else if (cur_pwd.length() < 6 || cur_pwd.length() > 20) {

            Common.showAlert(context,"Password must be minimum 6 characters long");
        }
        else if (new_pwd.length() < 6 || new_pwd.length() > 20) {

            Common.showAlert(context,"Password must be minimum 6 characters long");
        }
        else if (con_new_pwd.length() < 6 || con_new_pwd.length() > 20) {

            Common.showAlert(context,"Password must be minimum 6 characters long");
        }else  {

            if (!cur_pwd.equals(new_pwd)) {

                if (new_pwd.equals(con_new_pwd)) {
                    boolean status = Common.isNetworkAvailable(context);
                    if (status) {
                        change_pwd_Retrofit();
                    } else {
                        Common.showAlert(context, getResources().getString(R.string.no_internet));
                    }
                } else {
                    current_password.setText("");
                    new_password.setText("");
                    con_new_password.setText("");

                    Common.showAlert(context, "New password and confirm password does not match");

                }
            } else {
                current_password.setText("");
                new_password.setText("");
                con_new_password.setText("");

                Common.showAlert(context, "Current password and New password are same");


            }
        }

    }

    private void getUIValuesVisible() {

        background_line.setVisibility(View.GONE);
        background_line1.setVisibility(View.VISIBLE);
        background_line2.setVisibility(View.VISIBLE);
        background_line3.setVisibility(View.VISIBLE);
        background_line4.setVisibility(View.VISIBLE);
        current_password.setVisibility(View.VISIBLE);
        new_password.setVisibility(View.VISIBLE);
        con_new_password.setVisibility(View.VISIBLE);
        change_password_down.setVisibility(View.GONE);
        change_password_up.setVisibility(View.VISIBLE);
        set_submit_btn.setVisibility(View.VISIBLE);
    }

    private void getUIValuesInVisible() {

        background_line.setVisibility(View.VISIBLE);
        background_line1.setVisibility(View.GONE);
        background_line2.setVisibility(View.GONE);
        background_line3.setVisibility(View.GONE);
        background_line4.setVisibility(View.GONE);
        set_submit_btn.setVisibility(View.GONE);
        current_password.setVisibility(View.GONE);
        new_password.setVisibility(View.GONE);
        con_new_password.setVisibility(View.GONE);
        change_password_down.setVisibility(View.VISIBLE);
        change_password_up.setVisibility(View.GONE);

    }

    private void change_pwd_Retrofit() {

        // TODO Auto-generated method stub

        // Progress Dialog
        pd = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();


        int id= PreferenceConnector.getUserId(getApplicationContext());

        System.out.println("old_password:"+cur_pwd);
        System.out.println("new_password:"+con_new_pwd);

        ChangePasswordInput input = new ChangePasswordInput(id,cur_pwd, con_new_pwd);

        ChangePasswordRemoteApi.getInstance().setChangePasswordInput(input);

        // Call ChangePassword JSON
        ChangePasswordRemoteApi.getInstance().getChangePasswordData(context, new Callback<ForgotpasswordResponse>() {
            @Override
            public void success(ForgotpasswordResponse loginResponse, Response response) {
                closeProgress();

                if (loginResponse.status == 200) {

                    SharedPreferences.Editor editor = sharedPref.edit();

                    editor.putString("oldpassword", con_new_pwd);

                    editor.commit();

                    Common.showAlert(context, loginResponse.message);

                    current_password.setText("");
                    new_password.setText("");
                    con_new_password.setText("");

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


    private void logout_off_site()
    {

        int id= PreferenceConnector.getUserId(getApplicationContext());
        OnsiteInput input = new OnsiteInput(id,Common.S_LATITUDE, Common.S_LONGITUDE,0);

        SiteRemoteApi.getInstance().setOnsiteInput(input);

        // Call ChangePassword JSON
        SiteRemoteApi.getInstance().getUserUpdateData(context, new Callback<OnsiteResponse>() {
            @Override
            public void success(OnsiteResponse onsiteResponse, Response response) {


                SharedPreferences sharedPref; //Initialize the SharedPreference class
                SharedPreferences.Editor editor;
                sharedPref = getApplicationContext().getSharedPreferences(PreferenceConnector.PREF_NAME,0); //we can use same Reference key or dynamic key
                editor = sharedPref.edit();

                editor.clear();
                editor.commit();


            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


    }

    private void closeProgress() {
        // TODO Auto-generated method stub
        if (pd.isShowing())
            pd.cancel();
    }


}
