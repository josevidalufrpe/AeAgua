package com.inova.ufrpe.processos.carropipa.motorista.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.inova.ufrpe.processos.carropipa.R;
import com.inova.ufrpe.processos.carropipa.infraestrutura.serverlayer.Conexao;
import com.inova.ufrpe.processos.carropipa.infraestrutura.ui.M_MainActivity;
import com.inova.ufrpe.processos.carropipa.infraestrutura.validadores.Validacao;
import com.inova.ufrpe.processos.carropipa.motorista.dominio.Motorista;
import com.inova.ufrpe.processos.carropipa.motorista.dominio.Veiculo;
import com.inova.ufrpe.processos.carropipa.pessoa.dominio.Pessoa;
import com.inova.ufrpe.processos.carropipa.usuario.dominio.Usuario;

public class LoginMotoristaActivity extends AppCompatActivity {

    private EditText edt_login;
    private EditText edt_senha;
    private String url = "";
    private String parametros = "";

    private Motorista motorista = new Motorista();
    private Pessoa pessoa = new Pessoa();
    private Usuario usuario = new Usuario();
    private Veiculo veiculo = new Veiculo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                //isConnected = false;
                if (isConnected){

                    String emailUser = edt_login.getText().toString();
                    String senhaUser = edt_senha.getText().toString();

                    if(emailUser.isEmpty() || senhaUser.isEmpty()){
                        edt_login.setError("CAMPO VAZIO");
                        edt_senha.setError( "CAMPO VAZIO" );
                        Toast.makeText(LoginMotoristaActivity.this, getString(R.string.campo_vazio), Toast.LENGTH_SHORT).show();

                    } else if (!new Validacao().validarEmail(emailUser)) {
                        Toast.makeText(LoginMotoristaActivity.this, getString(R.string.campo_vazio), Toast.LENGTH_SHORT).show();

                    } else {
                        //TODO IP para connected.
                        url = "http://10.246.217.119:5000/login/logar_motorista";
                        //url = "http://192.168.1.101:5000/login/logar_motorista";
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
            if(resultado[0].contains("login_ok")){
                //exibir toast apenas para verificar os dados q chegam do servidor
                if(resultado.length==16){
                    montarObjetoPf(resultado);
                    irTelaMain();
                }else{
                    montarObjetoPj(resultado);
                    irTelaMain();
                }
            } else {
                Toast.makeText(LoginMotoristaActivity.this, getString(R.string.userPass_failed), Toast.LENGTH_SHORT).show();
            }
        }
        public void montarObjetoPf(String[] resultado){
            usuario.setId( Integer.parseInt( resultado[1] ) );
            usuario.setEmail(resultado[2]  );
            usuario.setSenha(resultado[3]  );
            pessoa.setId( Long.valueOf( resultado[4] ) );
            pessoa.setNome( resultado[5] );
            pessoa.setSnome( resultado[6] );
            pessoa.setCpf(resultado[7]);
            pessoa.setTelefone( resultado[8] );
            //pessoa.setUsuario( usuario);
            motorista.setId( Long.valueOf( resultado[9] ) );
            motorista.setRank( resultado[10] );
            motorista.setCnh( resultado[11] );
            //motorista.setPessoa( pessoa );
            veiculo.setId( Integer.parseInt( resultado[12] ) );
            veiculo.setPlaca( resultado[13] );
            veiculo.setCor( resultado[14] );
            veiculo.setCapacidade( resultado[15] );
            //motorista.setVeiculo( veiculo );
        }
        public void montarObjetoPj(String[] resultado){

            usuario.setId( Integer.parseInt( resultado[1] ) );
            usuario.setEmail(resultado[2]  );
            usuario.setSenha(resultado[3]  );
            pessoa.setId( Long.valueOf( resultado[4] ) );
            pessoa.setNome( resultado[5] );
            //pessoa.setSnome( resultado[6] );
            pessoa.setCpf(resultado[7]);
            //pessoa.setTelefone( resultado[7] );
            //pessoa.setUsuario( usuario);
            motorista.setId( Long.valueOf( resultado[8] ) );
            motorista.setRank( resultado[9] );
            motorista.setCnh( resultado[10] );
            //motorista.setPessoa( pessoa );
            motorista.setRank( resultado[11] );
            veiculo.setId( Integer.parseInt( resultado[12] ) );
            veiculo.setPlaca( resultado[13] );
            veiculo.setCor( resultado[14] );
            veiculo.setCapacidade( resultado[15] );
            //motorista.setVeiculo( veiculo );

        }
        public void irTelaMain(){
            Intent autentication = new Intent(LoginMotoristaActivity.this,M_MainActivity.class);
            Bundle bundle = new Bundle(  );
            bundle.putSerializable("motorista",motorista);
            bundle.putSerializable( "pessoa",pessoa );
            bundle.putSerializable("usuario",usuario );
            bundle.putSerializable( "veiculo",veiculo );
            autentication.putExtras( bundle );

            startActivity(autentication);
        }
    }
}
