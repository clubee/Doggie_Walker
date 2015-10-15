package com.clubee.doggywalker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by barcat on 7/6/15.
 */
public class Cadastro_ClientesDetalhes extends Activity {


    private static final String TAG_SUCCESS = "success";

    //url para cadastrar novo usuário
    private static String url_cadastraCliente = "http://clubee.com.br/dev/dbDoggyWalker/DoggyWalker_CadastroCliente_Inserir.php";

    JSONParser jsonParser = new JSONParser();

    //barra de progressão
    private ProgressDialog pDialog;

    public String nome, cep, email, cidade, estado, logradouro, endereco, bairro, latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_clientes_detalhes);

        nome = getIntent().getStringExtra("nome");
        cep = getIntent().getStringExtra("cep");
        email = getIntent().getStringExtra("email");
        cidade = getIntent().getStringExtra("cidade");
        estado = getIntent().getStringExtra("estado");
        logradouro = getIntent().getStringExtra("logradouro");
        endereco = getIntent().getStringExtra("endereco");
        bairro = getIntent().getStringExtra("bairro");
        latitude = getIntent().getStringExtra("lat");
        longitude = getIntent().getStringExtra("long");

        //Criar botão
        Button btnCadastraCliente = (Button) findViewById(R.id.btnCadastraCliente);

        //Criar evento do botão
        btnCadastraCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //abre thread em background
                new CadastraCliente().execute();
            }
        });
    }
    class CadastraCliente extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Cadastro_ClientesDetalhes.this);
            pDialog.setMessage("Cadastrando usuário..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {

            Spinner tamanhoPet = (Spinner) findViewById(R.id.spinner_TamPet);
            EditText descricao = (EditText)findViewById(R.id.char_SobreVoce);


            String Nome = nome.toString();
            String Cep = cep.toString();
            String Email = email.toString();
            String Cidade = cidade.toString();
            String Estado = estado.toString();
            String Logradouro = logradouro.toString();
            String Bairro = bairro.toString();
            String Latitude = latitude.toString();
            String Longitude = longitude.toString();
            String TipoLicenca = "DogWalker";
            /*String TamPet = tamanhoPet.getSelectedItem().toString();
            String SobreVc = descricao.getText().toString();*/


            Nome = getIntent().getStringExtra("nome");
            Cep = getIntent().getStringExtra("cep");
            Email = getIntent().getStringExtra("email");
            Cidade = getIntent().getStringExtra("cidade");
            Estado = getIntent().getStringExtra("estado");
            Logradouro = getIntent().getStringExtra("logradouro");
            Bairro = getIntent().getStringExtra("bairro");
            Latitude = getIntent().getStringExtra("lat");
            Longitude = getIntent().getStringExtra("long");


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("char_Nome", Nome));
            params.add(new BasicNameValuePair("char_Cep", Cep));
            params.add(new BasicNameValuePair("char_Email", Email));
            params.add(new BasicNameValuePair("char_Cidade", Cidade));
            params.add(new BasicNameValuePair("char_Estado", Estado));
            params.add(new BasicNameValuePair("char_Endereco", Logradouro));
            params.add(new BasicNameValuePair("char_Bairro", Bairro));
            params.add(new BasicNameValuePair("char_Lat", Latitude));
            params.add(new BasicNameValuePair("char_Long", Longitude));
            params.add(new BasicNameValuePair("char_TipoLicenca",TipoLicenca));
/*
            params.add(new BasicNameValuePair("char_TamPet", TamPet));
            params.add(new BasicNameValuePair("char_Descricao", SobreVc));
*/


            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_cadastraCliente,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), Cadastro_ClientesDetalhes.class);
                    startActivity(i);
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {

            // dismiss the dialog once done
            pDialog.dismiss();
        }
    }
}
