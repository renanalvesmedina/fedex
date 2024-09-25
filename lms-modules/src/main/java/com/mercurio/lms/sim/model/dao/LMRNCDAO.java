package com.mercurio.lms.sim.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.rnc.model.NaoConformidade;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;
/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class LMRNCDAO extends AdsmDao {
	
	public List findNaoConformidadeByIdDoctoServico(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(nc.nrNaoConformidade as nrNaoConformidade, " +
				"filial.sgFilial as sgFilial, " +
				"nc.tpStatusNaoConformidade as tpStatusNaoConformidade, " +
				"nc.idNaoConformidade as idNaoConformidade, " +
				"nc.dhEmissao as dhEmissao, " +
				"fil.sgFilial as sgFilialNaoConformidade, " +
				"fil.sgFilial as filResponsavel, " +
				"pes.nmFantasia as nmFantasiaResp )");
		
		hql.addFrom(NaoConformidade.class.getName() + " nc " +
				"join nc.filial filial " +
				"left outer join nc.doctoServico ds " +
				"join nc.filial fil " +
				"left outer join fil.pessoa pes ");
		
		hql.addRequiredCriteria("ds.idDoctoServico","=", idDoctoServico);
		
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	public List findPaginatedOcorrenciaNaoConformidade(Long idNaoConformidade){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(onc.nrOcorrenciaNc as nrOcorrenciaNc, " +
				""+PropertyVarcharI18nProjection.createProjection("manc.dsMotivoAbertura")+" as dsMotivoAbertura, " +
				"onc.qtVolumes as qtVolumes, " +
				"onc.vlOcorrenciaNc as vlOcorrenciaNc, " +
				"onc.idOcorrenciaNaoConformidade as idOcorrenciaNaoConformidade, " +
				"onc.dhInclusao as dhInclusao, " +
				"moeda.sgMoeda as sgMoeda, " +
				"moeda.dsSimbolo as dsSimbolo, " +
				"onc.filialByIdFilialAbertura.sgFilial as sgFilialNaoConform, " +
				"onc.tpStatusOcorrenciaNc as tpStatusOcorrenciaNc)");
		
		hql.addFrom(OcorrenciaNaoConformidade.class.getName()+ " onc " +
				"join onc.naoConformidade nc " +
				"join onc.motivoAberturaNc manc " +
				"left outer join onc.moeda moeda");
		
		hql.addCriteria("nc.idNaoConformidade","=", idNaoConformidade);
		
		hql.addOrderBy("onc.dhInclusao.value");
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

}
