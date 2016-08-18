package com.progressivecommunications.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class Buttonebrima extends Button{


    Context mContext;
    static String textFont = "ebrima_0.ttf";

    public Buttonebrima(Context context) {
        super(context);
        this.mContext = context;
        inite();
    }

    public Buttonebrima(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        inite();
    }

    public Buttonebrima(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        inite();
    }

    private void inite() {
        if (!isInEditMode()) {
            if(textFont == "" ||textFont == null){

            }else {
                this.setTypeface(Typeface.createFromAsset(mContext.getAssets(), textFont));
            }
        }
    }
}



