package com.inova.ufrpe.processos.carropipa.criarconta;

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
import com.inova.ufrpe.processos.carropipa.cliente.dominio.Cliente;
import com.inova.ufrpe.processos.carropipa.cliente.persistence.ClienteDAO;
import com.inova.ufrpe.processos.carropipa.infraestrutura.serverlayer.Conectar;
import com.inova.ufrpe.processos.carropipa.pessoa.dominio.EnumTipos;

public class CriarContaUsuarioFinalActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Conectar conectar = Conectar.getInstance();

        setContentView(R.layout.activity_criar_conta);

        Spinner pessoaTipo = findViewById(R.id.spn_tipo);

        editNome = findViewById(R.id.editNome);
        editSobreNome = findViewById(R.id.editSobrenome);
        editEmail = findViewById(R.id.editEmail);
        editTelefone = findViewById(R.id.editTelefone);
        editSenha = findViewById(R.id.editSenha);
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
            public void onNothingSelected(AdapterView<?> parent) {
            }
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
                criarConta();
            }
        });
    }

    private void criarConta() {
        verificarCampos();
        if (validarEntradas()) {
            if (Conectar.isConnected(CriarContaUsuarioFinalActivity.this)) {                //Se há conexão nesse momento
                Cliente cliente = new Cliente();
                cliente.setEmail(email);
                cliente.setNome(nome);
                cliente.setSobreNome(sobrenome);
                cliente.setSenha(senha);
                cliente.setTipo(tipo);
                cliente.setTelefone(celular);
                ClienteDAO clienteDAO = new ClienteDAO(CriarContaUsuarioFinalActivity.this);
                Boolean result = clienteDAO.salva(cliente);
                if (result) {
                    Toast.makeText(CriarContaUsuarioFinalActivity.this, getString(R.string.save_sucess), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CriarContaUsuarioFinalActivity.this, getString(R.string.cadastration_failed), Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            EditText edt = verificarErros(editNome, editSobreNome, editEmail, editSenha, editTelefone);
        }
    }

    private EditText verificarErros(EditText editNome, EditText editSobreNome, EditText editEmail, EditText editSenha, EditText editTelefone ) {
        return editTelefone;
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
        } else if (tipo.equals(getString(R.string.escolha_pessoa))) {
            Toast.makeText(CriarContaUsuarioFinalActivity.this,
                    R.string.escolha_pessoa, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean camposVazios() {
        return (email.isEmpty()) || (senha.isEmpty()) || (celular.isEmpty()) || (nome.isEmpty())
                || sobrenome.isEmpty();
    }

    private EditText verificarVazio() {
        if (nome.isEmpty()) {
            return editNome;
        } else if (sobrenome.isEmpty()) {
            return editSobreNome;
        } else if (email.isEmpty()) {
            return editEmail;
        } else if (celular.isEmpty()) {
            return editTelefone;
        } else if (senha.isEmpty()) {
            return editSenha;
        } else {
            return null;
        }
    }

    private Boolean validarEntradas() {
        Boolean retorno;
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError(getString(R.string.err_invalid_mail));
            retorno = false;
        } else if (!validarNome(nome)) {
            editNome.setError(getString(R.string.err_invalid_name));
            retorno = false;
        } else if (!validarSobrenome(sobrenome)) {                                                    //validarNome == validarSobreNome??
            editSobreNome.setError(getString(R.string.err_invalid_lastName));
            retorno = false;
        } else if (!validarNumero(celular)) {
            editTelefone.setError(getString(R.string.err_invalid_phoneNumber));
            retorno = false;
        } else {
            retorno = true;
        }
        return retorno;
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
}