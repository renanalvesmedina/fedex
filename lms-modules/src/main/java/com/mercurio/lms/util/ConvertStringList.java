package com.mercurio.lms.util;

import com.mercurio.lms.configuracoes.ConfiguracoesFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public  class  ConvertStringList {

    public List<String> convertStringListParametroCnpjRaiz(String cnpjRaiz, ConfiguracoesFacade configuracoesFacade) {
        return new ArrayList<>
                (Arrays.asList(((String) configuracoesFacade.getValorParametro(cnpjRaiz)).split(",")));
    }
}
