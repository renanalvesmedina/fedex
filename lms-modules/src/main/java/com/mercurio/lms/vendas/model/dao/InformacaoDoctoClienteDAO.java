package com.mercurio.lms.vendas.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.InformacaoDoctoCliente;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class InformacaoDoctoClienteDAO extends BaseCrudDao<InformacaoDoctoCliente, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return InformacaoDoctoCliente.class;
	}

	protected void initFindByIdLazyProperties(Map arg0) {
		arg0.put("cliente", FetchMode.JOIN);
		arg0.put("cliente.pessoa", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map arg0) {
		arg0.put("cliente", FetchMode.JOIN);
		arg0.put("cliente.pessoa", FetchMode.JOIN);
	}

	public Integer getRowCountByClienteTpModalTpAbrangenciaByBlRemetente(Long idCliente, String tpModal, String tpAbrangencia, Boolean blRemetente) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "idc")
			.setProjection(Projections.rowCount())
			.add(Restrictions.eq("idc.cliente.id", idCliente))
			.add(Restrictions.eq("idc.blRemetente", blRemetente))
			.add(Restrictions.or(Restrictions.eq("idc.tpModal", tpModal), Restrictions.isNull("idc.tpModal")))
			.add(Restrictions.or(Restrictions.eq("idc.tpAbrangencia", tpAbrangencia), Restrictions.isNull("idc.tpAbrangencia")));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	public Integer getRowCountByClienteTpModalTpAbrangenciaByBlDestinatario(Long idCliente, String tpModal, String tpAbrangencia, Boolean blDestinatario) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "idc")
			.setProjection(Projections.rowCount())			
			.add(Restrictions.eq("idc.cliente.id", idCliente))			
			.add(Restrictions.eq("idc.blDestinatario", blDestinatario))
			.add(Restrictions.or(Restrictions.eq("idc.tpModal", tpModal), Restrictions.isNull("idc.tpModal")))
			.add(Restrictions.or(Restrictions.eq("idc.tpAbrangencia", tpAbrangencia), Restrictions.isNull("idc.tpAbrangencia")));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	public Integer getRowCountByClienteTpModalTpAbrangenciaByBlDevedor(Long idCliente, String tpModal, String tpAbrangencia, Boolean blDevedor) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "idc")
			.setProjection(Projections.rowCount())			
			.add(Restrictions.eq("idc.cliente.id", idCliente))			
			.add(Restrictions.eq("idc.blDevedor", blDevedor))
			.add(Restrictions.or(Restrictions.eq("idc.tpModal", tpModal), Restrictions.isNull("idc.tpModal")))
			.add(Restrictions.or(Restrictions.eq("idc.tpAbrangencia", tpAbrangencia), Restrictions.isNull("idc.tpAbrangencia")));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}
	
	/**
	 * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
	 * @param idCliente
	 * @param dsCampo
	 * @return InformacaoDoctoCliente
	 */
	public InformacaoDoctoCliente findInformacaoDoctoCliente(Long idCliente, String dsCampo) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("cliente", "c");
		dc.add(Restrictions.eq("c.idCliente", idCliente));
		dc.add(Restrictions.eq("dsCampo", dsCampo));
		return (InformacaoDoctoCliente) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public List findDadosByClienteTpModalTpAbrangencia(Long idCliente, String tpModal, String tpAbrangencia, Boolean blRemetente, Boolean blDestinatario, Boolean blDevedor) {
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("idc.idInformacaoDoctoCliente"), "idInformacaoDoctoCliente");
		projectionList.add(Projections.property("idc.dsCampo"), "dsCampo");
		projectionList.add(Projections.property("idc.tpCampo"), "tpCampo");
		projectionList.add(Projections.property("idc.dsFormatacao"), "dsFormatacao");
		projectionList.add(Projections.property("idc.nrTamanho"), "nrTamanho");
		projectionList.add(Projections.property("idc.blIndicadorNotaFiscal"), "blIndicadorNotaFiscal");
		projectionList.add(Projections.property("idc.blOpcional"), "blOpcional");
		projectionList.add(Projections.property("idc.blRemetente"), "blRemetente");
		projectionList.add(Projections.property("idc.blDestinatario"), "blDestinatario");
		projectionList.add(Projections.property("idc.blDevedor"), "blDevedor");
		projectionList.add(Projections.property("idc.tpSituacao"), "tpSituacao");
		projectionList.add(Projections.property("idc.dsValorPadrao"), "dsValorPadrao");
		projectionList.add(Projections.property("idc.blValorFixo"), "blValorFixo");
		
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "idc")
		.setProjection(projectionList)
		.add(Restrictions.eq("idc.cliente.id", idCliente))
		.add(Restrictions.or(
				Restrictions.eq("idc.tpModal", tpModal),
				Restrictions.isNull("idc.tpModal")))
		.add(Restrictions.or(
				Restrictions.eq("idc.tpAbrangencia", tpAbrangencia),
				Restrictions.isNull("idc.tpAbrangencia") ))
		.add(Restrictions.eq("idc.tpSituacao","A"));
		if(blRemetente != null) {
			if(blDevedor != null) {
				dc.add(Restrictions.or(
						Restrictions.eq("idc.blRemetente", blRemetente), 
						Restrictions.eq("idc.blDevedor", blDevedor)));
			} else {
				dc.add(Restrictions.eq("idc.blRemetente", blRemetente));
			}
		}
		if(blDestinatario != null) {
			if(blDevedor != null) {
				dc.add(Restrictions.or(
						Restrictions.eq("idc.blDestinatario", blDestinatario), 
						Restrictions.eq("idc.blDevedor", blDevedor)));
			} else {
				dc.add(Restrictions.eq("idc.blDestinatario", blDestinatario));
			}
		}
		if(blDevedor != null && blDestinatario == null && blRemetente == null) {
			dc.add(Restrictions.eq("idc.blDevedor", blDevedor));
		}
		dc.addOrder(Order.asc("idc.blIndicadorNotaFiscal"))
		.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		return findByDetachedCriteria(dc);
	}

	public List findDadosByClienteTpModalTpAbrangencia(Long idCliente, String tpModal, String tpAbrangencia) {
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("idc.idInformacaoDoctoCliente"), "idInformacaoDoctoCliente");
		projectionList.add(Projections.property("idc.dsCampo"), "dsCampo");
		projectionList.add(Projections.property("idc.tpCampo"), "tpCampo");
		projectionList.add(Projections.property("idc.dsFormatacao"), "dsFormatacao");
		projectionList.add(Projections.property("idc.nrTamanho"), "nrTamanho");
		projectionList.add(Projections.property("idc.blIndicadorNotaFiscal"), "blIndicadorNotaFiscal");
		projectionList.add(Projections.property("idc.blOpcional"), "blOpcional");
		projectionList.add(Projections.property("idc.blRemetente"), "blRemetente");
		projectionList.add(Projections.property("idc.blDestinatario"), "blDestinatario");
		projectionList.add(Projections.property("idc.blDevedor"), "blDevedor");
		projectionList.add(Projections.property("idc.tpSituacao"), "tpSituacao");
		projectionList.add(Projections.property("idc.dsValorPadrao"), "dsValorPadrao");
		projectionList.add(Projections.property("idc.blValorFixo"), "blValorFixo");
		
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "idc")
		.setProjection(projectionList)
		.add(Restrictions.eq("idc.cliente.id", idCliente))
		.add(Restrictions.or(
				Restrictions.eq("idc.tpModal", tpModal),
				Restrictions.isNull("idc.tpModal")))
		.add(Restrictions.or(
				Restrictions.eq("idc.tpAbrangencia", tpAbrangencia),
				Restrictions.isNull("idc.tpAbrangencia") ))
		.add(Restrictions.eq("idc.tpSituacao","A"))
		.addOrder(Order.asc("idc.blIndicadorNotaFiscal"))
		.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		return findByDetachedCriteria(dc);
	}

	public List findDadosByCliente(Long idCliente){
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("idc.idInformacaoDoctoCliente"), "idInformacaoDoctoCliente");
		projectionList.add(Projections.property("idc.dsCampo"), "dsCampo");
		projectionList.add(Projections.property("idc.tpCampo"), "tpCampo");
		projectionList.add(Projections.property("idc.dsFormatacao"), "dsFormatacao");
		projectionList.add(Projections.property("idc.nrTamanho"), "nrTamanho");
		projectionList.add(Projections.property("idc.blIndicadorNotaFiscal"), "blIndicadorNotaFiscal");
		projectionList.add(Projections.property("idc.blOpcional"), "blOpcional");
		projectionList.add(Projections.property("idc.blRemetente"), "blRemetente");
		projectionList.add(Projections.property("idc.blDestinatario"), "blDestinatario");
		projectionList.add(Projections.property("idc.blDevedor"), "blDevedor");
		projectionList.add(Projections.property("idc.tpSituacao"), "tpSituacao");
		projectionList.add(Projections.property("idc.dsValorPadrao"), "dsValorPadrao");
		projectionList.add(Projections.property("idc.blValorFixo"), "blValorFixo");
		
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "idc")
		.setProjection(projectionList)
		.add(Restrictions.eq("idc.cliente.id", idCliente))
		.add(Restrictions.eq("idc.tpSituacao","A"))
		.addOrder(Order.asc("idc.blIndicadorNotaFiscal"))
		.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		
		return findByDetachedCriteria(dc);				
	}	

	public List<Map> findDadosByCliente(Long idCliente, String descricaoCampo) {
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("idc.idInformacaoDoctoCliente"), "idInformacaoDoctoCliente");
		projectionList.add(Projections.property("idc.dsCampo"), "dsCampo");
		projectionList.add(Projections.property("idc.tpCampo"), "tpCampo");
		projectionList.add(Projections.property("idc.dsFormatacao"), "dsFormatacao");
		projectionList.add(Projections.property("idc.nrTamanho"), "nrTamanho");
		projectionList.add(Projections.property("idc.blIndicadorNotaFiscal"), "blIndicadorNotaFiscal");
		projectionList.add(Projections.property("idc.blOpcional"), "blOpcional");
		projectionList.add(Projections.property("idc.tpSituacao"), "tpSituacao");
		projectionList.add(Projections.property("idc.dsValorPadrao"), "dsValorPadrao");
		projectionList.add(Projections.property("idc.blValorFixo"), "blValorFixo");
		
		
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "idc")
		.setProjection(projectionList)
		.add(Restrictions.eq("idc.cliente.id", idCliente))		
		.add(Restrictions.eq("idc.dsCampo", descricaoCampo))		
		.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		return findByDetachedCriteria(dc);
	}
	
	public List findIdsByClienteTpModalTpAbrangenciaBlOpcional(Long idCliente, String tpModal, String tpAbrangencia, Boolean blOpcional) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "idc")
		.setProjection(Projections.property("idc.idInformacaoDoctoCliente"))
		.add(Restrictions.eq("idc.cliente.id", idCliente))
		.add(Restrictions.eq("idc.blOpcional", blOpcional))
		.add(Restrictions.or(
				Restrictions.eq("idc.tpModal", tpModal),
				Restrictions.isNull("idc.tpModal")))
		.add(Restrictions.or(
				Restrictions.eq("idc.tpAbrangencia", tpAbrangencia),
				Restrictions.isNull("idc.tpAbrangencia") ));
		return findByDetachedCriteria(dc);
	}

	public InformacaoDoctoCliente findByIdClienteAndDsCampo(Long idCliente, String dsCampo) {
		StringBuilder queryString = new StringBuilder();
		queryString
		.append("SELECT idc ")
		.append("FROM ")
		.append(InformacaoDoctoCliente.class.getSimpleName()).append(" idc ")
		.append("JOIN idc.cliente c ")
		.append("WHERE ")
		.append("	 c.idCliente =:idCliente ")
		.append("AND idc.dsCampo =:dsCampo")
		;
		
		Map<String, Object> parameterValues = new HashMap<String, Object>();
		parameterValues.put("idCliente", idCliente);
		parameterValues.put("dsCampo", dsCampo);
		
		return (InformacaoDoctoCliente) getAdsmHibernateTemplate().findUniqueResult(queryString.toString(), parameterValues);
	}

	public InformacaoDoctoCliente findByIdInformacaoDoctoClienteAndDsCampo(Long idInformacaoDoctoCliente, String dsCampo){
		StringBuilder queryString = new StringBuilder();
		queryString
		.append("SELECT idc ")
		.append("FROM ")
		.append(InformacaoDoctoCliente.class.getSimpleName()).append(" idc ")
		.append("WHERE ")
		.append("idc.idInformacaoDoctoCliente = :idInformacaoDoctoCliente ")
		.append("AND idc.dsCampo =:dsCampo")
		;
		
		Map<String, Object> parameterValues = new HashMap<String, Object>();
		parameterValues.put("idInformacaoDoctoCliente", idInformacaoDoctoCliente);
		parameterValues.put("dsCampo", dsCampo);
		
		return (InformacaoDoctoCliente) getAdsmHibernateTemplate().findUniqueResult(queryString.toString(), parameterValues);
	}
	
	// LMSA-6520
	public InformacaoDoctoCliente findByNrIdentificacaoClienteAndDsCampo(String nrIdentificacao, String dsCampo) {
		StringBuilder queryString = new StringBuilder();
		queryString
		.append("SELECT idc ")
		.append("FROM ")
		.append(InformacaoDoctoCliente.class.getSimpleName()).append(" idc ")
		.append("JOIN idc.cliente c ")
		.append("WHERE ")
		.append("	 c.pessoa.nrIdentificacao = :nrIdentificacao ")
		.append("AND idc.dsCampo =:dsCampo")
		;
		
		Map<String, Object> parameterValues = new HashMap<String, Object>();
		parameterValues.put("nrIdentificacao", nrIdentificacao);
		parameterValues.put("dsCampo", dsCampo);
		
		return (InformacaoDoctoCliente) getAdsmHibernateTemplate().findUniqueResult(queryString.toString(), parameterValues);
	}



}
