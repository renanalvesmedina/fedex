package com.mercurio.lms.edi.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.edi.model.LogAtualizacaoEDI;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class LogAtualizacaoEDIDAO extends BaseCrudDao<LogAtualizacaoEDI, Long>{

	@Override
	public Class getPersistentClass() {		
		return LogAtualizacaoEDI.class;
	}

	public List findByNrNotaFiscal(String nrNotaFiscal) {
		StringBuilder query = new StringBuilder()
			.append(" from LogAtualizacaoEDI as loae ")
			.append(" where loae.nrNotaFiscal = ? ");
		return getAdsmHibernateTemplate().find(query.toString(), new Object [] {nrNotaFiscal});
	}

	public List findByNrProcessamento(Long nrProcessamento) {
		StringBuilder query = new StringBuilder()
			.append(" from LogAtualizacaoEDI as loae ")
			.append(" where loae.nrProcessamento = ? ");
		return getAdsmHibernateTemplate().find(query.toString(), new Object [] {nrProcessamento});
	}
	
	public List findByIdClienteRemetente(Long idClienteRemente) {
		StringBuilder query = new StringBuilder()
			.append(" from LogAtualizacaoEDI as loae ")
			.append(" where loae.clienteRemetente.idClienteRemetente = ? ");
		return getAdsmHibernateTemplate().find(query.toString(), new Object [] {idClienteRemente});
	}

	public List findByDistinctNrProcessamentoByIdClienteRemetente(Long idClienteRemente) {
		StringBuilder query = new StringBuilder()
			.append(" select distinct loae.nrProcessamento ")
			.append(" from LogAtualizacaoEDI as loae ")
			.append(" where loae.clienteRemetente.idClienteRemetente = ? ");
		return getAdsmHibernateTemplate().find(query.toString(), new Object [] {idClienteRemente});
	}

    public void removeByNrProcessamento(Long nrProcessamento) {
    	String deleteQuery = String.valueOf(" delete from " + getPersistentClass().getName() + " as loae "
    			+ " where loae.nrProcessamento = :id " );    	
    	getAdsmHibernateTemplate().removeById(deleteQuery, nrProcessamento);
    }
}