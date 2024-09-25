package com.mercurio.lms.edi.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.edi.model.NotaFiscalEdi;
import com.mercurio.lms.edi.model.NotaFiscalEdiComplemento;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class NotaFiscalEdiComplementoDAO extends BaseCrudDao<NotaFiscalEdiComplemento, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return NotaFiscalEdiComplemento.class;
    }

    public Long findSequence(){		
		return Long.valueOf(getSession().createSQLQuery("select NOTA_FISCAL_EDI_COMPLEMENTO_SQ.nextval from dual").uniqueResult().toString());
	}	

	public List findByIdNotaFiscalEdi(Long idNotaFiscalEdi) {
    	StringBuilder query = new StringBuilder()
    	.append(" from " + getPersistentClass().getName() + " as nfec ")
    	.append(" where nfec.notaFiscalEdi.idNotaFiscalEdi = :idNotaFiscalEdi " );
    	
    	Map criteria = new HashMap();
    	criteria.put("idNotaFiscalEdi", idNotaFiscalEdi);
    	
    	return getAdsmHibernateTemplate().findByNamedParam(query.toString(), criteria);
    }

	public List<NotaFiscalEdiComplemento> findByIdNotaFiscalEdiIndcIdInformacaoDoctoClien(final Long idNotaFiscalEdi, final Long indcIdInformacaoDoctoClien) {
		String hql = " from NotaFiscalEdiComplemento as nfec "
				+ " where nfec.notaFiscalEdi.idNotaFiscalEdi = :idNotaFiscalEdi " 
				+ "and nfec.indcIdInformacaoDoctoClien = :indcIdInformacaoDoctoClien";
		
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("idNotaFiscalEdi", idNotaFiscalEdi);
		criteria.put("indcIdInformacaoDoctoClien", indcIdInformacaoDoctoClien);
		
		return getAdsmHibernateTemplate().findByNamedParam(hql, criteria);
	}

	public List findDadosComplementos(List listIdNotaFiscalEdi) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(" distinct new Map( (case when (ifdc.blIndicadorNotaFiscal = 'S') then nfec.notaFiscalEdi.idNotaFiscalEdi else null end) as idNotaFiscalEdi, " + 
				" ifdc.idInformacaoDoctoCliente as idInformacaoDoctoCliente, " + 
				" ifdc.dsCampo as dsCampo, " +
				" nfec.valorComplemento as valorComplemento, " + 
				" nfec.notaFiscalEdi.nrNotaFiscal as nrNotaFiscal, " +
				" ifdc.blIndicadorNotaFiscal as blIndicadorNotaFiscal, " +
				" ifdc.dsFormatacao as dsFormatacao, " +
				" ifdc.tpCampo as tpCampo) "
		);

		sql.addFrom(getPersistentClass().getName() + " as nfec, " + 
		 " InformacaoDoctoCliente as ifdc ");
		
		sql.addCustomCriteria(" nfec.indcIdInformacaoDoctoClien = ifdc.idInformacaoDoctoCliente ");
		sql.addCriteriaIn("nfec.notaFiscalEdi.idNotaFiscalEdi", listIdNotaFiscalEdi);
		sql.addOrderBy("(case when (ifdc.blIndicadorNotaFiscal = 'S') then nfec.notaFiscalEdi.idNotaFiscalEdi else null end)");
		sql.addOrderBy("ifdc.blIndicadorNotaFiscal");

		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria()); 
    }
    
    public void removeByIdNotaFiscalEdi(List<Long> list) {
    	StringBuilder deleteQuery = new StringBuilder();
    	deleteQuery.append(" delete from " + getPersistentClass().getName() + " as nfec ");
    	deleteQuery.append(" where nfec.notaFiscalEdi.idNotaFiscalEdi in (" );
    	deleteQuery.append("select nfe.idNotaFiscalEdi from " + NotaFiscalEdi.class.getName() + " as nfe where (nfe.nrNotaFiscal, nfe.cnpjReme) in ");
    	deleteQuery.append("(select nfe1.nrNotaFiscal, nfe1.cnpjReme from " + NotaFiscalEdi.class.getName() + " as nfe1 where nfe1.idNotaFiscalEdi in (:id))) ");
    	while(list.size() > 1000){
    		List<Long> sublist = new ArrayList<Long>(list.subList(0, 999));
    		getAdsmHibernateTemplate().removeByIds(deleteQuery.toString(), sublist);
    		list.removeAll(sublist);
    	}
    	getAdsmHibernateTemplate().removeByIds(deleteQuery.toString(), list);
    }

	public NotaFiscalEdiComplemento findByIdInformacaoDocClienteAndIdNotaFiscalEdi(Long idInformacaoDoctoCliente, Long idNotaFiscalEdi) {
		StringBuilder hql = new StringBuilder();
		
		hql
		.append("SELECT nfec ")
		.append("FROM ")
		.append(NotaFiscalEdiComplemento.class.getSimpleName()).append(" nfec ")
		.append("JOIN nfec.notaFiscalEdi nfe ")
		.append("WHERE ")
		.append("	 nfe.idNotaFiscalEdi =:idNotaFiscalEdi ")
		.append("AND nfec.indcIdInformacaoDoctoClien =:indcIdInformacaoDoctoClien")
		;
		
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("indcIdInformacaoDoctoClien", idInformacaoDoctoCliente);
		criteria.put("idNotaFiscalEdi", idNotaFiscalEdi);

		return (NotaFiscalEdiComplemento) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), criteria);
	}
	
	public NotaFiscalEdiComplemento findByIdInformacaoDocClienteAndValorCompl(Long idInformacaoDoctoCliente, String valorComplemento) {
		StringBuilder hql = new StringBuilder();
		
		hql
		.append("SELECT nfec ")
		.append("FROM ")
		.append(NotaFiscalEdiComplemento.class.getSimpleName()).append(" nfec ")
		.append("JOIN nfec.notaFiscalEdi nfe ")
		.append("WHERE ")
		.append("	 nfec.valorComplemento =:valorComplemento ")
		.append("AND nfec.indcIdInformacaoDoctoClien =:indcIdInformacaoDoctoClien");
		
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("valorComplemento", valorComplemento);
		criteria.put("indcIdInformacaoDoctoClien", idInformacaoDoctoCliente);
		
		List<NotaFiscalEdiComplemento> listaRetorno = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), criteria);
		return  (listaRetorno == null || listaRetorno.isEmpty()) ? null: listaRetorno.get(0); 
		
	}
}