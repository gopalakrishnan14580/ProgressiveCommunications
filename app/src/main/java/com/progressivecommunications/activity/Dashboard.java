package com.progressivecommunications.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.progressivecommunications.R;
import com.progressivecommunications.commonfile.Common;
import com.progressivecommunications.commonfile.PreferenceConnector;
import com.progressivecommunications.model.OnsiteInput;
import com.progressivecommunications.model.OnsiteResponse;
import com.progressivecommunications.services.SiteRemoteApi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Dashboard extends Activity implements View.OnClickListener,LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    ImageView settings_icon;
    Button onsite_btn,offsite_btn;
    Context context;
    ProgressDialog pd;
    TextView time,time_cal;
    LinearLayout time_display;


    private SharedPreferences sharedPref;

    double latitude,longitude;

    LocationManager locationManager;

    /*--------------------*/
    private static final String TAG = "Dashboard";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dashboard);
        context = this;
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        createLocationRequest();
        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(AppIndex.API).build();

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        init();

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        mGoogleApiClient.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "dashboard Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.progressivecommunications/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);

    }

   /* @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "dashboard Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.progressivecommunications/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }*/

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
        time_dif();
    }

    /*
    *
    * updateUI();
        time_dif();
        */

    private void updateUI() {
        Log.d(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {


            latitude=mCurrentLocation.getLatitude();
            longitude=mCurrentLocation.getLongitude();

            System.out.println("latitude------------------------>"+latitude);
            System.out.println("longitude----------------------->"+longitude);

            Common.S_LATITUDE=mCurrentLocation.getLatitude();
            Common.S_LONGITUDE=mCurrentLocation.getLongitude();


            try{


                int site_status= PreferenceConnector.getonsite(getApplicationContext());
                int id= PreferenceConnector.getUserId(getApplicationContext());

                if(site_status ==1)
                {

                    update_on_site(id,latitude,longitude,site_status);
                    System.out.println("updated----------------->");

                    //Toast.makeText(Dashboard.this.getApplicationContext(), "Location updated", Toast.LENGTH_LONG).show();
                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            /*
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());
            tvLocation.setText("At Time: " + mLastUpdateTime + "\n" +
                    "Latitude: " + lat + "\n" +
                    "Longitude: " + lng + "\n" +
                    "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" +
                    "Provider: " + mCurrentLocation.getProvider());*/
        } else {
            Log.d(TAG, "location is null ...............");
        }
    }

    /*@Override
    protected void onPause() {
        super.onPause();
        //stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }
*/
    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }

    private void init() {
        settings_icon=(ImageView) findViewById(R.id.settings_icon);
        onsite_btn=(Button) findViewById(R.id.onsite_btn);
        offsite_btn=(Button) findViewById(R.id.offsite_btn);
        time=(TextView) findViewById(R.id.time);
        time_cal=(TextView) findViewById(R.id.time_cal);
        time_display=(LinearLayout) findViewById(R.id.time_display);
        time_display.setVisibility(View.GONE);


        onsite_btn.setOnClickListener(this);
        offsite_btn.setOnClickListener(this);
        settings_icon.setOnClickListener(this);
        sharedPref = getApplicationContext().getSharedPreferences(PreferenceConnector.PREF_NAME,0);

        int site_status=PreferenceConnector.getonsite(getApplicationContext());

        if(site_status ==1)
        {

            onsite_btn.setVisibility(View.VISIBLE);
            offsite_btn.setVisibility(View.GONE);
            time_display.setVisibility(View.VISIBLE);

            time.setText(PreferenceConnector.getLoginTime(getApplicationContext()));


        }

        else{

            offsite_btn.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.settings_icon:
                Intent activityChangeIntent = new Intent(Dashboard.this, Setting.class);
                startActivity(activityChangeIntent);
                break;
            case R.id.onsite_btn:
                call_webservice_offsite();
                break;
            case R.id.offsite_btn:

                call_webservice_onsite();
                break;
        }

    }

    private void call_webservice_onsite()
    {

        if(!checkLocation())
            return;

       /* if(longitude == 0.0 && latitude ==0.0)

        {
            showSettingsAlert();
        }
        else {*/

            pd = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();



            int id= PreferenceConnector.getUserId(getApplicationContext());


            OnsiteInput input = new OnsiteInput(id,latitude,longitude,1);

            SiteRemoteApi.getInstance().setOnsiteInput(input);

            // Call ChangePassword JSON
            SiteRemoteApi.getInstance().getUserUpdateData(context, new Callback<OnsiteResponse>() {
                @Override
                public void success(OnsiteResponse onsiteResponse , Response response) {


                    if (onsiteResponse.status == 200) {
                        onsite_btn.setVisibility(View.VISIBLE);
                        offsite_btn.setVisibility(View.GONE);
                        closeProgress();


                        System.out.println("Response Date :"+onsiteResponse.UpdatedTime);

                        String server_date_time=onsiteResponse.UpdatedTime;


                    /*
                    * Response Time :2016-07-27 18:04:54
                    * */


                        try {

                            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            final Date dateObj = format.parse(server_date_time);


                            System.out.println("Server Time ------------->:"+new SimpleDateFormat("h:mm a").format(dateObj));


                            String loginTime=new SimpleDateFormat("h:mm a").format(dateObj);


                            if(loginTime.contains("pm"))

                                loginTime = loginTime.replace("pm", "PM");

                            else if (loginTime.contains("am"))

                                loginTime = loginTime.replace("am", "AM");

                            time_display.setVisibility(View.VISIBLE);

                            //time.setText(new SimpleDateFormat("h:mm a").format(dateObj));
                            time.setText(loginTime);

                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("loginTime",loginTime);
                            editor.putInt("onSite", 1);
                            editor.putString("serverTime", server_date_time);

                            editor.commit();


                        } catch (final ParseException e) {
                            e.printStackTrace();
                        }


                    } else {
                        Common.showAlert(context, onsiteResponse.message);
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


       // }



    }



    private void call_webservice_offsite()
    {
        System.out.println("latitude :"+latitude);
        System.out.println("longitude :"+longitude);



        if(!checkLocation())
            return;

        /*if(longitude == 0.0 && latitude ==0.0)

        {
            showSettingsAlert();
        }
        else {*/

            pd = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();

            int id= PreferenceConnector.getUserId(getApplicationContext());

            OnsiteInput input = new OnsiteInput(id,latitude, longitude,0);

            SiteRemoteApi.getInstance().setOnsiteInput(input);

            // Call ChangePassword JSON
            SiteRemoteApi.getInstance().getUserUpdateData(context, new Callback<OnsiteResponse>() {
                @Override
                public void success(OnsiteResponse onsiteResponse , Response response) {


                    if (onsiteResponse.status == 200) {


                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("onSite", 0);
                        editor.putString("serverTime", "");
                        editor.putString("loginTime", "");
                        editor.commit();


                        onsite_btn.setVisibility(View.GONE);
                        offsite_btn.setVisibility(View.VISIBLE);
                        time_display.setVisibility(View.GONE);

                        closeProgress();
                        // Common.showAlert(context, forgotpasswordResponse.message);

                    } else {
                        Common.showAlert(context, onsiteResponse.message);
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
      //  }



    }



    private void time_dif()
    {


        try {

            //SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


            /*
                    * Response Time :2016-07-27 18:04:54
                    * */

        String dateStart = PreferenceConnector.getServerTime(getApplication());

        Date date = new Date();

        String  dateStop = format.format(date);

        /*System.out.println(DateToStr);
        String dateStop = DateToStr;*/

        //HH converts hour in 24 hours format (0-23), day calculation
       // SimpleDateFormat format = new SimpleDateFormat("HH:mm a");

            Date d1 = null;
            Date d2 = null;

            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            long st1= (3 * 1000)%60;

            System.out.println("st1fjd--------->"+st1);



            long str1= diffMinutes + 3;

            System.out.println("sreererer--------->"+str1);

            //System.out.print(diffDays + " days, ");
            System.out.print(diffHours + " hours, ");
            System.out.print(diffMinutes + " minutes, ");

            if(str1==0)
            {
                time_cal.setText(String.valueOf(" ( a moment ago)"));
            }


             if (str1 >0 && str1 < 60 && diffHours == 0)
            {
                time_cal.setText(String.valueOf(" ("+str1+" minutes ago)"));

            }


            if(diffHours >0){

                time_cal.setText(String.valueOf(" ("+diffHours+" hours ago)"));
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void update_on_site(int id,double latitude,double longitude,int site)
    {

        OnsiteInput input = new OnsiteInput(id,latitude,longitude,site);

        SiteRemoteApi.getInstance().setOnsiteInput(input);

        // Call ChangePassword JSON
        SiteRemoteApi.getInstance().getUserUpdateData(context, new Callback<OnsiteResponse>() {
            @Override
            public void success(OnsiteResponse onsiteResponse , Response response) {

                        /*      */
            }

            @Override
            public void failure(RetrofitError error) {
               // closeProgress();
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

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showSettingsAlert();
        return isLocationEnabled();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void showSettingsAlert(){


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
