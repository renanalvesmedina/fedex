package com.mercurio.lms.entrega.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RegistrarEntregasParceirosDAO extends AdsmDao {

	public ControleCarga findControleCargaByMeioTransporte(Long idMeioTransporte, Long idFilial, String tpStatusControleCarga) {
		DetachedCriteria dc = DetachedCriteria.forClass(ControleCarga.class,"CC")
			.createAlias("CC.meioTransporteByIdTransportado","MT")
			.createAlias("CC.filialByIdFilialOrigem","F")
			.add(Restrictions.eq("CC.tpStatusControleCarga",tpStatusControleCarga))
			.add(Restrictions.eq("MT.id",idMeioTransporte))
			.add(Restrictions.eq("F.id",idFilial))
			.addOrder(Order.desc("CC.id"));
		List<ControleCarga> list = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		if (!list.isEmpty())
			return list.get(0);
		return null;
	}

	public List<Manifesto> findManifestoByMeioTransporte(Long idControleCarga,String tpStatusControleCarga, String tpStatusManifesto) {
		DetachedCriteria dc = DetachedCriteria.forClass(Manifesto.class,"M")
			.createAlias("M.controleCarga","CC")
			.add(Restrictions.eq("CC.id",idControleCarga))
			.add(Restrictions.eq("CC.tpStatusControleCarga",tpStatusControleCarga))
			.add(Restrictions.ne("M.tpStatusManifesto",tpStatusManifesto));
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public EventoMeioTransporte findLastEvetoMeioTransporteByIdMeioTranspote(Long idMeioTransporte,Long idFilial) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(EventoMeioTransporte.class.getName(),"EMT");
		hql.addCriteria("EMT.meioTransporte.id","=",idMeioTransporte);
		hql.addCriteria("EMT.filial.id","=",idFilial);
		hql.addCustomCriteria(new StringBuilder("EMT.dhInicioEvento.value = (select max(E2.dhInicioEvento.value) from ").append(EventoMeioTransporte.class.getName()).append(" AS E2 ")
				.append("WHERE E2.meioTransporte.id = ? and E2.filial.id = ?)").toString());
		hql.addCriteriaValue(idMeioTransporte);
		hql.addCriteriaValue(idFilial);
		return (EventoMeioTransporte)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		StringBuilder hql = new StringBuilder();
		Object[] params = montHQL(hql, criteria);

		return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), params);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select new Map(");
		hql.append("   med.id as idManifestoEntregaDocumento,");
		hql.append("   con.id as doctoServico_idDoctoServico,");
		hql.append("   con.nrDoctoServico as doctoServico_nrDoctoServico,");
		hql.append("   con.tpDocumentoServico as doctoServico_tpDocumentoServico,");
		hql.append("   con.dtPrevEntrega as doctoServico_dtPrevEntrega,");
		hql.append("   con.dsEnderecoEntregaReal as doctoServico_dsEnderecoEntregaReal,");
		hql.append("   con.clienteByIdClienteConsignatario.id as doctoServico_clienteConsignatario_idCliente,");
		hql.append("   fo.id as doctoServico_filialOrigem_idFilial,");
		hql.append("   fo.sgFilial as doctoServico_filialOrigem_sgFilial,");
		hql.append("   pcd.nmPessoa as destinatario_pessoa_nmPessoa,");
		hql.append("   pcd.nrIdentificacao as destinatario_pessoa_nrIdentificacao,");
		hql.append("   pcd.tpIdentificacao as destinatario_pessoa_tpIdentificacao,");
		hql.append("   me.id as manifestoEntrega_idManifestoEntrega,");
		hql.append("   me.nrManifestoEntrega as manifestoEntrega_nrManifestoEntrega,");
		hql.append("   fm.id as manifestoEntrega_filial_idFilial,");
		hql.append("   fm.sgFilial as manifestoEntrega_filial_sgFilial,");
		hql.append("   pmd.nrOrdem as preManifestoDocto_nrOrdem,");
		hql.append("   awb.id as awb_id,");
		hql.append("   cia.nmPessoa as cia_aerea");
		hql.append(" ) ");

		Object[] params = montHQL(hql, criteria);


	    String tpOrdemDoc = criteria.getString("doctoServico.filialByIdFilialOrigem.tpOrdemDoc");
	    
	    //Verifica qual tipo de ordenação para listagem 
	    if(tpOrdemDoc != null && !"".equals(tpOrdemDoc)) {
	    	if(tpOrdemDoc.equals("DA")) {
	    		hql.append("order by  con.dhInclusao asc");
	    	}
	    	else if(tpOrdemDoc.equals("AA")){
	    	hql.append(" order by  con.tpDocumentoServico asc, ");	    	
	    	hql.append("fo.sgFilial asc, ");
	    	hql.append("con.nrDoctoServico asc"); 	
	    	}
	    }
		
		return getAdsmHibernateTemplate().findPaginated(hql.toString(), findDef.getCurrentPage(), findDef.getPageSize(), params);
	}

	private Object[] montHQL(StringBuilder hql, TypedFlatMap criteria) {
		List<Object> paramValues = new ArrayList<Object>();
		
		hql.append(" from Conhecimento as con");
		hql.append(" inner join con.manifestoEntregaDocumentos as med");
		hql.append("  left join med.ocorrenciaEntrega as oe");
		hql.append(" inner join med.manifestoEntrega as me");
		hql.append(" inner join me.manifesto as m");
		hql.append(" inner join m.preManifestoDocumentos as pmdm");
		hql.append(" inner join me.filial as fm");
		hql.append(" inner join con.localizacaoMercadoria as lm");
		hql.append(" inner join con.filialByIdFilialOrigem as fo");
		hql.append(" inner join con.clienteByIdClienteDestinatario as cd");
		hql.append(" inner join con.preManifestoDocumentos as pmd");
		hql.append(" inner join cd.pessoa as pcd");
		hql.append(" join m.controleCarga as cc");
		hql.append(" join cc.proprietario as pp");
		hql.append(" join pp.pessoa as pes");
		hql.append(" left join con.ctoAwbs as ctoawb");
		hql.append(" left join ctoawb.awb as awb");
		hql.append(" left join awb.ciaFilialMercurio as cfm");
		hql.append(" left join cfm.empresa as emp");
		hql.append(" left join emp.pessoa as cia");
		
		hql.append(" where cc.tpStatusControleCarga = ?");
		paramValues.add("TC");
		
		hql.append(" and m.tpStatusManifesto <> ?");
		paramValues.add("CA");

		hql.append(" and m.tpManifestoEntrega = ?");
		paramValues.add("EP");
		
		hql.append(" and oe.idOcorrenciaEntrega is null ");
		
		hql.append(" and pmd.id = pmdm.id");

		hql.append(" and con.blBloqueado = ?");
		paramValues.add(Boolean.FALSE);
		
		if(criteria.getLong("idAwb") != null) {
			paramValues.add(criteria.getLong("idAwb"));
			hql.append(" and awb.id = ? ");
		}
		
		if(criteria.getString("proprietarioNrIdentificacao") != null) {
			paramValues.add(criteria.getString("proprietarioNrIdentificacao"));
			hql.append(" and pes.nrIdentificacao = ? ");
		}

		if(criteria.getLong("consignatario.idCliente") != null) {
			paramValues.add(criteria.getLong("consignatario.idCliente"));
			hql.append(" and con.clienteByIdClienteConsignatario.id = ?");
		}
		if(criteria.getLong("remetente.idCliente") != null) {
			paramValues.add(criteria.getLong("remetente.idCliente"));
			hql.append(" and con.clienteByIdClienteRemetente.id = ?");
		}
		if(criteria.getLong("destinatario.idCliente") != null) {
			paramValues.add(criteria.getLong("destinatario.idCliente"));
			hql.append(" and cd.id = ?");
		}
		if(criteria.getLong("notaFiscalCliente.idDoctoServico") != null) {
			paramValues.add(criteria.getLong("notaFiscalCliente.idDoctoServico"));
			hql.append(" and con.id = ?");
		} 
		if(criteria.getLong("idDoctoServico") != null) {
			paramValues.add(criteria.getLong("idDoctoServico"));
			hql.append(" and con.id = ?");
		}
		if(StringUtils.isNotBlank(criteria.getString("doctoServico.tpDocumentoServico"))) {
			paramValues.add(criteria.getString("doctoServico.tpDocumentoServico"));
			hql.append(" and con.tpDocumentoServico = ?");
		}
		if(criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial") != null) {
			paramValues.add(criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial"));
			hql.append(" and fo.id = ?");
		}
		
		if(criteria.getInteger("manifestoEntrega.nrManifestoEntrega") !=null) {
			paramValues.add(criteria.getInteger("manifestoEntrega.nrManifestoEntrega"));
			hql.append(" and me.nrManifestoEntrega = ? ");
		}
		
		if(criteria.getLong("manifestoEntrega.idManifestoEntrega") !=null) {
			paramValues.add(criteria.getLong("manifestoEntrega.idManifestoEntrega"));
			hql.append(" and me.idManifestoEntrega = ? ");
		}

		return paramValues.toArray();
	}
}
