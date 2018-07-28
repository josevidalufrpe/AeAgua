package com.inova.ufrpe.processos.carropipa.cliente.dominio;

public enum EnumTipos {

        A("Escolha o Tipo de Pessoa"), FISICA("Pessoa Física"), JURIDICA("Pessoa Juridica");

        private final String valor;

        EnumTipos(String valor) {
            this.valor = valor;
        }

        private String getValor() {
            return valor;
        }

        public static String[] EnumTiposLista(){
            EnumTipos[] listaTipos = EnumTipos.values();
            String[] lista = new String[listaTipos.length];
            for (int i = 0; i < listaTipos.length; i++){
                lista[i] = listaTipos[i].getValor();
            }
            return  lista;
        }

    }
