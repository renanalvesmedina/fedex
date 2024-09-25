package com.mercurio.lms.configuracoes.enums;

public enum AjusteDBTipoBind {

    NUMBER("bindNumber"), STRING("bindString"), DATE("bindDate");

    private String chave;

    private AjusteDBTipoBind(String chave) {
        this.chave = chave;
    }

    public String getChave() {
        return chave;
    }

    public static AjusteDBTipoBind getTipoByChave(String chave) {
        for (AjusteDBTipoBind tipo : values()) {
            if (tipo.getChave().equals(chave)) {
                return tipo;
            }
        }

        throw new RuntimeException("Not a valid enum for this type.");
    }

}
