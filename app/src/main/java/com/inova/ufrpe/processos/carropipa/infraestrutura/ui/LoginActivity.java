package com.inova.ufrpe.processos.carropipa.infraestrutura.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.inova.ufrpe.processos.carropipa.R;
import com.inova.ufrpe.processos.carropipa.cliente.dominio.Cliente;
import com.inova.ufrpe.processos.carropipa.infraestrutura.serverlayer.Conexao;
import com.inova.ufrpe.processos.carropipa.infraestrutura.validadores.Validacao;
import com.inova.ufrpe.processos.carropipa.pessoa.dominio.Pessoa;
import com.inova.ufrpe.processos.carropipa.usuario.dominio.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText edt_login;
    private EditText edt_senha;
    //private final String url = "http://192.168.42.244:5000/login/logar";
    private final String url = "http://192.168.1.101:5000/login/logar";
    private String parametros = "";
    private Usuario usuario= new Usuario();
    private Pessoa pessoa = new Pessoa();
    private Cliente cliente = new Cliente();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AtivarGps();
        Button btn_logar = findViewById(R.id.btn_logar);
        edt_login = findViewById(R.id.edt_login);
        edt_senha = findViewById(R.id.edt_senha);

        //para logar no sistema:
        btn_logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent autentication = new Intent(LoginActivity.this,M_MainActivity.class);
                //startActivity(autentication);
                //snippet para verificar o status da conexão
                ConnectivityManager cm =
                        (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                //aqui pode gerar exception??
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                //isConnected = false;
                if (isConnected){

                    String emailUser = edt_login.getText().toString();
                    String senhaUser = edt_senha.getText().toString();

                    if(emailUser.isEmpty() || senhaUser.isEmpty()){
                        edt_login.setError("CAMPO VAZIO");
                        edt_senha.setError( "CAMPO VAZIO" );
                        Toast.makeText(LoginActivity.this, getString(R.string.campo_vazio), Toast.LENGTH_SHORT).show();

                    } else if (!new Validacao().validarEmail(emailUser)) {
                        Toast.makeText(LoginActivity.this, getString(R.string.campo_vazio), Toast.LENGTH_SHORT).show();

                    } else {
                        //TODO IP para connected.
                        parametros = "email=" + emailUser +"&senha=" + senhaUser;
                        new SolicitaDados().execute(url);
                    }
                }
                else{
                    Snackbar.make(v, R.string.connection_failed, Snackbar.LENGTH_LONG ).show();
                   // Toast.makeText(LoginActivity.this, getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*
    Usa asyncTasks!
    A classe interna a seguir conecta a internet e envia informações em segundo plano
     */
    private class SolicitaDados extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            return Conexao.postDados(url[0], parametros);
        }

        //exibe os resultados
        @Override
        protected void onPostExecute(String results){

            //Criado para tratar a nova String vinda do Servidor;

            String[] resultado = results.split(",");
            //Log.d("OLHO NO LANCE!",resultado[1]);
            //TODO falta verificar se é juridica ou fisica
            // está so pegando a resposta de fisica
            if(resultado[0].contains("login_ok")){
                //exibir toast apenas para verificar os dados q chegam do servidor
                Intent autentication = new Intent(LoginActivity.this,M_MainActivity.class);
                //Criar objeto usuario
                usuario.setId( Integer.parseInt( resultado[1] )  );
                usuario.setEmail( resultado[2] );
                usuario.setSenha(resultado[3]  );
                //Criar objeto pessoa
                pessoa.setId( Long.valueOf( resultado[4] ) );
                pessoa.setNome( resultado[5]   );
                pessoa.setSnome( resultado[6] );
                if(resultado.length == 11){
                    pessoa.setCpf( resultado[7] );
                    pessoa.setTelefone( resultado[8] );
                    cliente.setId( Integer.parseInt( resultado[9] ) );
                    cliente.setRank( resultado[10] );
                }
                else{
                    pessoa.setCpf( "0" );
                    cliente.setId( Integer.parseInt( resultado[7] ) );
                    cliente.setRank( resultado[8] );

                }

                //Unir todos objeto em 1 só
                pessoa.setUsuario( usuario );
                cliente.setPessoa( pessoa );

                autentication.putExtra("email",resultado[1]);
                autentication.putExtra("nome",resultado[2]);
                autentication.putExtra("snome",resultado[3]);
                autentication.putExtra("rank",resultado[4]);
                autentication.putExtra( "cliente",cliente );

                startActivity(autentication);
            }
            else {
                Toast.makeText(LoginActivity.this, getString(R.string.userPass_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void AtivarGps(){
        String provider = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        //Se vier null ou length == 0   é por que o GPS esta desabilitado.
        //Para abrir a tela do menu pode fazer assim:

        if (provider.length()==0 ||provider.equals( null )){
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 1);
        }
    }
}
