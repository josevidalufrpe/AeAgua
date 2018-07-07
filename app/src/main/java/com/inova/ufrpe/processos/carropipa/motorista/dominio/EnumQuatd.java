package com.inova.ufrpe.processos.carropipa.motorista.dominio;

public enum EnumQuatd {

        A("Escolha a Quantidade de ajuda"), MIL("1000"), CINCOMIL("5000"), DEZMIL("10000"), VINTEMIL("20000"), TRINTAMIL("30000");

        private final String valor;

        EnumQuatd(String valor) {
            this.valor = valor;
        }

        private String getValor() {
            return valor;
        }

        public static String[] EnumQuatdLista(){
            EnumQuatd[] listaTipos = EnumQuatd.values();
            String[] lista = new String[listaTipos.length];
            for (int i = 0; i < listaTipos.length; i++){
                lista[i] = listaTipos[i].getValor();
            }
            return  lista;
        }

    }
