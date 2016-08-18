package com.progressivecommunications.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class ButtonAgencyregular extends Button{


    Context mContext;
    static String textFont = "AGENCYR.TTF";

    public ButtonAgencyregular(Context context) {
        super(context);
        this.mContext = context;
        inite();
    }

    public ButtonAgencyregular(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        inite();
    }

    public ButtonAgencyregular(Context context, AttributeSet attrs, int defStyleAttr) {
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



