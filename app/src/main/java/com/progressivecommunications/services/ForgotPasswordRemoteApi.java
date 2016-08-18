package com.progressivecommunications.services;

import android.annotation.SuppressLint;
import android.content.Context;

import com.progressivecommunications.BuildConfig;
import com.progressivecommunications.commonfile.Common;
import com.progressivecommunications.model.ForgotpasswordInput;
import com.progressivecommunications.model.ForgotpasswordResponse;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by twilightuser on 19/7/16.
 */
public class ForgotPasswordRemoteApi {

    private static ForgotPasswordRemoteApi sInstance = new ForgotPasswordRemoteApi();

    public static ForgotPasswordRemoteApi getInstance() {
        return sInstance;
    }

    public static ForgotpasswordInput getForgotpasswordInput() {
        return forgotpasswordInput;
    }

    public static void setForgotpasswordInput(ForgotpasswordInput forgotpasswordInput) {
        ForgotPasswordRemoteApi.forgotpasswordInput = forgotpasswordInput;
    }

    public static ForgotpasswordInput forgotpasswordInput;

    public void getForgotPasswordData(Context context, Callback<ForgotpasswordResponse> callback)
            throws IllegalStateException {
        // TODO Auto-generated method stub
        // make the service call

        getRemoteApi().getForgot_password(forgotpasswordInput, callback);
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
