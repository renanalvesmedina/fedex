package com.mercurio.lms.util;

import com.mercurio.adsm.framework.model.util.DateTimeUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;

/**
 * Classe utilit�ria com algumas funcionalidades para tratar queries feitas por StringBuilder.
 *
 * @author mfachinelli
 */
public class StringBuilderSQLUtils {

    /**
     * M�todo para inserir valores para update em um StringBuilder. Neste m�todo a v�rgula sempre ser� adicionada
     * ao final do StringBuilder.
     * @param stringBuilder StringBuilder contendo o sql de update
     * @param valor Valor a ser atualizado
     * @param campo Nome do campo a receber o update
     */
    public static void adicionarValorUpdate(StringBuilder stringBuilder, Object valor, String campo) {
        adicionarValorUpdate(stringBuilder, valor, campo, false);
    }

    /**
     * M�todo para inserir valores para update em um stringbuilder.
     * @param stringBuilder Stringbuilder contendo o sql de update
     * @param valor Valor a ser atualizado
     * @param campo Nome do campo a receber o update
     * @param finalDeUpdate Caso true, n�o ir� adicionar a virgula ao lado do elemento.
     */
    public static void adicionarValorUpdate(StringBuilder stringBuilder, Object valor, String campo, boolean finalDeUpdate) {
        stringBuilder.append(campo + "=");
        formatarValor(stringBuilder, valor);
        if (!finalDeUpdate) {
            stringBuilder.append(",");
        }

    }

    /**
     * M�todo para informar os valores de um select. Neste m�todo a virula sempre ser� inserida ao final
     * do StringBuilder e n�o ser� inserido alias.
     *
     * @param stringBuilder StringBuilder contendo o sql de busca
     * @param valor Valor a ser buscado
     */
    public static void adicionarValorSelect(StringBuilder stringBuilder, Object valor) {
        adicionarValorSelect(stringBuilder, valor, null, false);
    }

    /**
     * M�todo para informar os valores de um select. Neste m�todo a virula sempre ser� inserida ao final
     * do StringBuilder.
     *
     * @param stringBuilder StringBuilder contendo o sql de busca
     * @param valor Valor a ser buscado
     * @param alias Nome do alias do campo
     */
    public static void adicionarValorSelect(StringBuilder stringBuilder, Object valor, String alias) {
        adicionarValorSelect(stringBuilder, valor, alias, false);
    }
    /**
     * M�todo para informar os valores de um select. Neste m�todo o alias n�o ser� enviado.
     *
     * @param stringBuilder StringBuilder contendo o sql de busca
     * @param valor Valor a ser buscado
     * @param finalDeSelect Caso true, n�o ir� adicionar a virgula ao lado do elemento.
     */
    public static void adicionarValorSelect(StringBuilder stringBuilder, Object valor, boolean finalDeSelect) {
        adicionarValorSelect(stringBuilder, valor, null, finalDeSelect);
    }

    /**
     * M�todo para informar os valores de um select.
     *
     * @param stringBuilder StringBuilder contendo o sql de busca
     * @param valor Valor a ser buscado
     * @param alias Nome do alias do campo
     * @param finalDeSelect Caso true, n�o ir� adicionar a virgula ao lado do elemento.
     */
    public static void adicionarValorSelect(StringBuilder stringBuilder, Object valor, String alias, boolean finalDeSelect) {
        String separador = ((alias != null) ? " as " + alias : "") + ((finalDeSelect) ? " " : ",");
        formatarValor(stringBuilder, valor);
        stringBuilder.append(separador);

    }

    /**
     * M�todo para inserir os valores formatados no stringbuilder
     *
     * @param stringBuilder Stringbuilder contendo o sql para o insert
     * @param valor Objeto a ser inserido no stringbuilder
     */
    private static void formatarValor(StringBuilder stringBuilder, Object valor) {
        if (valor == null) {
            stringBuilder.append("NULL");
        } else if (valor instanceof String) {
            stringBuilder.append("'" + ((String) valor).replaceAll("'","''") + "'");
        } else if (valor instanceof DateTime) {
            stringBuilder.append("'" + ((DateTime) valor).toString(DateTimeUtils.DD_MM_YYYY_HH_MI_SS_ZZ) + "'");
        } else if (valor instanceof Boolean) {
            stringBuilder.append("'" + ((Boolean) valor ? "S" : "N") + "'");
        } else if (NumberUtils.isNumber(String.valueOf(valor))) {
            stringBuilder.append(valor);
        }
    }

}
