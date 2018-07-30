package com.inova.ufrpe.processos.carropipa.perfil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.inova.ufrpe.processos.carropipa.infraestrutura.hardware.ExternalStorage;
import com.inova.ufrpe.processos.carropipa.infraestrutura.serverlayer.Conectar;
import com.inova.ufrpe.processos.carropipa.pessoa.dominio.EnumStados;
import com.inova.ufrpe.processos.carropipa.pessoa.dominio.EnumTipos;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class PerfilActivity extends AppCompatActivity {

    private final int GALERY = 1;
    private final int CAMERA = 0;
    private File foto = null;

    private EditText cpf;
    private EditText logradouro;
    private EditText complemento;
    private EditText cidade;
    private EditText bairro;
    private EditText cep;
    private Spinner uf;
    private String uf_Selecionado;
    private ImageView imageUser;
    private String user_email;
    private Cliente cliente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        checkPermissions();

        Intent perfilAct = getIntent();
        cliente = Objects.requireNonNull(perfilAct.getExtras()).getParcelable("cliente");
        Log.i("CLIENTE RECEBIDO", cliente.getNome());

        cpf = findViewById(R.id.edt_cpf);
        logradouro = findViewById(R.id.edt_logradouro);
        complemento = findViewById(R.id.edt_complemento);
        cidade = findViewById(R.id.edt_cidade);
        bairro = findViewById(R.id.edt_bairro);
        cep = findViewById(R.id.edt_cep);
        imageUser = findViewById(R.id.img_user);
        uf = findViewById(R.id.spn_uf);

        completaPerfil();

        Button limpar = findViewById(R.id.btn_limpar);
        Button enviar = findViewById(R.id.btn_enviar);
        FloatingActionButton tirarFoto = findViewById(R.id.fab_tirarFoto);
        FloatingActionButton abrirGaleria = findViewById(R.id.fab_abrirGaleria);
        Spinner pessoaTipo = findViewById(R.id.spn_tipo);

        //Setar Spinner Tipo de Pessoa:
        ArrayAdapter<String> enumTiposArrayAdapter;
        enumTiposArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                EnumTipos.EnumTiposLista());
        pessoaTipo.setAdapter(enumTiposArrayAdapter);
        enumTiposArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Log.d("Verificando Spinner", pessoaTipo.getSelectedItem().toString());

        //Spiner Estados:
        ArrayAdapter<String> enumStadosArrayAdapter;
        enumStadosArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                EnumStados.enumEstadosLista());
        uf.setAdapter(enumStadosArrayAdapter);
        enumStadosArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        uf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                uf_Selecionado = parent.getSelectedItem().toString();
                Log.d("Verificando Spinner", uf_Selecionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        //ação dos FABs:
        //Abrir Galeria
        abrirGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verifica permissão de galeria
                int permissionCheck = ContextCompat.checkSelfPermission(PerfilActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                //se tiver permissão tira foto
                if(permissionCheck == PackageManager.PERMISSION_GRANTED){
                    escolherFoto();
                }
                else{
                    // solicita permissão
                    ActivityCompat.requestPermissions(PerfilActivity.this,new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},GALERY);
                }

            }
        });
        //fim da galeria

        //abre a camerera
        tirarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verifica permissão de camera
                int permissionCheck = ContextCompat.checkSelfPermission(PerfilActivity.this, Manifest.permission.CAMERA);
                //UseCamera camera = new UseCamera();
                //se tiver permissão tira foto
                if(permissionCheck == PackageManager.PERMISSION_GRANTED){
                    abreCamera();
                }
                else{
                    // solicita permissão
                    ActivityCompat.requestPermissions(PerfilActivity.this,new String[]{
                            Manifest.permission.CAMERA},CAMERA);
                }
            }
        });
        //fim do abre a camera
        //ação dos botões:
        //botão limpar
        limpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limparCampos();
            }
        });

        //botão enviar:
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarCampos();
            }
        });
    }

    private void completaPerfil() {
        String clientCpf = cliente.getCpf();
        if(clientCpf != null){
            Log.d("CLIENTE TEM: ", clientCpf);
            cpf.setText(clientCpf);
        }
        String clientLogradouro = cliente.getLogradouro();
        if(clientLogradouro != null){
            logradouro.setText(clientLogradouro);
        }
        String clientComplemento = cliente.getComplemento();
        if(clientComplemento != null){
            complemento.setText(clientComplemento);
        }
        String clientCidade = cliente.getCidade();
        if(clientCidade != null) {
            complemento.setText(clientCidade);
        }
        String clientBairro = cliente.getBairro();
        if(clientBairro != null) {
            complemento.setText(clientBairro);
        }
        String clientCep = cliente.getCep();
        if(clientCep != null) {
            complemento.setText(clientCep);
        }
    }

    private void checkPermissions() {
        // trecho adiciona permissão de ler arquivos
        int PERMISSION_REQUEST = 0;
        if(ContextCompat.checkSelfPermission(PerfilActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(PerfilActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){

            }else{                  //não tem permissão: solicitar
                ActivityCompat.requestPermissions(PerfilActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
            }
        }
        // trecho adiciona permissão de gravar arquivos
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Log.d("WRITE PERMISSION: ",  "OK");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
            }
        }
    }

    /**
     *Metodo salvarPerfil - Salva um perfil no banco
     *
     * Le os campos do formulário, atribui a um objeto pessoa e salva esse objeto no banco
     * Para tal faz uso do objeto PessoaDAO - que manipula as opeações no banco de dados
     *
     */
    private void salvarPerfil() {

        Cliente cliente = new Cliente();
        ClienteDAO clienteDAO = new ClienteDAO(PerfilActivity.this);

        cliente.setCpf(cpf.getText().toString());
        cliente.setLogradouro(logradouro.getText().toString());
        cliente.setComplemento(complemento.getText().toString());
        cliente.setCidade(cidade.getText().toString());
        cliente.setBairro(bairro.getText().toString());
        cliente.setCep(cep.getText().toString());

        boolean isConnected = Conectar.isConnected(PerfilActivity.this);
        if (isConnected){
            if (clienteDAO.salva(cliente)){
                Toast.makeText(PerfilActivity.this, R.string.save_sucess, Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(PerfilActivity.this, R.string.falha, Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(PerfilActivity.this, getString(R.string.connection_failed), Toast.LENGTH_SHORT).show(); }
    }

    /**
     * Metodo abreCamera - Tira uma foto com a Camera do Dispositivo
     *
     * Retorna para o arquivo uma foto tirada pela camera do usuário
     */
    private void abreCamera() {
        //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try{
            ExternalStorage externalStorage = new ExternalStorage();
            foto = externalStorage.criarArquivo();
        }catch (IOException e) {   // Manipulação em caso de falha de criação do arquivo
            e.printStackTrace();
        }
        //if(foto!= null) {
        //    Uri photoURI =
        //            FileProvider.getUriForFile(getBaseContext(),
        //                    getBaseContext().getApplicationContext().getPackageName() + ".provider", foto);
        //    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        //    startActivityForResult(takePictureIntent, CAMERA);
        // }
    }

    /**
     *Metodo Escolher foto - Escolhe Foto do usuário
     *
     *  Abre a galeria do usuário para que ele possa escolher uma foto
     */
    private void escolherFoto() {
        Intent galery =
                new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galery,GALERY);
    }

    /**
     * Metodo Limpar campos - Limpa os campos de um formulário
     * params: None
     * return: None
     * action: Limpa os campos de um formulário
     */
    private void limparCampos() {
        try{
        cpf.setText(cpf.getHint());
        logradouro.setText(logradouro.getHint());
        complemento.setText(complemento.getHint());
        cidade.setText(cidade.getHint());
        bairro.setText(bairro.getHint());
        cep.setText(cep.getHint());
        uf.setSelection(0);}
        catch (Exception e){
            Toast.makeText( PerfilActivity.this,"Tela Limpa",Toast.LENGTH_LONG).show(); //Qual exeção??
        }
    }

    //*Trata a resposta de permissão do usuário de acordo com as constantes
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults){

        switch (requestCode){
            case CAMERA:{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    abreCamera();
                }
            }
            case GALERY:{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    escolherFoto();
                }
            }
        }
    }

    /*
   Metodo que responde as Intentes de Tirar Foto e Abrir galeria
   Caso o requestCode seja GALERY ou CAMERA e coloca o resultado no ImageView
    */
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){

        String picturePath = null;
        super.onActivityResult(requestCode,resultCode, data);
        Bitmap thumb;
        Bitmap btmReduzido;
        if(requestCode == GALERY && resultCode== RESULT_OK){
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
            if(c.moveToFirst()){
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
            }
            c.close();
            //Necessario redimensionar a imagem lida
            if(picturePath==null){
                thumb = BitmapFactory.decodeResource(getBaseContext().getResources(),R.mipmap.ic_launcher_round);
            }
            thumb = (BitmapFactory.decodeFile(picturePath));
            btmReduzido = Bitmap.createScaledBitmap(thumb,150,150,true);
            //imageUser.setImageBitmap(thumb);
            imageUser.setImageBitmap(btmReduzido);
        }
        if(requestCode == CAMERA && resultCode== RESULT_OK){

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(foto)));
            //coloca a imagem no ImageView.
            if(data!=null){
                Bundle bundle = data.getExtras();
                if(bundle!=null){
                    thumb = (Bitmap) bundle.get("data");
                    btmReduzido = Bitmap.createScaledBitmap(thumb,150,150,true);
                    //return bitmap criando um metodo de saida
                    imageUser.setImageBitmap(thumb);
                }
            }
        }
    }

        private void verificarCampos(){
        if (cpf.getText().toString().trim().isEmpty()) {
            cpf.setError(getString(R.string.err_emptyField));
        }else if (logradouro.getText().toString().trim().isEmpty()) {
            logradouro.setError(getString(R.string.err_emptyField));
        }else if (complemento.getText().toString().trim().isEmpty()) {
            complemento.setError(getString(R.string.err_emptyField));
        }else if (bairro.getText().toString().trim().isEmpty()) {
            bairro.setError(getString(R.string.err_emptyField));
        }else if (cidade.getText().toString().trim().isEmpty()) {
            cidade.setError(getString(R.string.err_emptyField));
        }else if (cep.getText().toString().trim().isEmpty()) {
            cep.setError(getString(R.string.err_emptyField));
        }else if (uf_Selecionado.equals("0")){
            Toast.makeText(PerfilActivity.this, R.string.err_informUF,Toast.LENGTH_LONG).show();
        }else if(validarNumero(cpf.getText().toString().trim() ) && validarNumero(cep.getText().toString().trim()) ){
            Toast.makeText(PerfilActivity.this, "Somente números em cpf e cep",Toast.LENGTH_LONG).show();
        }else{
            cliente.setUf(uf_Selecionado);
            salvarPerfil();
        }
    }

    private Boolean validarNumero(String numero) {
        return numero.matches("^[0-9]{0,5}+$");
    }
}
