package com.progressivecommunications.model;

/**
 * Created by twilightuser on 19/7/16.
 */
public class OnsiteInput {


    public int userid;
    public double latitude;
    public double longitude;
    public int onsite;

    public OnsiteInput(int userid, double latitude, double longitude, int onsite) {
        this.userid = userid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.onsite = onsite;
    }
/*{
        "userid": "2",
            "latitude": "11.9645",
            "longitude": "12.45458",
            "onsite": "0"
    }*/
}
