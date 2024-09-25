package com.mercurio.lms.configuracoes.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.BlackList;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.WhiteList;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.tracking.util.TrackingContantsUtil;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class WhiteListDAO extends BaseCrudDao<WhiteList, Long> {
	
	@Override
	@SuppressWarnings("all")
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("doctoServico", FetchMode.JOIN);	
	}

	@Override
	protected Class getPersistentClass() {
		return WhiteList.class;
	}
		
		/**
		 * Metodo responsavel por retorna whiteList com base no numero de documeneto sirviço passado
		 * @param eventoDoctoServico
		 * @return List<Object[]> - lista de whitelist
		 */
	
	@SuppressWarnings("unchecked")
	public List<WhiteList> findWhiteListById(Long idWhiteList) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT wl FROM ");
		hql.append(  WhiteList.class.getName() + " wl ,");
		hql.append(  Contato.class.getName()+" c ,");
		hql.append(  DoctoServico.class.getName()+" dcto ,");
		hql.append(  Filial.class.getName()+" f ");
		hql.append(" WHERE ");
		hql.append(" c.idContato = wl.contato.idContato ");
		hql.append(" AND wl.doctoServico.idDoctoServico = dcto.idDoctoServico ");
		hql.append(" AND dcto.filialByIdFilialOrigem.idFilial = f.idFilial ");
		hql.append(" AND c.idContato NOT IN ( ");
		hql.append(" SELECT black.contato.idContato FROM ");
		hql.append(  BlackList.class.getName()+ " black WHERE black.contato.idContato = c.idContato ) ");
		hql.append(" AND wl.situacaoWhiteList = '" + TrackingContantsUtil.WHITE_LIST_SITUACAO_ATIVO + "' ");
		hql.append(" AND wl.idWhiteList = ?");

		return  (List<WhiteList>) getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idWhiteList});
	}
	

	@SuppressWarnings("unchecked")
	public List<WhiteList> findWhiteListByDoctoServico(Long idDoctoServico) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT wl FROM ");
		hql.append(  WhiteList.class.getName() + " wl ,");
		hql.append(  Contato.class.getName()+" c ,");
		hql.append(  DoctoServico.class.getName()+" dcto ,");
		hql.append(  Filial.class.getName()+" f ");
		hql.append(" WHERE ");
		hql.append(" c.idContato = wl.contato.idContato ");
		hql.append(" AND wl.doctoServico.idDoctoServico = dcto.idDoctoServico ");
		hql.append(" AND dcto.filialByIdFilialOrigem.idFilial = f.idFilial ");
		hql.append(" AND c.idContato NOT IN ( ");
		hql.append(" SELECT black.contato.idContato FROM ");
		hql.append(  BlackList.class.getName()+ " black WHERE black.contato.idContato = c.idContato ) ");
		hql.append(" AND wl.situacaoWhiteList = '" + TrackingContantsUtil.WHITE_LIST_SITUACAO_ATIVO + "' ");
		hql.append(" AND wl.doctoServico.idDoctoServico = ?");
		
		return  (List<WhiteList>) getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idDoctoServico});
	}

	public WhiteList findWhiteListBy(Long idDoctoServico, String email, DomainValue tpWhiteList) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT wl FROM ");
		hql.append(WhiteList.class.getName() + " wl,  "+Contato.class.getName()+" c");
		hql.append(" WHERE ");
		hql.append(" c.idContato = wl.contato.idContato ");
		hql.append(" AND c.dsEmail = ?");
		hql.append(" AND wl.tpWhiteList = ?");
		hql.append(" AND wl.doctoServico.idDoctoServico = ?");

		 List<WhiteList> lstWhiteList = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{email, tpWhiteList, idDoctoServico});
		 
		 return (lstWhiteList != null && lstWhiteList.size() > 0 ?lstWhiteList.get(0): null);
	}
	
	@SuppressWarnings("unchecked")
	public WhiteList findWhiteListByDoctoServicoContatoTipo(Long idDoctoServico, Long idContato, DomainValue tipo) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT wl FROM ");
		hql.append(  WhiteList.class.getName() + " wl ,");
		hql.append(  Contato.class.getName()+" c ,");
		hql.append(  DoctoServico.class.getName()+" dcto ,");
		hql.append(  Filial.class.getName()+" f ");
		hql.append(" WHERE ");
		hql.append(" c.idContato = wl.contato.idContato ");
		hql.append(" AND wl.doctoServico.idDoctoServico = dcto.idDoctoServico ");
		hql.append(" AND dcto.filialByIdFilialOrigem.idFilial = f.idFilial ");
		hql.append(" AND wl.doctoServico.idDoctoServico = ? ");
		hql.append(" AND c.idContato = ? ");
		hql.append(" AND wl.tpWhiteList = ? ");
		
		List<WhiteList> lstWhiteList = (List<WhiteList>) getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idDoctoServico, idContato, tipo});
		
		return (lstWhiteList != null && !lstWhiteList.isEmpty() ? lstWhiteList.get(0): null);
	}	
	
}
