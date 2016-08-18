package com.progressivecommunications.model;

/**
 * Created by twilightuser on 19/7/16.
 */
public class OnsiteResponse {


    public int status;
    public String UpdatedTime;
    public String message;

    public OnsiteResponse(String message, String updatedTime, int status) {
        this.message = message;
        UpdatedTime = updatedTime;
        this.status = status;
    }
/* {"status":"200",
    "UpdatedTime":"2016-07-27 16:00:25",
    "message":"Updated successfully"}

    */
}
