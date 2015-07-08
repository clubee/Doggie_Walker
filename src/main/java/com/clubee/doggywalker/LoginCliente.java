package com.clubee.doggywalker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

}
