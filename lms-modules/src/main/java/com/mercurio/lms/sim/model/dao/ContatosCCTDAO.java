package com.mercurio.lms.sim.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.sim.model.ContatoCCT;
import com.mercurio.lms.sim.model.EmailCCT;

public class ContatosCCTDAO extends BaseCrudDao<ContatoCCT, Long>{
		
	@Override
	protected Class<ContatoCCT> getPersistentClass() {
		return ContatoCCT.class;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("clienteRemetente", FetchMode.JOIN);
		lazyFindById.put("clienteRemetente.pessoa", FetchMode.JOIN);
		lazyFindById.put("clienteDestinatario", FetchMode.JOIN);
		lazyFindById.put("clienteDestinatario.pessoa", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}
	
	public List<EmailCCT> findEmailsByIdContatoCCT(Long idContatoCCT){
		StringBuilder hql = new StringBuilder();
		hql.append("select ecct ");
		hql.append("from ");
		hql.append("com.mercurio.lms.sim.model.EmailCCT ecct ");
		hql.append("where ");
		hql.append("ecct.contatoCCT.idContatoCCT = ? ");
		
		List param = new ArrayList();
		if(idContatoCCT != null){
			param.add(idContatoCCT);
		}

		return getAdsmHibernateTemplate().find(hql.toString(), param.toArray());	
	}
	
	public List<String> findEmailsByClientesTpParametrizacao(Long idClienteRemetente, Long idClienteDestinatario, String tpParametrizacao) {
		StringBuilder hql = new StringBuilder();
		hql.append("select ecct.dsEmail ");
		hql.append("from ");
		hql.append("com.mercurio.lms.sim.model.EmailCCT ecct ");
		hql.append("inner join ecct.contatoCCT ccct ");
		hql.append("where ");
		hql.append("ccct.tpParametrizacao = ? ");
	
		if(idClienteRemetente != null){
			hql.append("and ccct.clienteRemetente.idCliente = ? ");
		}
		
		if(idClienteDestinatario != null){
			hql.append("and ccct.clienteDestinatario.idCliente = ? ");
		}
		
		List param = new ArrayList();
		param.add(tpParametrizacao);
		if(idClienteRemetente != null){
			param.add(idClienteRemetente);
		}

		if(idClienteDestinatario != null){
			param.add(idClienteDestinatario);
		}

		return getAdsmHibernateTemplate().find(hql.toString(), param.toArray());
	}
	
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginated(TypedFlatMap criteria , FindDefinition findDefinition) {
		List param = new ArrayList();
		StringBuilder hql = getSqlPaginated(criteria, param);
		return getAdsmHibernateTemplate().findPaginated(hql.toString(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), param.toArray());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private StringBuilder getSqlPaginated(TypedFlatMap criteria, List param){
		StringBuilder hql = new StringBuilder();
		hql.append("select new Map(remetente.idPessoa as idClienteRemetente, remetente.nrIdentificacao as nrIdentificacaoRemetente, remetente.nmPessoa as nmPessoaRemetente, ");
		hql.append(" destinatario.idPessoa as idClienteDestinatario, destinatario.nrIdentificacao as nrIdentificacaoDestinatario, destinatario.nmPessoa as nmPessoaDestinatario, ");
		hql.append(" ccct.idContatoCCT as idContatoCCT, ");
		hql.append(" ccct.tpParametrizacao as tipoParametrizacao) ");
		hql.append("from ");
		hql.append("com.mercurio.lms.sim.model.ContatoCCT ccct ");
		hql.append("left outer join ccct.clienteRemetente.pessoa remetente ");
		hql.append("left outer join ccct.clienteDestinatario.pessoa destinatario ");
		hql.append(" where 1=1 ");
	
		if (criteria.getString("tipoParametrizacao") != null) {
			hql.append(" and ccct.tpParametrizacao = ? ");
			param.add(criteria.getString("tipoParametrizacao"));
		}
		if (criteria.getLong("idClienteRemetente") != null) {
			hql.append(" and remetente.idPessoa = ? ");
			param.add(criteria.getLong("idClienteRemetente"));
		}
		if (criteria.getLong("idClienteDestinatario") != null) {
			hql.append(" and destinatario.idPessoa = ? ");
			param.add(criteria.getLong("idClienteDestinatario"));
		}
		if (criteria.getLong("idContatoCCT") != null) {
			hql.append(" and ccct.idContatoCCT = ? ");
			param.add(criteria.getLong("idContatoCCT"));
		}
		
		hql.append(" order by remetente.nmPessoa, destinatario.nmPessoa, ccct.tpParametrizacao ");
		return hql;
	}
	
	@SuppressWarnings("rawtypes")
	public Integer getRowCount(TypedFlatMap criteria){
		List param = new ArrayList();
		StringBuilder hql = getSqlPaginated(criteria, param);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), param.toArray());
	}

	public void removeByIdContatoCCT(List<Long> ids) {
		for(Long id : ids){
			StringBuilder sql = new StringBuilder()
	    	.append("delete from ")
	    	.append(EmailCCT.class.getName()).append(" as ecct ")
	    	.append(" where ecct.contatoCCT.idContatoCCT = ? ");

			List<Long> param = new ArrayList<Long>();
			param.add(id);

			super.executeHql(sql.toString(), param);
		}	
	}

	public void storeEmails(EmailCCT emailCCT) {
    	getAdsmHibernateTemplate().saveOrUpdate(emailCCT);
	}
	
}
