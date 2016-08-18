package com.progressivecommunications.services;

import android.annotation.SuppressLint;
import android.content.Context;

import com.progressivecommunications.BuildConfig;
import com.progressivecommunications.commonfile.Common;
import com.progressivecommunications.model.LoginInput;
import com.progressivecommunications.model.LoginResponse;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by twilightuser on 19/7/16.
 */
public class LoginRemoteApi {

    private static LoginRemoteApi sInstance = new LoginRemoteApi();

    public static LoginRemoteApi getInstance() {
        return sInstance;
    }

    public static LoginInput getLoginInput() {
        return loginInput;
    }

    public static void setLoginInput(LoginInput loginInput) {
        LoginRemoteApi.loginInput = loginInput;
    }

    public static LoginInput loginInput;

    public void getLoginData(Context context, Callback<LoginResponse> callback)
            throws IllegalStateException {
        // TODO Auto-generated method stub
        // make the service call

        getRemoteApi().getLogin(loginInput, callback);
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
