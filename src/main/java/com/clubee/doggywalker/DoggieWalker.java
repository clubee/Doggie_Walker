package com.clubee.doggywalker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.facebook.CallbackManager;

/**
 * Created by barcat on 7/13/15.
 */

public class DoggieWalker extends Activity {

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doggiewalker);



    }
}
