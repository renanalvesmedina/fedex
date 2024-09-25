package com.mercurio.lms.configuracoes.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * Classe que implementa utilidades para a manipulacao de Lists.
 * 
 * @author Diego Pacheco - GT5 - LMS
 * @version 1.0
 *
 */
public class ListUtilsPlus {

    /**
     * Metodo que informa se um elemento eh unico em uma List.
     * OBS: para saber se um Object eh igual ao outro eh usado o Metodo equals. Caso a List sejah NULL ou o size sejah == 0 o metodo retornara true.
     * 
     * @param base eh a List base com os dados.
     * @param objImplEquals eh o Objeto que implementa o metodo equals , que voce desejah saber se eh unico.
     * @return boolean true|false
     */
    @SuppressWarnings("rawtypes")
    public static boolean isUniqueOnList(List base, Object objImplEquals) {
        if (base == null || base.size() == 0)
            return true;
        boolean isUnique = true;

        ListIterator lit = base.listIterator();
        while (lit.hasNext()) {
            Object obj = lit.next();
            if (obj.equals(objImplEquals)) {
                isUnique = false;
                break;
            }
        }
        return isUnique;
    }

    /**
     * Metodo que soma todos os valores de dentro de uma List.
     * 
     * @param values eh a List de values a ser somada.
     * @return Double com o resultado da soma.
     */
    @SuppressWarnings("rawtypes")
    public static Double sumDouble(List values) {
        if (values == null || values.size() == 0)
            return new Double(0);

        double sum = 0;
        ListIterator lit = values.listIterator();
        while (lit.hasNext()) {
            Double aux = (Double) lit.next();
            sum += aux.doubleValue();
        }
        return new Double(sum);
    }

    /**
     * Metodo que soma todos os valores de dentro de uma List.
     * 
     * @param values eh a List de values a ser somada.
     * @return BigDecimal com o resultado da soma.
     */
    @SuppressWarnings("rawtypes")
    public static BigDecimal sumBigDecimal(List values) {
        if (values == null || values.size() == 0)
            return new BigDecimal(0);

        double sum = 0;
        ListIterator lit = values.listIterator();
        while (lit.hasNext()) {
            BigDecimal aux = (BigDecimal) lit.next();
            sum += aux.doubleValue();
        }
        return new BigDecimal(sum);
    }

    /**
     * Adiciona value na lista, desde que esse nao seja null.
     * 
     * @param <T>
     * @param list lista de valores
     * @param value valor que se deseja adicionar
     */
    public static <T> void addIfNotNull(List<T> list, T value) {
        if (value != null) {
            list.add(value);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> List<List<T>> chuncks(List<T> l, Integer range) {
        List<List<T>> la = new ArrayList();
        int size = l.size();
        int first = 0;
        while (size != first) {
            int last = size - first < range ? size : first + range;
            la.add(l.subList(first, last));
            first = last;
        }
        return la;
    }
    
    
    public static List<String> splitToListString(String stringList, String separador) {
        if(stringList == null){
            return Collections.emptyList();
        }
        return Arrays.asList(stringList.split(separador));
    }
    
    
}
