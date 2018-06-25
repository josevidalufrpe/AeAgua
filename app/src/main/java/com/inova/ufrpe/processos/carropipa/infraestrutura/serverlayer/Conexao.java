package com.inova.ufrpe.processos.carropipa.infraestrutura.serverlayer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Felipe on 26/11/2017.
 * cria conexão ao servidor externo
 */

public class Conexao {

    private static HttpURLConnection connection = null;

    public static String postDados(String urlUsuario, String parametroUsuario){

        try{
            URL url = new URL(urlUsuario);

            connection = (HttpURLConnection) url.openConnection();
            //configurar conexão
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type","application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length","" + Integer.toString(parametroUsuario.getBytes().length));
            connection.setRequestProperty("Content-Language","pt-BR");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            outputStreamWriter.write(parametroUsuario);
            outputStreamWriter.flush();

            //obter informação
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuffer resposta = new StringBuffer();

            String linha;
            while((linha = bufferedReader.readLine()) != null){

                resposta.append(linha);
                resposta.append('\r');
            }
            bufferedReader.close();

            return resposta.toString();

        }catch (Exception err){
            return  null;
        }finally {
            if (connection != null){
                connection.disconnect();
            }
        }
    }
}
