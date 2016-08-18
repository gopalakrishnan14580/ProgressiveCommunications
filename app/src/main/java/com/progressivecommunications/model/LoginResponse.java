package com.progressivecommunications.model;

/**
 * Created by twilightuser on 19/7/16.
 */
public class LoginResponse {

    public int id;
    public String name;
    public String email;
    public int pwdreset;
    public int status;
    public String message;

    public LoginResponse(int id, String name, String email, int pwdreset, int status, String message) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.pwdreset = pwdreset;
        this.status = status;
        this.message = message;
    }

/*Success:

    {
        "id": "3",
            "name": "Shiva",
            "email": "shiva@yopmail.com",
            "pwdreset": "0",
            "status": "200",
            "message": "User exist"
    }*/

}
