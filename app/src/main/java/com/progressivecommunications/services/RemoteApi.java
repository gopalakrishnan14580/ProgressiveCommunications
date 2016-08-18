package com.progressivecommunications.services;

import com.progressivecommunications.model.ChangePasswordInput;
import com.progressivecommunications.model.ForgotpasswordInput;
import com.progressivecommunications.model.ForgotpasswordResponse;
import com.progressivecommunications.model.LoginInput;
import com.progressivecommunications.model.LoginResponse;
import com.progressivecommunications.model.OnsiteInput;
import com.progressivecommunications.model.OnsiteResponse;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by twilightuser on 19/7/16.
 */
public interface RemoteApi {

    @POST("/check-email")
    public void getLogin(@Body LoginInput loginResponse,
                         Callback<LoginResponse> callback);

    @POST("/forgot-password")
    public void getForgot_password(@Body ForgotpasswordInput forgotResponse,
                         Callback<ForgotpasswordResponse> callback);

    @POST("/change-password")
    public void getChange_password(@Body ChangePasswordInput changepwdResponse,
                                   Callback<ForgotpasswordResponse> callback);

    @POST("/update-user")
    public void getUpdate_user(@Body OnsiteInput onsiteResponse,
                                   Callback<OnsiteResponse> callback);
}
