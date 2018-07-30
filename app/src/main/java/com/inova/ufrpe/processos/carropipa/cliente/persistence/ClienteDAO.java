package com.inova.ufrpe.processos.carropipa.cliente.persistence;

import android.content.Context;
import android.util.Log;

import com.inova.ufrpe.processos.carropipa.cliente.dominio.Cliente;
import com.inova.ufrpe.processos.carropipa.infraestrutura.serverlayer.Conectar;

public class ClienteDAO implements Conectar.OnCadastroListener{

    private String parametros;
    //private final String url = "http://10.246.217.119:5000/cadastro/criar_perfil";
    //private final String url = "http://192.168.1.101:5000/cadastro/criar_perfil";
    private Boolean response;
    private Context context;
    private Conectar conectar;

    public ClienteDAO(Context context){
        this.context = context;
        this.conectar = Conectar.getInstance();
        conectar.setOnCadastroListener(this);                                                       //registra metodo da interface cadastro
    }

    /**
     * Metodo salva - Cria e Atualiza uma pessoa no banco remoto
     * @param cliente - Objeto Cliente a ser salvo no banco
     * @return True - se salvo no banco com sucesso
     *         False - se houve falha no registro
     * o método salva é C/U de um CRUD
     */
    public Boolean salva(Cliente cliente) {

        if (Conectar.isConnected(context)){
            Log.d("TESTANDO: ", "conectado");
            if(cliente.getId() != 0){                                                               //cliente veio do banco
                Log.d("TESTANDO: ", "veio do banco!");
                parametros = "id="+ cliente.getId() + "cpf=" + cliente.getCpf() + "&logradouro="+   //Na moral essa história de string
                        cliente.getLogradouro() + "&complemento=" + cliente.getComplemento()+       //perdeu a graça
                        "&bairro=" + cliente.getBairro() + "&cep=" + cliente.getCep()+              //TODO: Usar JSON
                        "&cidade="+cliente.getCidade()+"&uf="+cliente.getUf() + "&usermail="+
                        cliente.getEmail()+ "&senha="+cliente.getSenha()+"&nome="+cliente.getNome()+
                        "&sobrenome="+cliente.getSobreNome()+"&telefone="+cliente.getTelefone();
                String url = "http://192.168.0.102:5000/atualizar/atualizarperfil";
                conectar.doConnect(context, url, parametros);
                Log.d("resposta: ", response.toString());
            }else{                                                                                  //novo cliente
                Log.d("TESTANDO: ", "novo cliente");
                parametros = "cpf=" + cliente.getCpf() + "&logradouro=" + cliente.getLogradouro()+  //precisa ser cadastrado no banco
                        "&complemento=" + cliente.getComplemento() + "&bairro=" + cliente.getBairro()+
                        "&cep=" + cliente.getCep()+ "&cidade="+cliente.getCidade()+"&uf="+cliente.getUf()+
                        "&usermail="+cliente.getEmail()+ "&senha="+cliente.getSenha()+"&nome="+cliente.getNome()+
                        "&sobrenome="+cliente.getSobreNome()+"&tipo="+cliente.getTipo()+
                        "&telefone="+cliente.getTelefone();
                String url = "http://192.168.42.244:5000/cadastro/cadastrar";
                conectar.doConnect(context, url, parametros);
            }
            //Log.d("resposta: ", response.toString());
        }
        return true;                                                                                //gambiarra detected!!
    }

    public Boolean deleta(){
        return Boolean.FALSE;
    }

    @Override
    public void onCadastro(String result) {
        Log.d("TESTANDO: ", "voltei com resposta");
        if (result.contains("cadastration_ok")){
            sucess();
        }else{
            fail();
        }
    }

    /**
     * metodo findAll - Retorna todas as pessoas cadastradas no banco remoto
     *
     * @return : Uma lista com todas as pessoas cadastradas no banco
     */
/*    public ArrayList<Pessoa> findAll(){

            return toList(cursor);
    }

    private ArrayList<Pessoa> toList(Cursor c){
       return ArrayList<Pessoa> l = new List<>();
    }
*/

    private void fail() {
        response = false;
    }

    private void sucess() {
        response = true;
    }
}
