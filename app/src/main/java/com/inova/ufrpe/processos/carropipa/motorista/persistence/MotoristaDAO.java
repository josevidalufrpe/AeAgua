package com.inova.ufrpe.processos.carropipa.motorista.persistence;

import android.database.Cursor;
import android.os.AsyncTask;

import com.inova.ufrpe.processos.carropipa.infraestrutura.serverlayer.Conexao;
import com.inova.ufrpe.processos.carropipa.pessoa.dominio.Pessoa;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class MotoristaDAO {

    String parametros;
    String url = "http://192.168.15.148:5000/cadastro/criar_perfil";
    Boolean response;



    /**
     * Metodo salva - Cria e Atualiza uma pessoa no banco remoto
     * @param pessoa - objeto a ser salvo no banco
     * @return True - se salvo no banco com sucesso
     *         False - se houve falha no registro
     * o método salva é C/U de um CRUD
     */
    public Boolean salva(Pessoa pessoa, String email) {
        parametros = "cpf=" + pessoa.getCpf() +"&logradouro=" + pessoa.getLogradouro() +"&complemento="
                + pessoa.getComplemento() +"&bairro=" + pessoa.getBairro() +"&cep=" + pessoa.getCep()+
        "&cidade="+pessoa.getCidade()+"&uf="+pessoa.getUf()+"&usermail="+email;
        new enviaDados().execute(url);
        return response;
    }

    public Boolean deleta(){
        return Boolean.FALSE;
    }

    /**
     * metodo findAll - Retorna todas as pessoas cadastradas no banco remoto
     * @param : None
     * @return : Uma lista com todas as pessoas cadastradas no banco
     */
/*    public ArrayList<Pessoa> findAll(){

            return toList(cursor);
    }

    private ArrayList<Pessoa> toList(Cursor c){
       return ArrayList<Pessoa> l = new List<>();
    }
*/
    private class enviaDados extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            return Conexao.postDados(url[0], parametros);
        }

        //exibe os resultados
        @Override
        protected void onPostExecute(String results){

            //Criado para tratar a nova String vinda do Servidor;

            String[] resultado = results.split(",");

            if(resultado[0].contains("cadastration_ok")) {
                sucess();
            }
            else{
                //Toast.makeText(PerfilActivity.this, getString(R.string.cadastration_failed), Toast.LENGTH_SHORT).show();
                // Falha no cadatros!! @TODO tratar erro, para exibir ao Usuário
                fail(resultado);
            }
        }
    }

    private void fail(String[] resultado) {
        if(resultado[0].contains("cadastration_fail")){ //colocar no resto do objeto motivos para falha
            response = FALSE;
        }
    }

    private void sucess() {
        response = TRUE;
    }
}
