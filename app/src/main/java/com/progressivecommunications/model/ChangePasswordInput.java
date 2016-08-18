package com.progressivecommunications.model;

/**
 * Created by twilightuser on 19/7/16.
 */
public class ChangePasswordInput {


    public int userid;
    public String oldpwd;
    public String newpwd;

    public ChangePasswordInput(int userid, String oldpwd, String newpwd) {
        this.userid = userid;
        this.oldpwd = oldpwd;
        this.newpwd = newpwd;
    }

    /*
     "userid": "2",
        "oldpwd": "123@ram",
        "newpwd": "ram@123"*/
}
