package com.clubee.doggywalker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by barcat on 7/13/15.
 */

public class DoggieWalker extends Activity {

    private TextView textView;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doggiewalker);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        String nome = extras.getString("nome");
        String pic = extras.getString("picURI");

        textView = (TextView) findViewById(R.id.txtView);
        textView.setText("Olá, " + nome + "!");

        new SetImageURI().execute(pic);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private class SetImageURI extends AsyncTask<String, Void, Drawable> {

        @Override
        protected Drawable doInBackground(String... params) {
            Drawable dr = null;
            String exc = null;
            try {
                URL url = new URL(params[0]);
                InputStream img = (InputStream) url.getContent();
                dr = Drawable.createFromStream(img, "src");
            } catch (Exception e) {
                exc = e.toString();
                Toast.makeText(DoggieWalker.this, "Ocorreu um problema", Toast.LENGTH_SHORT).show();
            }
            return dr;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            ImageButton imgView = (ImageButton) findViewById(R.id.imgPerfil);
            imgView.setImageDrawable(result);
        }
    }

}
