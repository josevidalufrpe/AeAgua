package com.inova.ufrpe.processos.carropipa.pessoa.dominio;

/**
 * Created by Felipe on 14/02/2018.
 * Enum listando todos os estados brasileiros
 */

public enum EnumStados {
    ACRE("AC"), ALAGOAS("AL"), AMAPA("AP"), AMAZONAS("AM"), BAHIA("BA"), CEARA("CE"),
    DISTRITO_FEDERAL("DF"), ESPIRITO_SANTO("ES"), GOIAS("GO"), MARANHAO("MA"), MATO_GROSSO("MT"),
    MATO_GROSSO_DO_SUL("MS"), MINAS_GERAIS("MG"), PARA("PA"), PARAIBA("PB"),PARANA("PR"),
    PERNAMBUCO("PE"), PIAUI("PI"), RIO_DE_JANEIRO("RJ"), RIO_GRANDE_DO_NORTE("RN"),
    RIO_GRANDE_DO_SUL("RS"), RONDONIA("RO"), RORAIMA("RR"), SANTA_CATARINA("SC"), SAO_PAULO("SP"),
    SERGIPE("SE"), TOCANTINS("TO");

    private final String valor;

    EnumStados(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static String[] enumEstadosLista(){
        EnumStados[] listaEstados = EnumStados.values();
        String[] lista = new String[listaEstados.length];
        for (int i =0; i<listaEstados.length;i++){
            lista[i] = listaEstados[i].getValor();
        }
        return  lista;
    }

}
