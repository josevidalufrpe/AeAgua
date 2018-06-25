package com.inova.ufrpe.processos.carropipa.infraestrutura.hardware;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.inova.ufrpe.processos.carropipa.R;
import com.inova.ufrpe.processos.carropipa.infraestrutura.ui.CriarContaUsuarioFinalActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class TirarFotoActivity extends AppCompatActivity {

    private final int GALERY = 1;
    private final int CAMERA = 0;

    private ImageView imageView;
    private Bitmap btmReduzido;
    private File foto = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent retornaImageView = getIntent();
        final String nome = retornaImageView.getStringExtra("nome");
        final String sobrenome = retornaImageView.getStringExtra("snome");
        final String email = retornaImageView.getStringExtra("email");
        setContentView(R.layout.activity_tirar_foto);

        imageView = findViewById(R.id.img_user);
        //TextView txtNome = findViewById(R.id.txt_foto_userName);
        /*usado formatação de recurso de String para correta aplicação ao setText()
        cria-se uma string no arquivo de string, captura-se a string e passamos
         os parametros necessários
         */
        Resources resources = getResources();
        //String text = String.format(resources.getString(R.string.nome_proprio),nome,sobrenome);
        //txtNome.setText(text);
        //TextView txtEmail = findViewById(R.id.txt_foto_userMail);
        //txtEmail.setText(email);

        FloatingActionButton fabGaleria = findViewById(R.id.fab_abrirGaleria);

        // trecho adiciona permissão de ler arquivos
        int PERMISSION_REQUEST = 0;
        if(ContextCompat.checkSelfPermission(TirarFotoActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            //não tem permissão: solicitar
            if(ActivityCompat.shouldShowRequestPermissionRationale(TirarFotoActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){

            }else{
                ActivityCompat.requestPermissions(TirarFotoActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
            }
        }

        // trecho adiciona permissão de gravar arquivos
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
            }
        }

        //setar listener para chamar a galeria
        fabGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //verifica permissão de galeria
                int permissionCheck = ContextCompat.checkSelfPermission(TirarFotoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                //se tiver permissão tira foto
                if(permissionCheck == PackageManager.PERMISSION_GRANTED){
                    escolherFoto();
                }
                else{
                    // solicita permissão
                    ActivityCompat.requestPermissions(TirarFotoActivity.this,new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},GALERY);
                }

            }
        });

        FloatingActionButton fabTirarFoto = findViewById(R.id.fab_tirarFoto);
        //setar listener para tirar foto
        fabTirarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //verifica permissão de camera
                int permissionCheck = ContextCompat.checkSelfPermission(TirarFotoActivity.this, Manifest.permission.CAMERA);
                //UseCamera camera = new UseCamera();
                //se tiver permissão tira foto
                if(permissionCheck == PackageManager.PERMISSION_GRANTED){
                    tirarFoto();
                }
                else{
                    // solicita permissão
                    ActivityCompat.requestPermissions(TirarFotoActivity.this,new String[]{
                            Manifest.permission.CAMERA},CAMERA);
                }
            }
        });

        Button btnSalvar = findViewById(R.id.btn_salvar);
        //Salvar tanto persiste a foto quanto retorna no imageview q a chamou.
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Salvar Imagem no banco de dados
                //REtorna a actitivty chamadora

                Intent retornaImageView = new Intent(TirarFotoActivity.this, CriarContaUsuarioFinalActivity.class);
                retornaImageView.putExtra("nome",nome);
                retornaImageView.putExtra("snome",sobrenome);
                retornaImageView.putExtra("email",email);

                if(btmReduzido!=null) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    btmReduzido.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte imagemBytes[] = byteArrayOutputStream.toByteArray();
                    retornaImageView.putExtra("Bitmap", imagemBytes);
                }else{
                    //e se nada for retornado? precisamos setar uma imagem no lugar
                    finish();
                }
                startActivity(retornaImageView);
                //N pode dar finish antes de persistir no banco.
                finish();

            }
        });
    }

    private void escolherFoto() {
        Intent galery =
                new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galery,GALERY);
    }

    private void tirarFoto(){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try{
            ExternalStorage externalStorage = new ExternalStorage();
            foto = externalStorage.criarArquivo();
        }catch (IOException e) {   // Manipulação em caso de falha de criação do arquivo
            e.printStackTrace();
        }
        if(foto!= null) {
            Uri photoURI= FileProvider.getUriForFile(getBaseContext(),getBaseContext().getApplicationContext().getPackageName() + ".provider", foto);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, CAMERA);
        }
    }

    /*Trata a resposta de permissão do usuário
    de acordo com as constantes
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){

        switch (requestCode){
            case CAMERA:{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    tirarFoto();
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
            imageView.setImageBitmap(thumb);
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
                    imageView.setImageBitmap(thumb);
                }
            }
        }
    }
}
