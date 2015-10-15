package com.clubee.doggywalker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.Profile;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by barcat on 7/13/15.
 */

public class DoggieWalker extends Activity {

    //JSON node
    private static final String TAG_SUCCESS = "success";
    private TextView textView;
    private MediaPlayer mp;

    //url para cadastrar novo usuário
    private static String url_cadastraCliente = "http://clubee.com.br/dev/dbDoggyWalker/DoggyWalker_CadastroCliente_Inserir.php";
    JSONParser jsonParser = new JSONParser();
    EditText char_Nome;
    EditText char_CEP;
    EditText char_Email;
    EditText char_Password;
    EditText char_Cidade;
    EditText char_Estado;
    EditText char_Logradouro;
    EditText char_Endereco;
    EditText char_Bairro;
    TextView char_Lat;
    TextView char_Long;

    //barra de progressão
    private ProgressDialog pDialog;

    public String nome;
    public String cep;
    public String email;
    public String cidade;
    public String estado;
    public String logradouro;
    public String bairro;
    public String latitude;
    public String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doggiewalker);

        char_Nome = (EditText) findViewById(R.id.inputNome);
        char_Email = (EditText) findViewById(R.id.inputEmail);
        char_Password = (EditText)findViewById(R.id.char_password);
        char_Logradouro = (EditText) findViewById(R.id.inputLogradouro);
        char_Cidade = (EditText) findViewById(R.id.inputCidade);
        char_Estado = (EditText) findViewById(R.id.inputEstado);
        char_Bairro = (EditText)findViewById(R.id.inputBairro);
        char_CEP = (EditText) findViewById(R.id.inputCEP);
        char_Lat = (TextView) findViewById(R.id.inputLatitude);
        char_Long = (TextView) findViewById(R.id.inputLongitude);

        Drawable originalDrawable = char_Email.getBackground();

        //Criar botão buscaCep
        Button btnBuscaCEP = (Button) findViewById(R.id.btnBuscaEndereco);

        //Criar botão Continuar
        Button btnContinuar = (Button) findViewById(R.id.btnContinuar);

        //Valida conteúdo do email
        char_Email.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if(!hasFocus){
                    email = char_Email.getText().toString();
                    boolean emailValido = isEmailValid(email);
                    if(!emailValido){
                        char_Email.setText("");
                        char_Email.setHint("Preencha com um email válido");
                        char_Email.setHintTextColor(Color.RED);
                        //Toast.makeText(getApplicationContext(), "E-mail Inválido", Toast.LENGTH_LONG).show();
                    } else {
                        char_Email.setTextColor(Color.GREEN);
                    }
                }
            }

            private boolean isEmailValid(String email){
                return Patterns.EMAIL_ADDRESS.matcher(email).matches();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        String nome = extras.getString("nome");
        String pic = extras.getString("picURI");

        textView = (TextView) findViewById(R.id.txtView);
        textView.setText("Olá, " + nome + "!");

        new SetImageURI().execute(pic);

        //Criar evento do botão
        btnBuscaCEP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //abre thread em background
                new HttpRequestTask().execute();
            }
        });

        //Criar evento do botão
        btnContinuar.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent i = new Intent(DoggieWalker.this, Cadastro_ClientesDetalhes.class);
                startActivity(i);
                finish();
            }
        });
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, DAOPostmon> {
        String charCepTrim = char_CEP.getText().toString().trim();
        final String url = "http://api.postmon.com.br/v1/cep/"+charCepTrim;
        RestTemplate restTemplate = new RestTemplate();

        @Override
        protected DAOPostmon doInBackground(Void... params) {
            try {
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                DAOPostmon DAOPostmon = restTemplate.getForObject(url, DAOPostmon.class);
                return DAOPostmon;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(DAOPostmon DAOPostmon) {

            try {
                //quando a tag Logradouro estiver disponiivel no retorno da api rest
                if (DAOPostmon == null || DAOPostmon.getLogradouro() == null) {

                    TextView greetingEndereco = (TextView) findViewById(R.id.inputLogradouro);
                    TextView greetingBairro = (TextView) findViewById(R.id.inputBairro);
                    TextView greetingCidade = (TextView) findViewById(R.id.inputCidade);
                    TextView greetingEstado = (TextView) findViewById(R.id.inputEstado);
                    TextView greetingCEP = (TextView) findViewById(R.id.inputCEP);

                    greetingEndereco.setText(DAOPostmon.getEndereco().toUpperCase());
                    greetingCidade.setText(DAOPostmon.getCidade().toUpperCase());
                    greetingBairro.setText(DAOPostmon.getBairro().toUpperCase());
                    greetingEstado.setText(DAOPostmon.getEstado().toUpperCase());
                    greetingCEP.setText(DAOPostmon.getCep());

                    String endereco = char_Logradouro.getText().toString();
                    BuscaGeolocalizacao localizacaoEnd = new BuscaGeolocalizacao();
                    localizacaoEnd.getAddressFromLocation(endereco,
                            getApplicationContext(), new GeocoderHandler());

                } else {

                    //senão, quando não tiver a tag logradouro, usar endereco

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

                    String endereco = char_Logradouro.getText().toString();
                    BuscaGeolocalizacao localizacaoEnd = new BuscaGeolocalizacao();
                    localizacaoEnd.getAddressFromLocation(endereco,
                            getApplicationContext(), new GeocoderHandler());

                }
            } catch (Exception e) {
                e.printStackTrace();
                TextView greetingEndereco = (TextView) findViewById(R.id.inputLogradouro);
                TextView greetingBairro = (TextView) findViewById(R.id.inputBairro);
                TextView greetingCidade = (TextView) findViewById(R.id.inputCidade);
                TextView greetingEstado = (TextView) findViewById(R.id.inputEstado);
                TextView greetingCEP = (TextView) findViewById(R.id.inputCEP);

                greetingEndereco.setText("ENDEREÇO NÃO ENCONTRADO");
                greetingBairro.setText("");
                greetingCidade.setText("");
                greetingEstado.setText("");
                greetingCEP.setText("");
                Toast.makeText(getBaseContext(),"Entramos em contato por email. Continue o cadastro.",Toast.LENGTH_SHORT).show();

            }
        }
    }


    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String localizacaoEnd;
            Double char_Latitude=null;
            Double char_Longitude=null;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    localizacaoEnd = bundle.getString("endereco");
                    char_Latitude = bundle.getDouble("char_lat");
                    char_Longitude = bundle.getDouble("char_long");
                    break;
                default:
                    localizacaoEnd = null;
            }
            char_Lat.setText(char_Latitude.toString());
            char_Long.setText(char_Longitude.toString());
        }
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