package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.ChecklistMeioTransporte;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ChecklistMeioTransporteDAO extends BaseCrudDao<ChecklistMeioTransporte, Long>
{

	
	
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ChecklistMeioTransporte.class;
    }

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(" +
				"pes.nmFantasia as descricaoFilial, " +
				"fil.sgFilial as sgFilial, " +
				"fil.sgFilial as sgFilialNrCheck, " +
				"fil.sgFilial as sgFilialNrSol, " +
				"check.idChecklistMeioTransporte as idChecklistMeioTransporte, " +
				"check.nrChecklist as nrChecklist, " +
				"sol.nrSolicitacaoContratacao as nrSolicitacaoContratacao, " +
				"tmt.tpMeioTransporte as tpMeioTransporte, " +
				"tmt.dsTipoMeioTransporte as dsTipoMeioTransporte, " +
				"sol.nrIdentificacaoMeioTransp as nrIdentificacaoMeioTransp, " +
				"check.dtRealizacao as dtRealizacao)");
		
		hql.addFrom(ChecklistMeioTransporte.class.getName(), new StringBuffer("check ")
				.append("join check.filial fil ")
    			.append("left outer join fil.pessoa pes ")
    			.append("join check.solicitacaoContratacao sol ")
    			.append("join sol.tipoMeioTransporte tmt ")
    			.toString());
					
		hql.addCriteria("fil.idFilial", "=",criteria.getLong("filial.idFilial"));
		
		hql.addCriteria("check.nrChecklist", "=", criteria.getLong("nrChecklist"));
		
		hql.addCriteria("sol.idSolicitacaoContratacao", "=", criteria.getLong("solicitacaoContratacao.idSolicitacaoContratacao"));
		
		hql.addCriteria("sol.tpSolicitacaoContratacao", "=" , criteria.getString("tpSolicitacaoContratacao"));
		
		hql.addCriteria("tmt.tpMeioTransporte", "=",criteria.getString("tipoMeioTransporte.tpMeioTransporte"));
		
		hql.addCriteria("tmt.idTipoMeioTransporte", "=", criteria.getLong("tipoMeioTransporte.idTipoMeioTransporte"));
		
		hql.addCriteria("upper(sol.nrIdentificacaoMeioTransp)", "like", criteria.getString("solicitacaoContratacao.nrIdentificacaoMeioTransp").toUpperCase());
		
		hql.addCriteria("check.dtRealizacao", ">=" ,criteria.getYearMonthDay("dtRealizacaoInicial"));
		hql.addCriteria("check.dtRealizacao", "<=" ,criteria.getYearMonthDay("dtRealizacaoFinal"));
		
		
		hql.addOrderBy("fil.sgFilial");
		hql.addOrderBy("check.nrChecklist");
		hql.addOrderBy("sol.nrSolicitacaoContratacao");
		
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(),findDef.getCurrentPage(), findDef.getPageSize(), hql.getCriteria());
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addFrom(ChecklistMeioTransporte.class.getName(),  new StringBuffer("check ")
    			.append("join check.filial fil ")
    			.append("left outer join fil.pessoa pes ")
    			.append("join check.solicitacaoContratacao sol ")
    			.append("join sol.tipoMeioTransporte tmt ")
    			.toString());
		
				
		hql.addCriteria("fil.idFilial", "=",criteria.getLong("filial.idFilial"));
		
		hql.addCriteria("check.nrChecklist","=",criteria.getLong("nrChecklist"));
		
		hql.addCriteria("sol.idSolicitacaoContratacao", "=", criteria.getLong("solicitacaoContratacao.idSolicitacaoContratacao"));
		
		hql.addCriteria("sol.tpSolicitacaoContratacao", "=" , criteria.getString("tpSolicitacaoContratacao"));
		
		hql.addCriteria("tmt.tpMeioTransporte", "=",criteria.getString("tipoMeioTransporte.tpMeioTransporte"));
		
		hql.addCriteria("tmt.idTipoMeioTransporte", "=", criteria.getLong("tipoMeioTransporte.idTipoMeioTransporte"));
		
		hql.addCriteria("upper(sol.nrIdentificacaoMeioTransp)", "like", criteria.getString("solicitacaoContratacao.nrIdentificacaoMeioTransp").toUpperCase());
		
				
		hql.addCriteria("check.dtRealizacao", ">=" ,criteria.getYearMonthDay("dtRealizacaoInicial"));
		hql.addCriteria("check.dtRealizacao", "<=" ,criteria.getYearMonthDay("dtRealizacaoFinal"));
		
		
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(),hql.getCriteria());
	}
	
    //verifica se existe algum checklist associado a solicitacao de contratacao
	public boolean findCheckListByIdSolicitacao(Long idSolicitacaoContratacao){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("solicitacaoContratacao.idSolicitacaoContratacao",idSolicitacaoContratacao));
		return findByDetachedCriteria(dc).size()>0;
	}
	
	
	public Map findByIdCustom(Long id) {
		SqlTemplate hql = new SqlTemplate();
					
		hql.addProjection("new Map(CTM.idChecklistMeioTransporte","idChecklistMeioTransporte");
		hql.addProjection("CTM.nrChecklist","nrChecklist");
		
		hql.addProjection("F.idFilial","filial_idFilial");
		hql.addProjection("F.sgFilial","filial_sgFilial");
		hql.addProjection("P.nmFantasia","filial_pessoa_nmFantasia");
		
		hql.addProjection("FSC.idFilial","solicitacaoContratacao_filial_idFilial");
		hql.addProjection("FSC.sgFilial","solicitacaoContratacao_filial_sgFilial");
		hql.addProjection("PSC.nmFantasia","solicitacaoContratacao_filial_pessoa_nmFantasia");
		
		hql.addProjection("TMT.idTipoMeioTransporte","tipoMeioTransporte_idTipoMeioTransporte");
		hql.addProjection("ROTA.dsRota","rota_dsRota");
		hql.addProjection("TMT.tpMeioTransporte","tipoMeioTransporte_tpMeioTransporte");
		hql.addProjection("TMT.dsTipoMeioTransporte","tipoMeioTransporte_dsTipoMeioTransporte");
		
		hql.addProjection("TMT.tipoMeioTransporte.id","idTipoMeioTransporteSemiReboque");
		hql.addProjection("SC.idSolicitacaoContratacao","solicitacaoContratacao_idSolicitacaoContratacao");
		hql.addProjection("SC.nrSolicitacaoContratacao","solicitacaoContratacao_nrSolicitacaoContratacao");
		hql.addProjection("SC.tpSolicitacaoContratacao","tpSolicitacaoContratacao");
		hql.addProjection("SC.nrIdentificacaoMeioTransp","solicitacaoContratacao_nrIdentificacaoMeioTransp");
		hql.addProjection("SC.nrIdentificacaoSemiReboque","solicitacaoContratacao_nrIdentificacaoSemiReboque");
		
		hql.addProjection("PM1.idPessoa","pessoaByIdPrimeiroMotorista_idPessoa");
		hql.addProjection("PM1.tpIdentificacao","pessoaByIdPrimeiroMotorista_tpIdentificacao");
		hql.addProjection("PM1.nrIdentificacao","pessoaByIdPrimeiroMotorista_nrIdentificacao");
		hql.addProjection("PM1.nmPessoa","pessoaByIdPrimeiroMotorista_nmPessoa");
		hql.addProjection("PM1.tpPessoa","pessoaByIdPrimeiroMotorista_tpPessoa");
		hql.addProjection("PM1.dsEmail","pessoaByIdPrimeiroMotorista_dsEmail");
		hql.addProjection("PM1.nmPessoa","pessoaByIdPrimeiroMotorista_nmPessoa");
		
		hql.addProjection("PM2.idPessoa","pessoaByIdSegundoMotorista_idPessoa");
		hql.addProjection("PM2.tpIdentificacao","pessoaByIdSegundoMotorista_tpIdentificacao");
		hql.addProjection("PM2.nrIdentificacao","pessoaByIdSegundoMotorista_nrIdentificacao");
		hql.addProjection("PM2.nmPessoa","pessoaByIdSegundoMotorista_nmPessoa");
		hql.addProjection("PM2.tpPessoa","pessoaByIdSegundoMotorista_tpPessoa");
		hql.addProjection("PM2.dsEmail","pessoaByIdSegundoMotorista_dsEmail");
		hql.addProjection("PM2.nmPessoa","pessoaByIdSegundoMotorista_nmPessoa");
		
		hql.addProjection("USU.idUsuario","usuario_idUsuario");
		hql.addProjection("FUNC.nrMatricula","usuario_vfuncionario_nrMatricula");
		hql.addProjection("FUNC.nmFuncionario","usuario_vfuncionario_nmFuncionario");
		hql.addProjection("CTM.tpSituacao","tpSituacao");
		hql.addProjection("CTM.dtRealizacao","dtRealizacao)");
		
		String hqlFrom = new StringBuilder()
				.append(ChecklistMeioTransporte.class.getName()).append(" CTM ")
				.append(" inner join CTM.filial as F ")
				.append(" inner join F.pessoa as P ")
				.append(" inner join CTM.solicitacaoContratacao as SC ")
				.append("  left join SC.filial as FSC ")
				.append("  left join FSC.pessoa as PSC ")
				.append(" inner join SC.tipoMeioTransporte as TMT ")
				.append("  left join SC.rota as ROTA ")
				.append(" inner join CTM.pessoaByIdPrimeiroMotorista PM1 ")
				.append("  left join CTM.pessoaByIdSegundoMotorista PM2 ")
				.append(" inner join CTM.usuario USU ")
				.append("  left join USU.vfuncionario FUNC ")
				.toString();
		
		hql.addFrom(hqlFrom);
		
		hql.addCriteria("CTM.id","=",id);
		
		return (Map)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
	}


}