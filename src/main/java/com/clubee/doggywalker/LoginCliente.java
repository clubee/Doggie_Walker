package com.clubee.doggywalker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by barcat on 7/7/15.
 */
public class LoginCliente extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logincliente);

}

    public void cadastro_dogwalker(View view) {
        Intent i = new Intent(this, CadastroClientes.class);
        startActivity(i);
    }

    public void acesso_login(View view) {
        Button btnLogin = (Button) findViewById(R.id.btn_Login);
        Button btnCadastro = (Button) findViewById(R.id.btn_CadastroDogWalker);
        EditText txtUsuario = (EditText) findViewById(R.id.char_email);
        EditText txtPwd = (EditText) findViewById(R.id.char_password);

        btnCadastro.setVisibility(View.INVISIBLE);
        btnLogin.setVisibility(View.INVISIBLE);

        txtUsuario.setVisibility(View.VISIBLE);
        txtPwd.setVisibility(View.VISIBLE);
    }

}
