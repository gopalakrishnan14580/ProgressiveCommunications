package com.progressivecommunications.model;

/**
 * Created by twilightuser on 19/7/16.
 */
public class ForgotpasswordResponse {

    public int status;
    public String message;

    public ForgotpasswordResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

/* {
        "status": "200",
            "message": "Password has been sent to registered email id."
    }*/
}
