package com.mercurio.lms.edi.model.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.edi.model.ConhecimentoFedex;
import com.mercurio.lms.municipios.model.Filial;

/**
 * @author ernani.brandao
 * LMSA-6520: LMSA-6534
 */
public class ConhecimentoFedexDAO extends BaseCrudDao<ConhecimentoFedex, Long> {

    @SuppressWarnings("rawtypes")
    @Override
    protected Class getPersistentClass() {
        return ConhecimentoFedex.class;
    }

    // LMSA-6267: LMSA-6630
	public BigDecimal findTotalPesoRealConhecimentoFedexByIdControleCarga(Long idControleCarga) {
		StringBuilder query = new StringBuilder();
		query.append(" select nvl(sum(cfdx.pesoReal),0) ");
		query.append(" from  " + ConhecimentoFedex.class.getName() + " as cfdx ");
		query.append(" where cfdx.controleCarga.idControleCarga = :idControleCarga ");
		
		Map<String, Object> criteria = new HashMap<String, Object>(1);
		criteria.put("idControleCarga", idControleCarga);

		return (BigDecimal) getAdsmHibernateTemplate().findUniqueResult(query.toString(), criteria);
	}
    
    /**
     * recuperar todas as notas fiscais vinculadas a chave mdfe informada 
     * @param chaveMdfeFedex
     * @return
     */
    public List<ConhecimentoFedex> findByChaveMdfeFedex(final String chaveMdfeFedex) {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT cfedex ");
        query.append("FROM " + ConhecimentoFedex.class.getName() + " AS cfedex ");
        query.append("WHERE cfedex.chaveMdfe = :chaveMdfe ");

        final Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("chaveMdfe", chaveMdfeFedex);
        String temp = query.toString();
        @SuppressWarnings("unchecked")
        List<ConhecimentoFedex> result = getAdsmHibernateTemplate().findByNamedParam(temp, criteria);
        return result;
    }

    /**
     * recuperar todas as notas fiscais vinculadas a um controle de carga
     * @param idControleCarga
     * @return
     */
    public List<ConhecimentoFedex> findByControleCarga(final Long idControleCarga) {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT cfedex ");
        query.append("FROM " + ConhecimentoFedex.class.getName() + " AS cfedex ");
        query.append("WHERE cfedex.controleCarga.idControleCarga = :idControleCarga ");

        final Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("idControleCarga", idControleCarga);
        String temp = query.toString();
        @SuppressWarnings("unchecked")
        List<ConhecimentoFedex> result = getAdsmHibernateTemplate().findByNamedParam(temp, criteria);
        return result;
    }

    /**
     * recuperar um conhecimento fedex a partir da UK (chaveMdfe, numero Conhecimento)
     * @param chaveMdfeFedex
     * @param conhecimentoFedex
     * @return
     */
    public ConhecimentoFedex findByChaveMdfeAndConhecimentoFedex(final String chaveMdfeFedex, final String conhecimentoFedex) {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT cfedex ");
        query.append("FROM " + ConhecimentoFedex.class.getName() + " AS cfedex ");
        query.append("WHERE cfedex.chaveMdfe = :chaveMdfe ");
        query.append("AND cfedex.numeroConhecimento = :conhecimentoFedex ");

        final Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("chaveMdfe", chaveMdfeFedex);
        criteria.put("conhecimentoFedex", conhecimentoFedex);
        String temp = query.toString();
        @SuppressWarnings("unchecked")
        List<ConhecimentoFedex> list = getAdsmHibernateTemplate().findByNamedParam(temp, criteria);
        ConhecimentoFedex result = null;
        if (list != null && !list.isEmpty()) {
            result = list.get(0);
        }
        return result;
    }

	public Long findSequence(){
		return Long.valueOf(getSession().createSQLQuery("select CONHECIMENTO_FEDEX_SQ.nextval from dual").uniqueResult().toString());
	}

	public ConhecimentoFedex findByFilialNrConhecimento(String sgFilial,
			String nrDocumentoFedex) {
		final StringBuilder query = new StringBuilder();
        query.append("SELECT cfedex ");
        query.append("FROM " + ConhecimentoFedex.class.getName() + " AS cfedex ");
        query.append("WHERE cfedex.siglaFilialOrigem = :siglaFilialOrigem ");
        query.append("AND cfedex.numeroConhecimento = :conhecimentoFedex ");

        final Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("siglaFilialOrigem", sgFilial);
        criteria.put("conhecimentoFedex", nrDocumentoFedex);
        String temp = query.toString();
        @SuppressWarnings("unchecked")
        List<ConhecimentoFedex> list = getAdsmHibernateTemplate().findByNamedParam(temp, criteria);
        ConhecimentoFedex result = null;
        if (list != null && !list.isEmpty()) {
            result = list.get(0);
        }
        return result;
	}

	public ConhecimentoFedex findByIdConhecimento(Long idConhecimentoFedex) {
		final StringBuilder query = new StringBuilder();
        query.append("SELECT cfedex ");
        query.append("FROM " + ConhecimentoFedex.class.getName() + " AS cfedex ");
        query.append("WHERE cfedex.doctoServico.idDoctoServico = :idConhecimentoFedex ");
        query.append("ORDER BY cfedex.idConhecimentoFedex desc ");
        
        final Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("idConhecimentoFedex", idConhecimentoFedex);
        String temp = query.toString();
        @SuppressWarnings("unchecked")
        List<ConhecimentoFedex> list = getAdsmHibernateTemplate().findByNamedParam(temp, criteria);
        ConhecimentoFedex result = null;
        if (list != null && !list.isEmpty()) {
            result = list.get(0);
        }
        return result;
	}	

}
