package com.progressivecommunications.services;

import android.annotation.SuppressLint;
import android.content.Context;

import com.progressivecommunications.BuildConfig;
import com.progressivecommunications.commonfile.Common;
import com.progressivecommunications.model.ChangePasswordInput;
import com.progressivecommunications.model.ForgotpasswordResponse;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by twilightuser on 19/7/16.
 */
public class ChangePasswordRemoteApi {

    private static ChangePasswordRemoteApi sInstance = new ChangePasswordRemoteApi();

    public static ChangePasswordRemoteApi getInstance() {
        return sInstance;
    }

    public static ChangePasswordInput getChangePasswordInput() {
        return changePasswordInput;
    }

    public static void setChangePasswordInput(ChangePasswordInput changePasswordInput) {
        ChangePasswordRemoteApi.changePasswordInput = changePasswordInput;
    }

    public static ChangePasswordInput changePasswordInput;

    public void getChangePasswordData(Context context, Callback<ForgotpasswordResponse> callback)
            throws IllegalStateException {
        // TODO Auto-generated method stub
        // make the service call

        getRemoteApi().getChange_password(changePasswordInput, callback);
    }

    private RemoteApi mRemoteApi;

    private RemoteApi getRemoteApi() {
        if (mRemoteApi == null) {

            mRemoteApi = create();
        }
        return mRemoteApi;
    }

    @SuppressLint("NewApi")
    private RemoteApi create() throws IllegalStateException {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(Common.ServiceURL);

        if (BuildConfig.DEBUG) {
            builder.setLogLevel((RestAdapter.LogLevel.FULL));
        }

        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {

            }
        });
        OkHttpClient client = new OkHttpClient();
        builder.setClient(new OkClient(client));
        RestAdapter adapter = builder.build();

        return adapter.create(RemoteApi.class);
    }
}
