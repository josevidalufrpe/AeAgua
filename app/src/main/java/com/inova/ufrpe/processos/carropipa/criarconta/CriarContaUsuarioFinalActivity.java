package com.inova.ufrpe.processos.carropipa.criarconta;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.inova.ufrpe.processos.carropipa.R;
import com.inova.ufrpe.processos.carropipa.infraestrutura.serverlayer.Conectar;
import com.inova.ufrpe.processos.carropipa.pessoa.dominio.EnumTipos;

public class CriarContaUsuarioFinalActivity extends AppCompatActivity implements Conectar.OnCadastroListener{

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
    private String tipo;
    private String parametros = "";
    private Conectar conectar;
    //private final String url = "http://192.168.1.101:5000/cadastro/cadastrar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        conectar = new Conectar();
        conectar.setOnCadastroListener(CriarContaUsuarioFinalActivity.this);

        setContentView( R.layout.activity_criar_conta );

        Spinner pessoaTipo = findViewById(R.id.spn_tipo);

        editNome = findViewById( R.id.editNome );
        editSobreNome = findViewById( R.id.editSobrenome );
        editEmail = findViewById( R.id.editEmail );
        editTelefone = findViewById( R.id.editTelefone );
        editSenha = findViewById( R.id.editSenha );
        ImageView imageUser = findViewById(R.id.img_user);
        Button btn_criar = findViewById(R.id.btn_criarconta);

        //Spiner Tipos de Pessoa:
        ArrayAdapter<String> enumTiposArrayAdapter;
        enumTiposArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, EnumTipos.EnumTiposLista());
        pessoaTipo.setAdapter(enumTiposArrayAdapter);
        enumTiposArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        pessoaTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipo = parent.getSelectedItem().toString();
                //Log.d("Verificando Spinner", tipo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        //quando o usuário tocar na foto
        imageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chamar função de tirar foto ou escolher
            }
        });

        btn_criar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarCampos();
            }
        });
    }


    private void verificarCampos() {

        nome = editNome.getText().toString().trim();
        sobrenome = editSobreNome.getText().toString().trim();
        celular = editTelefone.getText().toString().trim();
        email = editEmail.getText().toString().trim();
        senha = editSenha.getText().toString().trim();

        if (camposVazios()) {
            EditText v = verificarVazio();
            if (v != null) {
                v.setError(getString(R.string.campo_vazio));
            }
        }else if (tipo.equals(getString(R.string.escolha_pessoa))) {
            Toast.makeText(CriarContaUsuarioFinalActivity.this, R.string.escolha_pessoa, Toast.LENGTH_SHORT).show();
        } else {
            verificarEntradas();
        }
    }

    private boolean camposVazios() {
        return (email.isEmpty())
                || (senha.isEmpty())
                || (celular.isEmpty())
                || (nome.isEmpty())
                || sobrenome.isEmpty();
    }

    private EditText verificarVazio(){
        if (nome.isEmpty()){
            return editNome;
        }else if (sobrenome.isEmpty()){
            return editSobreNome;
        }else if (email.isEmpty()){
            return editEmail;
        }else if (celular.isEmpty()) {
            return editTelefone;
        }else if(senha.isEmpty()){
            return editSenha;
        }
        else{
            return null;
        }
    }

    private void verificarEntradas(){
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError(getString(R.string.err_invalid_mail));
        }else if (!validarNome( nome )) {
            editNome.setError(getString(R.string.err_invalid_name));
        }else if (!validarSobrenome( sobrenome )) {
            editSobreNome.setError(getString(R.string.err_invalid_lastName));
        }else if (!validarNumero( celular )) {
            editTelefone.setError(getString(R.string.err_invalid_phoneNumber));
        }else {
            // Criar com API
            ConnectivityManager cm =
                    (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            //aqui pode gerar exception??
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if (isConnected){
                parametros = "email=" + email +"&tipo=" + tipo +"&senha=" + senha +"&celular=" + celular +"&sobrenome=" + sobrenome +"&nome=" + nome;
                String url = "http://10.246.217.119:5000/cadastro/cadastrar";
                conectar.doConnect(CriarContaUsuarioFinalActivity.this, url, parametros);
            }
            else{ Toast.makeText(CriarContaUsuarioFinalActivity.this, getString(R.string.connection_failed), Toast.LENGTH_SHORT).show(); }
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

        return numero.matches("^[0-9]{8,9}+$");
    }

    @Override
    public void onCadastro(String result) {
        trataResult(result);
    }

    private void trataResult(String results) {
        String[] resultado = results.split(",");

        if(resultado[0].contains("cadastration_ok")) {
            finish();
        }
        else{
            Toast.makeText(CriarContaUsuarioFinalActivity.this, getString(R.string.cadastration_failed), Toast.LENGTH_SHORT).show();
            // Falha no cadatros!! @TODO tratar erro, para exibir ao Usuário
        }
    }
}
