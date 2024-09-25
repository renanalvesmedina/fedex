package com.mercurio.lms.rnc.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.model.hibernate.ProjectionsVarcharI18n;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.rnc.model.MotivoAberturaNc;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;
import com.mercurio.lms.rnc.model.util.ConstantesMotivoAberturaNc;
import com.mercurio.lms.sim.model.MonitoramentoCCT;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean 
 */
public class MotivoAberturaNcDAO extends BaseCrudDao<MotivoAberturaNc, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MotivoAberturaNc.class;
    }

    public List findMotivoAberturaNcBySetor(Long idSetor) {
		DetachedCriteria dc = DetachedCriteria
				.forClass(MotivoAberturaNc.class, "motivoAbertura")
      	.setProjection(
						Projections
								.projectionList()
								.add(Projections.property("idMotivoAberturaNc"),
										"idMotivoAberturaNc")
								.add(ProjectionsVarcharI18n
										.propertyI18n("dsMotivoAbertura"),
										"dsMotivoAbertura"))
        
        .setFetchMode("setorMotivoAberturaNcs",FetchMode.SELECT)
        .setFetchMode("setorMotivoAberturaNcs.setor",FetchMode.JOIN)

				.createAlias("motivoAbertura.setorMotivoAberturaNcs",
						"setorMotivoAberturaNcs")
        .createAlias("setorMotivoAberturaNcs.setor", "setor")

        .add(Restrictions.eq("setor.id", idSetor))
        .add(Restrictions.eq("motivoAbertura.tpSituacao", "A"))
				.addOrder(
						OrderVarcharI18n.asc("motivoAbertura.dsMotivoAbertura",
								LocaleContextHolder.getLocale()));

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(
				MotivoAberturaNc.class));
	    List result = super.findByDetachedCriteria(dc);
    	return result;
    }

    /**
	 * Obtém a quantidade de Motivos de Abertura de Nao Conformidadde de acordo
	 * com o idNaoConformidade e o atributo blPermissao
	 * 
     * @param idNaoConformidade
     * @param blPermissao
     * @return
     */
	public Integer getRowCountMotivosAberturaByIdNaoConformidadeBlPermissao(
			Long idNaoConformidade, Boolean blPermissao) {
    	StringBuffer sb = new StringBuffer()
				.append("from " + OcorrenciaNaoConformidade.class.getName()
						+ " onc ").append("join onc.motivoAberturaNc manc ")
    	.append("where onc.naoConformidade.id = ? ")
    	.append("and manc.blPermiteIndenizacao = ? ");
		return getAdsmHibernateTemplate().getRowCountForQuery(sb.toString(),
				new Object[] { idNaoConformidade, blPermissao });
    }
    
	public List<Long> findDoctoServicoMCCT(long idDoctoServico) {
		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT  ds.idDoctoServico as doctoServico ")
				.append(" FROM " + MonitoramentoCCT.class.getName() + " mcct ")
				.append(" INNER JOIN  mcct.doctoServico ds ")
				.append(" WHERE  ds.idDoctoServico = ? ");

		List param = new ArrayList();
		param.add(idDoctoServico);

		return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());

	}
	
	
	public List<String> findNrChavePorNr(long idDoctoServico) {
	
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT NFX.XML_CHAVE_NFE ");
			sql.append("FROM NFE_IDX NFX ");
			sql.append("INNER JOIN MONITORAMENTO_CCT MCCT ON MCCT.NR_CHAVE = NFX.XML_CHAVE_NFE ");
			sql.append("WHERE MCCT.ID_DOCTO_SERVICO =");
			sql.append(idDoctoServico);

			return getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
	}
	public List findNrChavePorId(long idDoctoServico) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT NFX.XML_CHAVE_NFE ");
		sql.append("FROM NFE_IDX NFX ");
		sql.append("INNER JOIN MONITORAMENTO_CCT MCCT ON MCCT.NR_CHAVE = NFX.XML_CHAVE_NFE ");
		sql.append("INNER JOIN DOCTO_SERVICO DS ON MCCT.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO ");
		sql.append("WHERE DS.ID_DOCTO_SERVICO =");
		sql.append(idDoctoServico);
		
		return getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();	
	}
	
	@SuppressWarnings("unchecked")
	public MotivoAberturaNc findMotivoAberturaNcByIdDoctoServico(Long idDoctoServico) {
		List<Long> idsMotivosAbertura = new ArrayList<Long>();
		idsMotivosAbertura.add(ConstantesMotivoAberturaNc.ID_AVARIA_PARCIAL);
		idsMotivosAbertura.add(ConstantesMotivoAberturaNc.ID_AVARIA_TOTAL);
		idsMotivosAbertura.add(ConstantesMotivoAberturaNc.ID_FALTA_PARCIAL);
		idsMotivosAbertura.add(ConstantesMotivoAberturaNc.ID_FALTA_TOTAL);
		idsMotivosAbertura.add(ConstantesMotivoAberturaNc.ID_TROCA_MERCADORIA);
		
		List<String> tpsStatusNaoConformidade = new ArrayList<String>();
		tpsStatusNaoConformidade.add("CANC");
		tpsStatusNaoConformidade.add("AGP");
		
		TypedFlatMap params = new TypedFlatMap();
		params.put("idsMotivosAbertura", idsMotivosAbertura);
		params.put("tpsStatusNaoConformidade", tpsStatusNaoConformidade);
		params.put("idDoctoServico", idDoctoServico);
		params.put("tpStatusOcorrenciaNc", "F");
		
		StringBuilder sql = new StringBuilder();
		
		sql
		.append("SELECT manc ")
		.append("FROM ")
		.append(MotivoAberturaNc.class.getSimpleName()).append(" manc ")
		.append("JOIN manc.ocorrenciaNaoConformidades onc ")
		.append("JOIN onc.naoConformidade nc ")
		.append("JOIN nc.doctoServico ds ")
		.append("WHERE ")
		.append(" 	 manc.idMotivoAberturaNc in(:idsMotivosAbertura) ")
		.append("AND nc.tpStatusNaoConformidade not in(:tpsStatusNaoConformidade) ")
		.append("AND onc.tpStatusOcorrenciaNc =:tpStatusOcorrenciaNc ")
		.append("AND ds.id =:idDoctoServico ")
		;
		
		List<MotivoAberturaNc> l = getAdsmHibernateTemplate().findByNamedParam(sql.toString(), params);
		
		if (CollectionUtils.isNotEmpty(l)) {
			return l.get(0);
		}
		
		return null;
	}
}