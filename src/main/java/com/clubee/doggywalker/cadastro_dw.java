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
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


public class cadastro_dw extends Activity {

    //JSON node
    private static final String TAG_SUCCESS = "success";
    //url para cadastrar novo usuário
    private static String url_cadastraCliente = "http://clubee.com.br/dev/dbDoggyWalker/DoggyWalker_CadastroCliente_Inserir.php";
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
        setContentView(R.layout.cadastro_dw);

        char_Nome = (EditText) findViewById(R.id.inputNome);
        char_Email = (EditText) findViewById(R.id.inputEmail);
        char_Logradouro = (EditText) findViewById(R.id.inputLogradouro);
        char_Cidade = (EditText) findViewById(R.id.inputCidade);
        char_Estado = (EditText) findViewById(R.id.inputEstado);
        char_Bairro = (EditText)findViewById(R.id.inputBairro);
        char_CEP = (EditText) findViewById(R.id.inputCEP);

        //Criar botão
        Button btnCadastraCliente = (Button) findViewById(R.id.btnCadastraCliente);
        Button btnBuscaCEP = (Button) findViewById(R.id.btnBuscaEndereco);

        //Criar evento do botão
        btnCadastraCliente.setOnClickListener(new View.OnClickListener() {
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

    private class HttpRequestTask extends AsyncTask<Void, Void, DAOPostmon> {
        @Override
        protected DAOPostmon doInBackground(Void... params) {
            try {
                final String url = "http://api.postmon.com.br/v1/cep/"+char_CEP.getText();
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                DAOPostmon DAOPostmon = restTemplate.getForObject(url, DAOPostmon.class);
                return DAOPostmon;
            } catch (Exception e) {
                Log.e("cadastro_dw", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(DAOPostmon DAOPostmon) {

            if (DAOPostmon.getLogradouro() == null) {

                TextView greetingLogradouro = (TextView) findViewById(R.id.inputLogradouro);
                TextView greetingBairro = (TextView) findViewById(R.id.inputBairro);
                TextView greetingCidade = (TextView) findViewById(R.id.inputCidade);
                TextView greetingEstado = (TextView) findViewById(R.id.inputEstado);
                TextView greetingCEP = (TextView) findViewById(R.id.inputCEP);
                greetingLogradouro.setText(DAOPostmon.getEndereco().toUpperCase());
                greetingCidade.setText(DAOPostmon.getCidade().toUpperCase());
                greetingBairro.setText(DAOPostmon.getBairro().toUpperCase());
                greetingEstado.setText(DAOPostmon.getEstado().toUpperCase());
                greetingCEP.setText(DAOPostmon.getCep());

            } else {

                TextView greetingLogradouro = (TextView) findViewById(R.id.inputLogradouro);
                TextView greetingBairro = (TextView) findViewById(R.id.inputBairro);
                TextView greetingCidade = (TextView) findViewById(R.id.inputCidade);
                TextView greetingEstado = (TextView) findViewById(R.id.inputEstado);
                TextView greetingCEP = (TextView) findViewById(R.id.inputCEP);
                greetingLogradouro.setText(DAOPostmon.getLogradouro().toUpperCase());
                greetingCidade.setText(DAOPostmon.getCidade().toUpperCase());
                greetingBairro.setText(DAOPostmon.getBairro().toUpperCase());
                greetingEstado.setText(DAOPostmon.getEstado().toUpperCase());
                greetingCEP.setText(DAOPostmon.getCep());
            }
        }
    }

    class CadastraCliente extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(cadastro_dw.this);
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
            String Endereco = char_Endereco.getText().toString();
            String TipoLicenca = "DogWalker";


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
                    Intent i = new Intent(getApplicationContext(), cadastro_dw.class);
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