package com.inova.ufrpe.processos.carropipa.motorista.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.inova.ufrpe.processos.carropipa.R;
import com.inova.ufrpe.processos.carropipa.infraestrutura.serverlayer.Conexao;

public class CriarContaMotoristaActivity extends AppCompatActivity {

    private EditText editNome;
    private EditText editSobreNome;
    private EditText editEmail;
    private EditText editTelefone;
    private EditText editSenha;
    private String nome;
    private String sobrenome;
    private String email;
    private String celular;
    private String senha;
    private String parametros = "";
    private final String url = "http://10.246.1.121:5000/cadastro/cadastrar_motorista";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_criar_contamotorista );

        editNome = findViewById( R.id.editNome );
        editSobreNome = findViewById( R.id.editSobrenome );
        editEmail = findViewById( R.id.editEmail );
        editTelefone = findViewById( R.id.editTelefone );
        editSenha = findViewById( R.id.editSenha );
        Button btn_criar = findViewById(R.id.btn_criarconta);

        btn_criar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verificarCampos();

            }
        } );

    }
    private void verificarCampos() {

         nome = editNome.getText().toString().trim();
         sobrenome = editSobreNome.getText().toString().trim();
         celular = editTelefone.getText().toString().trim();
         email = editEmail.getText().toString().trim();
         senha = editSenha.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty() || celular.isEmpty() || nome.isEmpty()|| sobrenome.isEmpty()) {
            Toast.makeText( CriarContaMotoristaActivity.this, getString( R.string.campo_vazio ), Toast.LENGTH_SHORT ).show();

        }  else if (email.equals("Seu Email") || senha.equals( "senha" ) || nome.equals( "Nome" ) || sobrenome.equals( "Sobrenome" ) || celular.equals( "Telefone" )) {
            Toast.makeText( CriarContaMotoristaActivity.this, getString( R.string.campo_vazio ), Toast.LENGTH_SHORT ).show();

        } else {
            verificarEntradas();
        }
    }

    private void verificarEntradas(){
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Email inválido");
        } else if (validarNome( nome )) {
            editNome.setError("Nome inválido");

        } else if (validarSobrenome( sobrenome )) {
            editSobreNome.setError("sobrenome inválido");

        } else if (validarNumero( celular )) {
            editTelefone.setError("Telefone inválido");

        } else {
            // Criar com API
            ConnectivityManager cm =
                    (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            //aqui pode gerar exception??
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if (isConnected){
                parametros = "email=" + email +"&senha=" + senha +"&celular=" + celular +"&sobrenome=" + sobrenome +"&nome=" + nome;
                new SolicitaDados().execute(url);
            }
            else{ Toast.makeText(CriarContaMotoristaActivity.this, getString(R.string.connection_failed), Toast.LENGTH_SHORT).show(); }
        }
    }

    private Boolean validarNome(String nome) {

        return nome.matches("^(?![ ])(?!.*[ ]{2})((?:e|da|do|das|dos|de|d'|D'|la|las|el|los)" +
                "\\s*?|(?:[A-Z][^\\s]*\\s*?)(?!.*[ ]$))+$");
    }

    private Boolean validarSobrenome(String sobreNome) {

        return (sobreNome.matches("^(?![ ])(?!.*[ ]{2})((?:e|da|do|das|dos|de|d'|D'|la|las|el|los)" +
                "\\s*?|(?:[A-Z][^\\s]*\\s*?)(?!.*[ ]$))+$"));
    }
    private Boolean validarNumero(String numero) {

        return numero.matches("^[0-9]{0,5}+$");
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

            String[] resultado = results.split(", ");

            if(resultado[0].contains("login_ok")) {
                finish();
            }
            else{
                Toast.makeText(CriarContaMotoristaActivity.this, getString(R.string.cadastration_failed), Toast.LENGTH_SHORT).show();
                // Falha no cadatros!! @TODO tratar erro, para exibir ao Usuário
            }
        }
    }
}
