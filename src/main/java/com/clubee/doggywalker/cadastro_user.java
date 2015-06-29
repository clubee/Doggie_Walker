package com.clubee.doggywalker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by barcat on 6/24/15.
 */
public class cadastro_user extends ActionBarActivity {

    @JsonIgnoreProperties(ignoreUnknown = true)

    //JSON node
    private static final String TAG_SUCCESS = "success";

    //url para cadastrar novo usuário
    private static String url_cadastraCliente = "http://clubee.com.br/dev/dbDoggyWalker/DoggyWalker_CadastroUsuario_Inserir.php";

    JSONParser jsonParser = new JSONParser();

    EditText char_Nome;
    EditText char_CEP;
    EditText char_Email;
    EditText char_Cidade;
    EditText char_Estado;
    EditText char_Logradouro;
    EditText char_Endereco;
    EditText char_Bairro;

    //barra de progressão
    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_user);

        char_Nome = (EditText) findViewById(R.id.inputNome);
        char_Email = (EditText) findViewById(R.id.inputEmail);
        char_Logradouro = (EditText) findViewById(R.id.inputLogradouro);
        char_Endereco = (EditText) findViewById(R.id.inputLogradouro2);
        char_Cidade = (EditText) findViewById(R.id.inputCidade);
        char_Estado = (EditText) findViewById(R.id.inputEstado);
        char_Bairro = (EditText)findViewById(R.id.inputBairro);
        char_CEP = (EditText) findViewById(R.id.inputCEP);

        //Criar botão
        Button btnCadastraUsuario = (Button) findViewById(R.id.btnCadastraUsuario);
        Button btnBuscaCEP = (Button) findViewById(R.id.btnBuscaEndereco);

        //Criar evento do botão
        btnCadastraUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //abre thread em background
                new CadastraCliente().execute();
            }
        });

        //Criar evento do botão
        btnBuscaCEP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //abre thread em background
                new HttpRequestTask().execute();
            }
        });
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Greeting> {
        @Override
        protected Greeting doInBackground(Void... params) {
            try {
                final String url = "http://api.postmon.com.br/v1/cep/"+char_CEP.getText();
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Greeting greeting = restTemplate.getForObject(url, Greeting.class);
                return greeting;
            } catch (Exception e) {
                Log.e("cadastro_user", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Greeting greeting) {
            TextView greetingLogradouro = (TextView) findViewById(R.id.inputLogradouro);
            TextView greetingEndereco = (TextView) findViewById(R.id.inputLogradouro2);
            TextView greetingBairro = (TextView) findViewById(R.id.inputBairro);
            TextView greetingCidade = (TextView) findViewById(R.id.inputCidade);
            TextView greetingEstado = (TextView) findViewById(R.id.inputEstado);
            TextView greetingCEP = (TextView) findViewById(R.id.inputCEP);
            greetingLogradouro.setText(greeting.getLogradouro());
            greetingEndereco.setText(greeting.getEndereco());
            greetingCidade.setText(greeting.getCidade());
            greetingBairro.setText(greeting.getBairro());
            greetingEstado.setText(greeting.getEstado());
            greetingCEP.setText(greeting.getCep());
        }
    }

    class CadastraCliente extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(cadastro_user.this);
            pDialog.setMessage("Cadastrando usuário..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {
            String Nome = char_Nome.getText().toString();
            String Email = char_Email.getText().toString();
            String Endereco = char_Logradouro.getText().toString();
            String TipoLicenca = "Usuario";


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("char_Nome", Nome));
            params.add(new BasicNameValuePair("char_Email", Email));
            params.add(new BasicNameValuePair("char_Endereco", Endereco));
            params.add(new BasicNameValuePair("char_TipoLicenca",TipoLicenca));


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
                    Intent i = new Intent(getApplicationContext(), cadastro_user.class);
                    startActivity(i);

                    // closing this screen
                    finish();
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