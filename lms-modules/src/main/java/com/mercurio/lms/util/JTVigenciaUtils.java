package com.mercurio.lms.util;

import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.util.SqlTemplate;

/**
 * Classe para funções utilitárias de manipulação de intervalos de vigências.
 * 
 * 
 * @author Felipe Ferreira
 * @author Rodrigo Silveira
 * @since 10-02-2006
 */
public class JTVigenciaUtils {
	
	/**
	 * Recebe uma entidade e valida se é permitido excluir ela no sistema.
	 * 
	 * @param vigencia Vigencia: entidade com a Vigência incial a ser validada.
	 * @return  
	 */
	public static void validaVigenciaRemocao(Vigencia vigencia) {
		YearMonthDay dtVigenciaInicial = vigencia.getDtVigenciaInicial();
		
		if (dtVigenciaInicial == null) {
    		throw new IllegalArgumentException("dtVigenciaInicial não pode ser null.");
    	}
		
		if (dtVigenciaInicial.compareTo(JTDateTimeUtils.getDataAtual()) <= 0)
			throw new BusinessException("LMS-00005");
	}
	
	/**
	 * Recebe uma entidade de Vigencia e retorna um inteiro correspondente ao seu estado.
	 * 
	 * @param vigencia
	 * @return 0 se antes vigência, 1 se vigente, 2 se depois da vigência.
	 */
	
	public static Integer getIntegerAcaoVigencia(Vigencia vigencia) {
		return getIntegerAcaoVigencia(vigencia,Integer.valueOf(0));
	}
	public static Integer getIntegerAcaoVigencia(Vigencia vigencia, Integer diasAnterioresHoje) {
		YearMonthDay dtVigenciaInicial = vigencia.getDtVigenciaInicial();
		YearMonthDay dtVigenciaFinal = vigencia.getDtVigenciaFinal();
		
		return getIntegerAcaoVigencia(dtVigenciaInicial, dtVigenciaFinal, diasAnterioresHoje);
	}
	
	/**
	 * Recebe datas e retorna o inteiro correspondente. 
	 * 
	 * @author Felipe Ferreira
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @return 0 se antes vigência, 1 se vigente, 2 se depois da vigência.
	 */
	
	public static Integer getIntegerAcaoVigencia(YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		return getIntegerAcaoVigencia(dtVigenciaInicial,dtVigenciaFinal,Integer.valueOf(0));
	}
	
	public static Integer getIntegerAcaoVigencia(YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, Integer diasAnterioresHoje) {
		int acaoVigenciaAtual = 2;
		
		if (dtVigenciaInicial.compareTo(JTDateTimeUtils.getDataAtual()) > 0)
			acaoVigenciaAtual = 0;
		else if (dtVigenciaFinal == null || dtVigenciaFinal.compareTo(JTDateTimeUtils.getDataAtual().minusDays(diasAnterioresHoje.intValue())) >= 0)
			acaoVigenciaAtual = 1;
		
		return Integer.valueOf(acaoVigenciaAtual);
	}

	/**
	 * Monta um DetachedCriteria a partir da das datas de vigencia
	 * inicial e final e de um DetachedCriteria inicial, considerando os
	 * atributos de vigencia padrão (dtVigenciaInicial e dtVigenciaFinal)
	 * 
	 * @param dc DetachedCriteria inicial
	 * @param atributoVigenciaInicial nome do atributo de data de vigencia inicial no pojo
	 * @param atributoVigenciaFinal nome do atributo de data de vigencia final no pojo
	 * @param dtVigenciaInicial data de vigencia inicial
	 * @param dtVigenciaFinal data de vigencia final
	 * @return DetachedCriteria devidamente montado para a consulta
	 */
	public static DetachedCriteria getDetachedVigencia(DetachedCriteria dc, 
			YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		
		Criterion restriction = Restrictions.and(
				Restrictions.ge("dtVigenciaFinal", dtVigenciaInicial),
				Restrictions.le("dtVigenciaInicial", JTDateTimeUtils.maxYmd(dtVigenciaFinal))
		);			
		dc.add(restriction);
		
		return dc;
	}
	

	/**
	 * Monta um DetachedCriteria a partir da das datas de vigencia do fluxo filial
	 * inicial e final e de um DetachedCriteria inicial, considerando os
	 * atributos de vigencia padrão (dtVigenciaInicial e dtVigenciaFinal)
	 * 
	 * @param dc DetachedCriteria inicial
	 * @param atributoVigenciaInicial nome do atributo de data de vigencia inicial no pojo
	 * @param atributoVigenciaFinal nome do atributo de data de vigencia final no pojo
	 * @param dtVigenciaInicial data de vigencia inicial
	 * @param dtVigenciaFinal data de vigencia final
	 * @return DetachedCriteria devidamente montado para a consulta
	 */
	public static DetachedCriteria getDetachedVigenciaFluxoFilial(DetachedCriteria dc, 
			YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		
		Criterion restriction = Restrictions.and(
				Restrictions.le("dtVigenciaInicial", dtVigenciaInicial),
				Restrictions.ge("dtVigenciaFinal", JTDateTimeUtils.maxYmd(dtVigenciaFinal))
		);
		dc.add(restriction);
		
		return dc;
	}
	
	/**
	 * Monta um DetachedCriteria a partir da das datas de vigencia
	 * inicial e final e da classe de persistência em questão, considerando os
	 * atributos de vigencia padrão (dtVigenciaInicial e dtVigenciaFinal).
	 * Na restrição, ignorará o id passado.
	 * 
	 * @param persistentClass classe do objeto persistente.
	 * @param id id do pojo que está sendo salvo.
	 * @param dtVigenciaInicial data de vigencia inicial
	 * @param dtVigenciaFinal data de vigencia final
	 * @return DetachedCriteria devidamente montado para a consulta
	 */
	public static DetachedCriteria getDetachedVigencia(Class persistentClass, Long id, 
			YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		DetachedCriteria dc = DetachedCriteria.forClass(persistentClass);
		
		if (id != null)
			dc.add(Restrictions.ne("id",id));
		
		dc = getDetachedVigencia(dc,dtVigenciaInicial,dtVigenciaFinal);
		
		return dc;
	}
	
	/**
	 * Monta um SqlTemplate a partir das datas de vigencia inicial e final. 
	 * 
	 * @author Felipe Ferreira
	 * @since 08-02-2006
	 * 
	 * @param atributoVigenciaInicial nome do atributo de data de vigencia inicial no pojo
	 * @param atributoVigenciaFinal nome do atributo de data de vigencia final no pojo
	 * @param dtVigenciaInicial data de vigencia inicial
	 * @param dtVigenciaFinal data de vigencia final
	 * 
	 * @return SqlTemplate
	 */	
	public static SqlTemplate getHqlVigencia(SqlTemplate hql, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal,
			String alias) {
		if (alias == null)
			alias = "";
		
		hql.addCriteria(alias + ".dtVigenciaInicial",	"<=",	JTDateTimeUtils.maxYmd(dtVigenciaFinal));
		hql.addCriteria(alias + ".dtVigenciaFinal",		">=",	dtVigenciaInicial);
		
		return hql;
	}
	
	/**
	 * Verifica se pelo menos um dia da semana foi checado.
	 * @param map
	 * @return True se pelo menos um dia da semana 
	 * foi checado ou false em caso contrario.
	 */
	public static boolean verificaPeloMenosUmDiaChecado(Map map) {
		return !(   ((Boolean)map.get("blDomingo")).equals(Boolean.FALSE) 
			&& ((Boolean)map.get("blSegunda")).equals(Boolean.FALSE)
			&& ((Boolean)map.get("blTerca")).equals(Boolean.FALSE)
			&& ((Boolean)map.get("blQuarta")).equals(Boolean.FALSE)
			&& ((Boolean)map.get("blQuinta")).equals(Boolean.FALSE)
            && ((Boolean)map.get("blSexta")).equals(Boolean.FALSE)
			&& ((Boolean)map.get("blSabado")).equals(Boolean.FALSE) );
	}
	
	/**
	 *
	 * @see getHqlVigenciaNotNull(SqlTemplate hql, String campoVigIni,
			String campoVigFin, YearMonthDay dtVigIni, YearMonthDay dtVigFin)
	 * 
	 **/
	public static SqlTemplate getHqlVigenciaNotNull(SqlTemplate hql, String campoVigIni, String campoVigFin, YearMonthDay dtVigIni) {
		return getHqlVigenciaNotNull(hql, campoVigIni, campoVigFin, dtVigIni, null);
	}
	
	/**
	 * Monta um SqlTemplate a partir das datas de vigencia inicial e final nas tabelas onde a
	 * data de vigencia final é igual a 1/1/4000 quando a data final deveria ser nulo. Regras
	 * de vigência do GT2.
	 * 
	 * @author Mickaël Jalbert
	 * @since 26/05/2006
	 * 
	 * @param SqlTemplate hql de orgigem
	 * @param String campoVigIni nome do campo de vigencia inicial
	 * @param String campoVigFin nome do campo de vigencia final
	 * @param YearMonthDay dtVigIni vigencia inicial
	 * @param YearMonthDay dtVigFin vigencia final
	 * 
	 * @return SqlTemplate
	 */	
	public static SqlTemplate getHqlVigenciaNotNull(SqlTemplate hql, 
			                                        String campoVigIni,
			                                        String campoVigFin, 
			                                        YearMonthDay dtVigIni, 
			                                        YearMonthDay dtVigFin) {
		
		if (dtVigIni != null){		
			if (dtVigFin != null){ 
				hql = getHqlValidaVigencia(hql, campoVigIni, campoVigFin, dtVigIni, dtVigFin);
			} else {
				hql.addCustomCriteria("(" + campoVigIni + " <= ? AND " + campoVigFin + " >= ? )");
				hql.addCriteriaValue(dtVigIni);
				hql.addCriteriaValue(dtVigIni);			
			}
		}
		
		return hql;
	}
	
	/**
	 * 
	 * Adiciona ao sqlTemplate passado como parâmetro o critério de validação da vigência de acordo com as datas passadas
	 * 
	 * @author José Rodrigo Moraes
	 * @since  19/06/2006
	 *  
	 * @param hqlString SqlTemplate da consulta
	 * @param campoVigIni Nome do campo de data vigencia inicial
	 * @param campoVigFin Nome do campo de data vigencia final
	 * @param dtVigIni Data Inicial informada na tela
	 * @param dtVigFin Data Final informada na tela (opcional - Se for informado null a data final é transformada para 01/01/4000)
	 * @return SqlTemplate com o critério de vigência
	 */
	public static SqlTemplate getHqlValidaVigencia(SqlTemplate hql, 
			                                       String campoVigIni,
			                                       String campoVigFin, 
			                                       YearMonthDay dtVigIni, 
			                                       YearMonthDay dtVigFin) {
		
		if(dtVigFin == null){
			dtVigFin = JTDateTimeUtils.MAX_YEARMONTHDAY;
		}
		
		String sQuery = new String();
		
		sQuery = "( (? between " + campoVigIni + " and " + campoVigFin + ") or (? between " + campoVigIni + " and " + campoVigFin + ") " +
				 "or ( ? < " + campoVigIni + " and ? > " + campoVigFin + " ))";
		
		hql.addCustomCriteria(sQuery);
		
		hql.addCriteriaValue(dtVigIni);		
		hql.addCriteriaValue(dtVigFin);
		hql.addCriteriaValue(dtVigIni);
		hql.addCriteriaValue(dtVigFin);
		
		return hql;
		
	}
	
	/**
	 * Monta um DetachedCriteria a partir da das datas de vigencia inicial 
	 * e final e de um DetachedCriteria inicial. 
	 * 
	 * @param dc DetachedCriteria inicial
	 * @param atributoVigenciaInicial nome do atributo de data de vigencia inicial no pojo
	 * @param atributoVigenciaFinal nome do atributo de data de vigencia final no pojo
	 * @param dtVigenciaInicial data de vigencia inicial
	 * @param dtVigenciaFinal data de vigencia final
	 * @return DetachedCriteria devidamente montado para a consulta
	 */
	public static void setDetachedVigencia(
		DetachedCriteria dc,
		String atributoVigenciaInicial,
		String atributoVigenciaFinal,
		YearMonthDay dtVigenciaInicial,
		YearMonthDay dtVigenciaFinal
	) {
		if(dtVigenciaFinal == null) {
			dtVigenciaFinal = JTDateTimeUtils.MAX_YEARMONTHDAY;
	}
		dc.add(Restrictions.and(
				Restrictions.le(atributoVigenciaInicial, dtVigenciaInicial),
				Restrictions.ge(atributoVigenciaFinal, dtVigenciaFinal)));
	}
	
	/**
	 * Valida se o objeto vigência está vigente na data de referência recebido.
	 * @param vigencia
	 * @param dtReferencia
	 * @return
	 */
	public static boolean isVigente(Vigencia vigencia, YearMonthDay dtReferencia) {
		if (vigencia == null) {
			throw new IllegalArgumentException("Vigência não pode ser null.");
		}
		if (dtReferencia == null) {
			throw new IllegalArgumentException("DtReferencia não pode ser null");
		}
		
		if (CompareUtils.le(vigencia.getDtVigenciaInicial(), dtReferencia)) {
			if (vigencia.getDtVigenciaFinal() != null) {
				if (CompareUtils.ge(vigencia.getDtVigenciaFinal(), dtReferencia)) {
					return true;
				}
			} else {
				// se nao tem vigencia final é porque é valido até o fim dos
				// tempos
				return true;
			}
		}
		return false;
	}
}

