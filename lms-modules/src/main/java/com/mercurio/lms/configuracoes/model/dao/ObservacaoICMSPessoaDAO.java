package com.mercurio.lms.configuracoes.model.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.DescrTributIcmsPessoa;
import com.mercurio.lms.configuracoes.model.ObservacaoICMSPessoa;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * @author José Rodrigo Moraes
 * @since  16/06/2006
 * 
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ObservacaoICMSPessoaDAO extends BaseCrudDao<ObservacaoICMSPessoa, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ObservacaoICMSPessoa.class;
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("inscricaoEstadual", FetchMode.JOIN);
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("inscricaoEstadual", FetchMode.JOIN);
	}

	/**
	 * Validar se existe outro intervalo de vigência existente para a mesma
	 * Inscrição Estadual.
	 *
	 * @param idDescricaoTributacaoIcms
	 * @param dtInicioVigencia
	 * @param dtFimVigencia
	 * @return true or false
	 */
	public boolean validarVigencia( Long idInscricaoEstadual,
									Long idObservacaoICMSPessoa, 
									YearMonthDay dtVigenciaInicial,
									YearMonthDay dtVigenciaFinal) {

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.add(Restrictions.eq("inscricaoEstadual.idInscricaoEstadual", idInscricaoEstadual));
		
		if (idObservacaoICMSPessoa != null) {
			dc.add(Restrictions.ne("idDescrTributIcmsPessoa", idObservacaoICMSPessoa));
		}

		boolean result = true;

		List list = findByDetachedCriteria(dc);

		for (Iterator iter = list.iterator(); iter.hasNext();) {
			DescrTributIcmsPessoa descrTributIcmsPessoa = (DescrTributIcmsPessoa) iter
					.next();
			YearMonthDay dtInicial = descrTributIcmsPessoa
					.getDtVigenciaInicial();
			YearMonthDay dtFinal = descrTributIcmsPessoa.getDtVigenciaFinal();
			if (dtFinal == null) {
				dtFinal = new YearMonthDay(4000, 12, 30);
			}

			/**
			 * As datas de vigência inicial ou final não estejam dentro de outro
			 * intervalo de vigência existente para a mesma Inscrição Estadual
			 */

			if (dtVigenciaInicial.compareTo(dtInicial) >= 0
					&& dtVigenciaInicial.compareTo(dtFinal) <= 0) {
				result = false;
				break;
			}

			if (dtVigenciaFinal != null) {
				if (dtVigenciaFinal.compareTo(dtInicial) >= 0
						&& dtVigenciaFinal.compareTo(dtFinal) <= 0) {
					result = false;
					break;
				}
			} else if (dtVigenciaInicial.compareTo(dtFinal) <= 0) {
				result = false;
				break;
			}
		}

		return result;
	}

	public boolean existeObservacaoICMSPessoaConflitante(ObservacaoICMSPessoa bean) {
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("oip");
		
		sql.addInnerJoin(ObservacaoICMSPessoa.class.getName(),"oip");
		sql.addInnerJoin("oip.inscricaoEstadual","ie");
		
		sql.addCriteria("oip.id","<>",bean.getIdObservacaoICMSPessoa());
		sql.addCriteria("ie.id","=",bean.getInscricaoEstadual().getIdInscricaoEstadual());
		sql.addCriteria("oip.tpObservacaoICMSPessoa","=",bean.getTpObservacaoICMSPessoa().getValue());
		sql.addCriteria("oip.nrOrdemImpressao","=",bean.getNrOrdemImpressao());
		
		JTVigenciaUtils.getHqlValidaVigencia(sql,"oip.dtVigenciaInicial","oip.dtVigenciaFinal",bean.getDtVigenciaInicial(),bean.getDtVigenciaFinal());		
		
		List retorno = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		
		if( retorno != null && !retorno.isEmpty() ){
			return true;
		}		
		
		return false;
	}

	public List findVigenteByIe(Long idIe, YearMonthDay dtVigencia, String tpObservacaoIcmsPessoa){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("oip");
    	
    	hql.addInnerJoin(ObservacaoICMSPessoa.class.getName(), "oip");
    	hql.addInnerJoin("oip.inscricaoEstadual", "ie");
    	hql.addCriteria("ie.id", "=", idIe);
    	hql.addCriteria("oip.tpObservacaoICMSPessoa", "=", tpObservacaoIcmsPessoa);
    	JTVigenciaUtils.getHqlVigenciaNotNull(hql, "oip.dtVigenciaInicial", "oip.dtVigenciaFinal", dtVigencia);
    	
    	return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }    

}