package com.clubee.doggywalker;

        import android.app.Activity;
        import android.content.Intent;
        import android.graphics.drawable.Drawable;
        import android.media.MediaPlayer;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.AdapterView.OnItemClickListener;
        import android.widget.GridView;
        import android.widget.ImageButton;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.facebook.Profile;

        import java.io.InputStream;
        import java.net.URL;
        import java.util.ArrayList;

public class HomePage extends Activity implements OnItemClickListener
{
    GridView gridview;
    GridViewAdapter gridviewAdapter;
    ArrayList<Item> data = new ArrayList<Item>();

    private static final int request_code = 5;
    private TextView textView;
    private TextView textView1;
    private MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.homepage);

        initView(); // Initialize the GUI Components
        fillData(); // Insert The Data
        setDataAdapter(); // Set the Data Adapter

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        String nome = extras.getString("nome");
        String pic = extras.getString("picURI");

        textView = (TextView) findViewById(R.id.txtView);
        textView1 = (TextView) findViewById(R.id.txtView1);
        textView.setText("Olá, " + nome + "!");

        new SetImageURI().execute(pic);
    }

    public void logout(View view){
        finish();
    }

    public void finish() {
        Intent data = new Intent();
        setResult(RESULT_OK, data);
        super.finish();
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
                Toast.makeText(HomePage.this, "Ocorreu um problema", Toast.LENGTH_SHORT).show();
            }
            return dr;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            ImageButton imgView = (ImageButton) findViewById(R.id.imgPerfil);
            imgView.setImageDrawable(result);
        }
    }

    // Initialize the GUI Components
    private void initView()
    {
        gridview = (GridView) findViewById(R.id.gridView);
        gridview.setOnItemClickListener(this);
    }

    // Insert The Data
    private void fillData()
    {
        data.add(new Item("Sou um Doggiewalker", getResources().getDrawable(R.mipmap.btn_sou_dw)));
        data.add(new Item("Procuro um DoggieWalke", getResources().getDrawable(R.mipmap.btn_procuro_dw)));
        //data.add(new Item("Perfil", getResources().getDrawable(R.mipmap.perfil)));
        //data.add(new Item("Tendências", getResources().getDrawable(R.mipmap.tendencia)));
        //data.add(new Item("Gráficos", getResources().getDrawable(R.mipmap.relatorios)));
    }

    // Set the Data Adapter
    private void setDataAdapter()
    {
        gridviewAdapter = new GridViewAdapter(getApplicationContext(), R.layout.row_grid, data);
        gridview.setAdapter(gridviewAdapter);
    }

    @Override
    public void onItemClick(final AdapterView<?> arg0, final View view, final int position, final long id)
    {
        //String message = "Clicked : " + data.get(position).getTitle();
        //Toast.makeText(getApplicationContext(), message , Toast.LENGTH_SHORT).show();
        if (data.get(position).getTitle() == "Sou um Doggiewalker"){
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                return;
            }
            String nome = extras.getString("nome");
            String pic = extras.getString("picURI");

            textView = (TextView) findViewById(R.id.txtView);
            textView1 = (TextView) findViewById(R.id.txtView1);
            textView.setText("Olá, " + nome + "!");
            new SetImageURI().execute(pic);
            Profile curProfile = Profile.getCurrentProfile();
            startNewIntent(curProfile, nome);
        }else{
            Intent i = new Intent(getApplicationContext(), Cliente.class);
            startActivity(i);
        }
    }
    private void startNewIntent(Profile elUsero, String nome){
        Intent i = new Intent(this, DoggieWalker.class);

        i.putExtra("nome", nome);
        i.putExtra("picURI", elUsero.getProfilePictureUri(1500,1500).toString());
        startActivityForResult(i, request_code);
    }
}