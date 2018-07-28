package com.inova.ufrpe.processos.carropipa.login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.inova.ufrpe.processos.carropipa.R;
import com.inova.ufrpe.processos.carropipa.cliente.dominio.Cliente;
import com.inova.ufrpe.processos.carropipa.infraestrutura.serverlayer.Conectar;
import com.inova.ufrpe.processos.carropipa.home.M_MainActivity;
import com.inova.ufrpe.processos.carropipa.infraestrutura.validadores.Validacao;
import com.inova.ufrpe.processos.carropipa.pessoa.dominio.Pessoa;
import com.inova.ufrpe.processos.carropipa.usuario.dominio.Usuario;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements Conectar.OnLoginListener{

    private EditText edt_login;
    private EditText edt_senha;
    //private final String url = "http://10.246.1.121:5000/login/logar";    //quando na ufrpe
    private final String url = "http://192.168.42.244:5000/login/logar";    //via meu 4g
    private String parametros = "";
    //private Usuario usuario= new Usuario();
    //private Pessoa pessoa = new Pessoa();
    private Cliente cliente;
    private Conectar conectar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conectar = new Conectar();
        cliente = new Cliente();
        conectar.setOnLoginListener(LoginActivity.this);
        setContentView(R.layout.activity_login);

        Button btn_logar = findViewById(R.id.btn_logar);
        edt_login = findViewById(R.id.edt_login);
        edt_senha = findViewById(R.id.edt_senha);

        //para logar no sistema:
        btn_logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //snippet para verificar o status da conexão
                ConnectivityManager cm =
                        (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = Objects.requireNonNull(cm).getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                if (isConnected){
                    String emailUser = edt_login.getText().toString();
                    String senhaUser = edt_senha.getText().toString();
                    if(emailUser.isEmpty() || senhaUser.isEmpty()){
                        edt_login.setError(getString(R.string.campo_vazio));
                        edt_senha.setError(getString(R.string.campo_vazio));
                        Toast.makeText(LoginActivity.this, getString(R.string.campo_vazio), Toast.LENGTH_SHORT).show();
                    }else if (!new Validacao().validarEmail(emailUser)) {
                        Toast.makeText(LoginActivity.this, getString(R.string.campo_vazio), Toast.LENGTH_SHORT).show();

                    }else {
                        //TODO IP para connected.
                        parametros = "email=" + emailUser +"&senha=" + senhaUser;
                        conectar.doConnect(LoginActivity.this, url, parametros);
                    }
                }
                else{
                    Snackbar.make(v, R.string.connection_failed, Snackbar.LENGTH_LONG ).show();
                }
            }
        });
    }

    @Override
    public void onLogin(String result) {
        trataResult(result);
    }

    private void trataResult(String results) {
        String[] resultado = results.split(",");
        //Log.d("OLHO NO LANCE!",resultado[1]);

        if(resultado[0].contains("login_ok")){
            //exibir toast apenas para verificar os dados q chegam do servidor
            Intent autentication = new Intent(LoginActivity.this,M_MainActivity.class);
            cliente.setEmail(resultado[1]);
            cliente.setNome(resultado[2]);
            cliente.setSobreNome(resultado[3]);
            Log.d("OLHO NO LANCE!",resultado[3]);
            cliente.setRank(resultado[4]);//esta retornando null (troca para 0.0)
            //setar os outro atributos da resposta??

            /*autentication.putExtra("email",resultado[1]);
            autentication.putExtra("nome",resultado[2]);
            autentication.putExtra("snome",resultado[3]);
            autentication.putExtra("rank",resultado[4]);*/
            autentication.putExtra( "cliente", cliente );
            startActivity(autentication);
        }else {
            Toast.makeText(LoginActivity.this, getString(R.string.userPass_failed), Toast.LENGTH_SHORT).show();
        }
    }
}
