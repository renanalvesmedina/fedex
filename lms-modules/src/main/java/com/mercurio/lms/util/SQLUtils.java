package com.mercurio.lms.util;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.util.SqlTemplate;

/**
 * Classe utilitária com algumas funcionalidades para tratar queries SQL.
 * @author luisfco
 */
public class SQLUtils {

	/**
	 * Adiciona (AND) à expressao recebida.
	 * @param expression
	 * @return
	 */
	public static String andExpression(String expression) {
		return (" AND " + expression + " ");			
	}
	

	/**
	 * Une n expressões SQL com (AND).
	 * Exemplo: List = {'AGENCIA.ID=CONTA.ID_AGENCIA' 'CIDADE.ID=AGENCIA.ID_CIDADE'}
	 * Resultado: '(AGENCIA.ID=CONTA.ID_AGENCIA AND CIDADE.ID=AGENCIA.ID_CIDADE)'
	 * @param list
	 * @return
	 */
	public static String joinExpressionsWithAND(List list) {
		String retorno = new String();
		
		for (int i=0; i<list.size(); i++ ) {
			if (i==0) {
				retorno += (String)list.get(i);
			} else {
				retorno += andExpression((String)list.get(i));	
			}
		}
		return (StringUtils.isNotBlank(retorno)) ? ("(" + retorno + ")") : retorno;
	}
	
	/**
	 * Adiciona (OR) à expressao recebida.
	 * @param expression
	 * @return
	 */
	public static String orExpression(String expression) {
		return (" OR " + expression + " ");
	}
	
	/**
	 * Une n expressões SQL com (OR).
	 * Exemplo: List = {'AGENCIA.ID=CONTA.ID_AGENCIA'  'CIDADE.ID=AGENCIA.ID_CIDADE'}
	 * Resultado: '(AGENCIA.ID=CONTA.ID_AGENCIA OR CIDADE.ID=AGENCIA.ID_CIDADE)'
	 * @param list
	 * @return
	 */
	public static String joinExpressionsWithOR(List list) {
		String retorno = new String();
		
		for (int i=0; i<list.size(); i++ ) {
			if (i==0) {
				retorno += (String)list.get(i);
			} else {
				retorno += orExpression((String)list.get(i));	
			}
		}
		return (StringUtils.isNotBlank(retorno)) ? ("(" + retorno + ")") : retorno;
	}
	
	/**
	 * Une n exrpessões com vírgula.
	 * Exemplo: List = {'B' 'BC' 'CBA'}
	 * Resultado =  '(B, BC, CBA)'
	 * Sugestão de consumo: experimente com na cláusula IN(?, ?, ...?)
	 * @author luisfco
	 * @param list
	 * @return
	 */
	public static String joinExpressionsWithComma(List list) {
		String retorno = new String();
		
		for (int i=0; i<list.size(); i++ ) {
			if (i==0) {
				retorno += (Object)list.get(i);
			} else {
				retorno += ", " + (Object)list.get(i) + " ";	
			}
		}
		return (StringUtils.isNotBlank(retorno)) ? ("(" + retorno + ")") : retorno;
	}
	
	/**
	 * Método que monta uma String para ser usada em cláusulas IN
	 * Exemplo: List = ["B","BC","CBA"]
	 * Resultado =  ('B','BC','CBA')
	 * 
	 * @param list
	 * @return
	 */
	public static String mountStringForInExpression(List list) {
		String stringRetorno = new String();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			stringRetorno += "'" + (String) iter.next() + "'"; 
			if (iter.hasNext()){
				stringRetorno += ",";
			}			
		}
		return "(" + stringRetorno + ")";
	}
	
	/**
	 * Método que monta um valor numérico para ser usada em cláusulas IN
	 * Exemplo: List = [1,2,3]
	 * Resultado =  (1,2,3)
	 * 
	 * @param list
	 * @return
	 */
	public static String mountNumberForInExpression(List list) {
		String numeberRetorno = new String();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			numeberRetorno += (Object)iter.next(); 
			if (iter.hasNext()){
				numeberRetorno += ",";
			}			
		}
		return "(" + numeberRetorno + ")";
	}
	
	
	/**
	 * Gera um criterio no SqlTemplate informado em cima do campo informado.
	 * 
	 * Exemplo: list = {1,2,3}
	 * 			sql.getSql = select * from a
	 * 			strCampo = "a.campo1"
	 * 
	 * Resultado: sql.getSql = select * from a where a.campo1 in (?,?,?)
	 * 			  sql.getCriteria = {1,2,3}
	 * 
	 * @author Mickaël Jalbert
	 * @since 04/05/2006
	 * 
	 * @param List list
	 * @param SqlTemplate sql
	 * @param String strCampo
	 * 
	 * @return
	 */
	public static SqlTemplate joinExpressionsWithComma(List list, SqlTemplate sql, String strCampo) {
		String strCriterios = null;
		
		if (list != null && !list.isEmpty()){
			strCriterios = new String();
			
			for (Iterator iter = list.iterator(); iter.hasNext();){
				strCriterios = strCriterios + "?,";
				sql.addCriteriaValue(iter.next());
			}		
	
			sql.addCustomCriteria(strCampo +" in (" + strCriterios.substring(0, strCriterios.length()-1) + ")");		
		}
		
		return sql;
	
}
}
