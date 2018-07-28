package com.inova.ufrpe.processos.carropipa.infraestrutura.serverlayer;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.inova.ufrpe.processos.carropipa.R;

import java.util.Objects;

public class Conectar extends Activity {

    private String parametros;
    private Context ctx;
    private OnLoginListener onLoginListener;
    private OnCadastroListener onCadastroListener;

    public interface OnLoginListener{
        void onLogin(String result);
    }

    public interface OnCadastroListener{
        void onCadastro(String result);
    }

    public void setOnLoginListener(OnLoginListener onLoginListener){
        this.onLoginListener = onLoginListener;
    }

    public void setOnCadastroListener(OnCadastroListener onCadastroListener){
        this.onCadastroListener = onCadastroListener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void doConnect(Context context, String url, String parametros){
        Log.d("URL PASSADOS", url);
        Log.d("PARAMETRO PASSADOS", parametros);
        this.ctx = context;
        //snippet para verificar o status da conexão
        boolean isConnected = isConnected(context);
        if (isConnected){
            this.parametros = parametros;
            Log.d("doConnect", this.parametros);
           new SolicitaDados().execute(url);
        }
    }

    /**
     * Usado para obter informação se há ou não conexão com a internet
     * @param context: contexto da chamada - necessário para chamar o getSystemService
     * @return: um boleano que informa o estado da conexão com a internet
     */
    public static Boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = Objects.requireNonNull(cm).getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /*
    Usa asyncTasks!
    A classe interna a seguir conecta a internet e envia informações em segundo plano
     */
private class SolicitaDados extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... url) {
        Log.d("Chamei conexão: ", url[0]);
        return Conexao.postDados(url[0], parametros);
    }

    //exibe os resultados

    /**
     * dentro do metodo onPost result deve ser implementada a lista de cada activites que acessam o
     * servidor
     *
     * @param results: String retorndada pelo servidor. um dia sera JSON
     */
    @Override
    protected void onPostExecute(String results){
        String[] resultado = results.split(",");

        if(resultado[0].contains("cadastration_ok")) {
            if(onCadastroListener != null){
                onCadastroListener.onCadastro(results);
            }
        }else if (resultado[0].contains("login_ok")){
            if (onLoginListener != null){
               onLoginListener.onLogin(results);
            }
        }
        else {
            Toast.makeText(getApplicationContext(), getString(R.string.falha), Toast.LENGTH_SHORT).show();
            // Falha no cadatros!! @TODO tratar erro, para exibir ao Usuário
        }
    }
}
}

