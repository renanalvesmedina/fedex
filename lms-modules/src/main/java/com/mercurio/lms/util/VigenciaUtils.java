package com.mercurio.lms.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.util.DateTimeUtils;
import com.mercurio.adsm.framework.util.SqlTemplate;

/**
 * 
 * Classe para funções utilitárias de manipulação de intervalos de vigências.
 * 
 * @deprecated Utilizar a classe JTVigenciaUtils.
 * @author Felipe Ferreira
 * @author Rodrigo Silveira
 */
public class VigenciaUtils {

	/**
	 * Recebe a data de vigência inicial e verifica se é menor ou igual que a data do sistema.
	 * Caso a data seja menor ou igual, gera exceção
	 * 
	 * @param date Data de Vigência incial a ser validada.
	 * @return  
	 */
	public static void validaVigenciaRemocao(Date date) {
		if (date instanceof Timestamp) {
			if (date.compareTo(new Timestamp(System.currentTimeMillis())) <= 0)
				throw new BusinessException("LMS-00005");
		} else {
			if (DateTimeUtils.comparaData(date,DateTimeUtils.getDataCorrente()) <= 0)
				throw new BusinessException("LMS-00005");
		}
	}

	/**
	 * Recebe a data de vigência inicial e verifica se é menor que a data do sistema.
	 * Caso a regra seja desrespeitada, gera exceção
	 * 
	 * @param date Data de Vigência incial a ser validada.
	 * @return  
	 */
	public static void validaVigenciaInicial(Date date) {
		if (date instanceof Timestamp) {
			if (date.compareTo(new Timestamp(System.currentTimeMillis())) < 0)
				throw new BusinessException("LMS-00006");
		} else {
			if (DateTimeUtils.comparaData(date,DateUtils.truncate(new Date(),Calendar.DAY_OF_MONTH)) < 0)
				throw new BusinessException("LMS-00006");
		}
	}
	
	/**
	 * Recebe data de vigência inicial e compara com data de vigencia inicial antes de ser alterado na tela.
	 * Após, verifica se as datas sao menores que a data do sistema.
	 * Caso a regra seja desrespeitada, gera exceção
	 * 
	 * @param dateInicial Data de Vigência incial a ser validada.
	 * @param dateInicialOldObj Data de vigência antes de ser alterada na tela.
	 * @param dateFinal Data de Vigência final a ser validada.  
	 */
	public static void validaVigenciaBeforeStore(Date dateInicial, Object dateInicialOldObj, Date dateFinal) {
		if (dateInicialOldObj != null && !((String)dateInicialOldObj).equals("")) {
			
			Object dateInicialOldAux = ReflectionUtils.toObject((String)dateInicialOldObj,Date.class);
			Date dateInicialOld = null;
			if (dateInicialOldAux != null)
				dateInicialOld = (Date)dateInicialOldAux;
			else {
				dateInicialOldAux = ReflectionUtils.toObject((String)dateInicialOldObj,Timestamp.class);
				if (dateInicialOldAux != null)
					dateInicialOld = (Date)dateInicialOldAux;
			}
			
			if (dateInicialOld.compareTo(dateInicial) != 0) {
				if (dateInicial instanceof Timestamp) {
					if (dateInicial.compareTo(new Timestamp(System.currentTimeMillis())) < 0)
						throw new BusinessException("LMS-00006");
				} else if (DateTimeUtils.comparaData(dateInicial,DateUtils.truncate(new Date(),Calendar.DAY_OF_MONTH)) < 0)
					throw new BusinessException("LMS-00006");
			}
		}	
		
		if(dateFinal != null){
			if (dateFinal instanceof Timestamp) {
				if (dateFinal.compareTo(new Timestamp(System.currentTimeMillis())) < 0)
					throw new BusinessException("LMS-00007");
			}else if (DateTimeUtils.comparaData(dateFinal,DateUtils.truncate(new Date(),Calendar.DAY_OF_MONTH)) < 0)
				throw new BusinessException("LMS-00007");
		}
	}
	
	/**
	 * Recebe data de vigência inicial e compara com data de vigencia inicial antes de ser alterado na tela.
	 * Após, verifica se as datas sao menores que a data do sistema.
	 * Caso a regra seja desrespeitada, gera exceção
	 * 
	 * @param dateInicial Data de Vigência incial a ser validada.
	 * @param dateInicialOld Data de vigência antes de ser alterada na tela.
	 * @param dateFinal Data de Vigência final a ser validada.  
	 */
	public static void validaVigenciaBeforeStore(Date dateInicial, Date dateInicialOld, Date dateFinal) {
		if (dateInicialOld != null) {
			if (dateInicialOld.compareTo(dateInicial) != 0) {
				if (DateTimeUtils.comparaData(dateInicial,DateUtils.truncate(new Date(),Calendar.DAY_OF_MONTH)) < 0)
					throw new BusinessException("LMS-00006");
			}
		}		
		if(dateFinal != null){
			if (DateTimeUtils.comparaData(dateFinal,DateUtils.truncate(new Date(),Calendar.DAY_OF_MONTH)) < 0)
				throw new BusinessException("LMS-00007");
		}
	}

	/**
	 * Recebe o objeto populado e retorna ação esperada da vigência.  
	 * 
	 * @param map.
	 * @param dtVigenciaInicial Data de Vigência incial a ser validada.
	 * @param dtVigenciaFinal Data de Vigência final a ser validada.
	 * @return 
	 */
	public static Map getMapAcaoVigencia(Object object, Date dtVigenciaInicial, Date dtVigenciaFinal) {
		return getMapAcaoVigencia(object, dtVigenciaInicial, dtVigenciaFinal, null);
	}
	
	/**
	 * Recebe o objeto populado e retorna ação esperada da vigência.  
	 * 
	 * @param map.
	 * @param dtVigenciaInicial Data de Vigência incial a ser validada.
	 * @param dtVigenciaFinal Data de Vigência final a ser validada.
	 * @return 
	 */
	public static Map getMapAcaoVigencia(Object object, Date dtVigenciaInicial, Date dtVigenciaFinal, List filters) {
		
		Map map = null;
		if (object instanceof Map) {
			map = (Map)object;
		} else {
			map = (Map)ReflectionUtils.copyAndFilterNestedBean(object, filters);
		}
			
		map.put("acaoVigenciaAtual",getIntegerAcaoVigencia(dtVigenciaInicial, dtVigenciaFinal));
		
		return map;
	}
	
	/**
	 * Recebe datas e retorna o inteiro correspondente. 
	 * 
	 * @author Felipe Ferreira
	 * @param dtVigenciaInicial obrigatorio
	 * @param dtVigenciaFinal
	 * @return 0 se antes vigência, 1 se vigente, 2 se depois da vigência.
	 */
	public static Integer getIntegerAcaoVigencia(Date dtVigenciaInicial, Date dtVigenciaFinal) {
		int acaoVigenciaAtual = 2;
		
		if (dtVigenciaInicial == null) {
			throw new IllegalArgumentException("dtVigenciaInicial não pode ser null");
		}
		
		if (dtVigenciaInicial instanceof Timestamp) {
			if (dtVigenciaInicial.compareTo(new Timestamp(System.currentTimeMillis())) > 0) {
				acaoVigenciaAtual = 0;
			}
			else if ((dtVigenciaFinal == null) || 
					dtVigenciaFinal.compareTo(new Timestamp(System.currentTimeMillis())) >= 0) {
				acaoVigenciaAtual = 1;
			}
		} else {
			if (DateTimeUtils.comparaData(dtVigenciaInicial,DateTimeUtils.getDataCorrente()) > 0) {
				acaoVigenciaAtual = 0;
			}
			else if ((dtVigenciaFinal == null) ||
						DateTimeUtils.comparaData(dtVigenciaFinal,DateTimeUtils.getDataCorrente()) >= 0) {
				acaoVigenciaAtual = 1;
			}
		}
		
		return Integer.valueOf(acaoVigenciaAtual);
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
	public static DetachedCriteria getDetachedVigencia(DetachedCriteria dc, String atributoVigenciaInicial,
			String atributoVigenciaFinal, Date dtVigenciaInicial, Date dtVigenciaFinal) {
		dc.add(
			Restrictions.or(Restrictions.and(Restrictions.le(atributoVigenciaInicial, dtVigenciaInicial),
											 Restrictions.or(Restrictions.isNull(atributoVigenciaFinal),
													 		 Restrictions.ge(atributoVigenciaFinal, dtVigenciaInicial))),
							Restrictions.and(Restrictions.gt(atributoVigenciaInicial,dtVigenciaInicial),
											 Restrictions.or(Restrictions.sqlRestriction((dtVigenciaFinal == null ? "1=1" : "1=2")),
													 		 Restrictions.le(atributoVigenciaInicial, dtVigenciaFinal)))
			));
		return dc;
	}
	
	
	/**
	 * Monta um DetachedCriteria a partir da das datasHora de vigencia inicial 
	 * e final e de um DetachedCriteria inicial. 
	 * 
	 * @param dc DetachedCriteria inicial
	 * @param atributoVigenciaInicial nome do atributo de dataHora de vigencia inicial no pojo
	 * @param atributoVigenciaFinal nome do atributo de dataHora de vigencia final no pojo
	 * @param dhVigenciaInicial dataHora de vigencia inicial
	 * @param dhVigenciaFinal dataHora de vigencia final
	 * @return DetachedCriteria devidamente montado para a consulta
	 */
	public static DetachedCriteria getDetachedVigencia(DetachedCriteria dc, String atributoVigenciaInicial,
			String atributoVigenciaFinal, Timestamp dhVigenciaInicial, Timestamp dhVigenciaFinal) {
		dc.add(
			Restrictions.or(Restrictions.and(Restrictions.le(atributoVigenciaInicial, dhVigenciaInicial),
											 Restrictions.or(Restrictions.isNull(atributoVigenciaFinal),
													 		 Restrictions.ge(atributoVigenciaFinal, dhVigenciaInicial))),
							Restrictions.and(Restrictions.gt(atributoVigenciaInicial,dhVigenciaInicial),
											 Restrictions.or(Restrictions.sqlRestriction((dhVigenciaFinal == null ? "1=1" : "1=2")),
													 		 Restrictions.le(atributoVigenciaInicial, dhVigenciaFinal)))
			));
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
	public static SqlTemplate getHqlVigencia(String atributoVigenciaInicial,
			String atributoVigenciaFinal, Date dtVigenciaInicial, Date dtVigenciaFinal) {
		SqlTemplate hql = new SqlTemplate();
		
		StringBuffer sb = new StringBuffer()
				.append("(")
				.append(atributoVigenciaInicial).append(" <= ? and ( ")
				.append(atributoVigenciaFinal)  .append(" is NULL or ") .append(atributoVigenciaFinal) .append(" >= ?")
				.append(") OR (")
				.append(atributoVigenciaInicial).append(" > ? ");
		
		if (dtVigenciaFinal != null) {
			sb.append(" and ").append(atributoVigenciaFinal).append(" <= ? ");
		}
		
		sb.append("))");
		
		hql.addCustomCriteria(sb.toString());
		hql.addCriteriaValue(dtVigenciaInicial);
		hql.addCriteriaValue(dtVigenciaInicial);
		hql.addCriteriaValue(dtVigenciaInicial);
		if (dtVigenciaFinal != null) {
			hql.addCriteriaValue(dtVigenciaFinal);
		}
		
		return hql;
	}
	
	/**
	 * Monta um DetachedCriteria a partir da das datas de vigencia
	 * inicial e final e de um DetachedCriteria inicial, considerando os
	 * atributos de vigencia padrão (dtVigenciaInicial e dtVigenciaFinal)
	 * 
	 * @param dc
	 *            DetachedCriteria inicial
	 * @param dtVigenciaInicial
	 *            data de vigencia inicial
	 * @param dtVigenciaFinal
	 *            data de vigencia final
	 * @return DetachedCriteria devidamente montado para a consulta
	 */
	public static DetachedCriteria getDetachedVigencia(DetachedCriteria dc, Date dtVigenciaInicial, Date dtVigenciaFinal) {
		return getDetachedVigencia(dc, "dtVigenciaInicial", "dtVigenciaFinal", dtVigenciaInicial, dtVigenciaFinal);
	}
	
	
	/**
	 * Monta um DetachedCriteria a partir das datasHora de vigencia
	 * inicial e final e de um DetachedCriteria inicial, considerando os
	 * atributos de vigencia padrão (dhVigenciaInicial e dhVigenciaFinal)
	 * 
	 * @param dc
	 *            DetachedCriteria inicial
	 * @param dhVigenciaInicial
	 *            dataHora de vigencia inicial
	 * @param dhVigenciaFinal
	 *            dataHora de vigencia final
	 * @return DetachedCriteria devidamente montado para a consulta
	 */
	public static DetachedCriteria getDetachedVigencia(DetachedCriteria dc, Timestamp dhVigenciaInicial, Timestamp dhVigenciaFinal) {
		return getDetachedVigencia(dc, "dhVigenciaInicial", "dhVigenciaFinal", dhVigenciaInicial, dhVigenciaFinal);
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
	public static DetachedCriteria getDetachedVigencia(Class persistentClass, Long id, Date dtVigenciaInicial, Date dtVigenciaFinal) {
		DetachedCriteria dc = DetachedCriteria.forClass(persistentClass);
		if (id != null)
			dc.add(Restrictions.ne("id",id));
		dc = getDetachedVigencia(dc,dtVigenciaInicial,dtVigenciaFinal);
		return dc;
	}
	
	/**
	 * Verifica se pelo menos um dia da semana foi checado.
	 * @param map
	 * @return True se pelo menos um dia da semana 
	 * foi checado ou false em caso contrario.
	 */
	public static boolean verificaPeloMenosUmDiaChecado(Map map) {
		return !( ((Boolean)map.get("blDomingo")).equals(Boolean.FALSE) 
			&& ((Boolean)map.get("blSegunda")).equals(Boolean.FALSE)
			&& ((Boolean)map.get("blTerca")).equals(Boolean.FALSE)
			&& ((Boolean)map.get("blQuarta")).equals(Boolean.FALSE)
			&& ((Boolean)map.get("blQuinta")).equals(Boolean.FALSE)
            && ((Boolean)map.get("blSexta")).equals(Boolean.FALSE)
			&& ((Boolean)map.get("blSabado")).equals(Boolean.FALSE) );
	}
	
	/**
	 * Retorna detachedCriteria para validar se houver um registro vigente.
	 * @param clazz
	 * @param id
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @return
	 */
	public DetachedCriteria getDetachedIsEntidadeVigente(Class clazz, Long id, Date dtVigenciaInicial, Date dtVigenciaFinal) {
		DetachedCriteria dc = DetachedCriteria.forClass(clazz);
		
		dc.setProjection(Projections.rowCount());
		
		dc.add(Restrictions.eq("id",id));
		dc.add(Restrictions.le("dtVigenciaInicial",dtVigenciaInicial));
		if (dtVigenciaFinal != null)
			dc.add(Restrictions.or(Restrictions.ge("dtVigenciaFinal",dtVigenciaFinal),Restrictions.isNull("dtVigenciaFinal")));
		
		return dc;
	}
	
}
