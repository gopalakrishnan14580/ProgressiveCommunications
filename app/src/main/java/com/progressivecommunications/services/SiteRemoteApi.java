package com.progressivecommunications.services;

import android.annotation.SuppressLint;
import android.content.Context;

import com.progressivecommunications.BuildConfig;
import com.progressivecommunications.commonfile.Common;
import com.progressivecommunications.model.OnsiteInput;
import com.progressivecommunications.model.OnsiteResponse;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by twilightuser on 19/7/16.
 */
public class SiteRemoteApi {

    private static SiteRemoteApi sInstance = new SiteRemoteApi();

    public static SiteRemoteApi getInstance() {
        return sInstance;
    }

    public static OnsiteInput getOnsiteInput() {
        return onsiteInput;
    }

    public static void setOnsiteInput(OnsiteInput onsiteInput) {
        SiteRemoteApi.onsiteInput = onsiteInput;
    }

    public static OnsiteInput onsiteInput;

    public void getUserUpdateData(Context context, Callback<OnsiteResponse> callback)
            throws IllegalStateException {
        // TODO Auto-generated method stub
        // make the service call

        getRemoteApi().getUpdate_user(onsiteInput, callback);
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
