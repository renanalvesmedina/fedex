package com.mercurio.lms.carregamento.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.IntegranteEqOperac;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class IntegranteEqOperacDAO extends BaseCrudDao<IntegranteEqOperac, Long> {

    /**
     * Nome da classe que o DAO é responsável por persistir.
     */
    protected final Class getPersistentClass() {
        return IntegranteEqOperac.class;
    }
    
    protected void initFindByIdLazyProperties(Map map) {
    	map.put("usuario", FetchMode.JOIN);
    	map.put("usuario.vfuncionario", FetchMode.JOIN);
    	map.put("pessoa", FetchMode.JOIN);
    	map.put("empresa", FetchMode.JOIN);
    	map.put("empresa.pessoa", FetchMode.JOIN);
    	map.put("cargoOperacional", FetchMode.JOIN);
    	map.put("equipeOperacao", FetchMode.JOIN);
    }

    protected void initFindPaginatedLazyProperties(Map map) {
    	map.put("usuario", FetchMode.JOIN);
    	map.put("usuario.vfuncionario", FetchMode.JOIN);
    	map.put("pessoa", FetchMode.JOIN);
        map.put("empresa", FetchMode.JOIN);
        map.put("empresa.pessoa", FetchMode.JOIN);
        map.put("cargoOperacional", FetchMode.JOIN);
        map.put("equipeOperacao", FetchMode.JOIN);
    }
    
    
    /**
     * Busca todos o integrantes de uma equipe operacao pelo id de sua equipe
     * 
     * @param idEquipeOperacao
     * @return
     */
    public List findIntegranteEqOperacByIdEquipeOp(Long idEquipeOperacao) {
    	return this.findByDetachedCriteria(addSqlLiberacaoRiscoDadosEquipe(idEquipeOperacao));
    }

    /**
     * 
     * @param idEquipeOperacao
     * @return
     */
	private DetachedCriteria addSqlLiberacaoRiscoDadosEquipe(Long idEquipeOperacao) {
		DetachedCriteria dc = DetachedCriteria.forClass(IntegranteEqOperac.class)
                .setFetchMode("usuario",FetchMode.JOIN)
                .setFetchMode("usuario.vfuncionario",FetchMode.JOIN)
                .setFetchMode("pessoa", FetchMode.JOIN)
                .setFetchMode("empresa", FetchMode.JOIN)
                .setFetchMode("empresa.pessoa", FetchMode.JOIN)
                .setFetchMode("cargoOperacional", FetchMode.JOIN)
                .setFetchMode("equipeOperacao", FetchMode.JOIN)
                .add(Restrictions.eq("equipeOperacao.idEquipeOperacao",idEquipeOperacao))
                .add(Restrictions.or(Restrictions.isNotNull("usuario"), Restrictions.isNotNull("pessoa")));
		return dc;
	}

    
    /**
     * Busca os dados da equipeOperacao informada.
     * @param idIntegranteEquipeOperacao
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedLiberacaoRiscoDadosEquipe(Long idEquipeOperacao, FindDefinition fd) {
        DetachedCriteria dc = addSqlLiberacaoRiscoDadosEquipe(idEquipeOperacao);

        ResultSetPage rspRetorno = super.findPaginatedByDetachedCriteria(dc, fd.getCurrentPage(),fd.getPageSize());
        return rspRetorno;
    }

    /**
     * Row count para o método findPaginatedLiberacaoRiscoDadosEquipe()
     * @param idEquipeOperacao
     * @param fd
     * @return
     */
    public Integer getRowCountLiberacaoRiscoDadosEquipe(Long idEquipeOperacao) {
        DetachedCriteria dc = addSqlLiberacaoRiscoDadosEquipe(idEquipeOperacao);
        dc.setProjection(Projections.rowCount());
        List result = super.findByDetachedCriteria(dc);
        return (Integer) result.get(0);
    }
    

    
    
    /**
     * 
     * @param idEquipeOperacao
     * @return
     */
	private DetachedCriteria addSqlEmitirControleCargaDadosEquipe(Long idEquipeOperacao) {
		DetachedCriteria dc = DetachedCriteria.forClass(IntegranteEqOperac.class)
                .setFetchMode("usuario",FetchMode.JOIN)
                .setFetchMode("usuario.vfuncionario",FetchMode.JOIN)
                .setFetchMode("pessoa", FetchMode.JOIN)
                .setFetchMode("empresa", FetchMode.JOIN)
                .setFetchMode("empresa.pessoa", FetchMode.JOIN)
                .setFetchMode("cargoOperacional", FetchMode.JOIN)
                .setFetchMode("equipeOperacao", FetchMode.JOIN)
                .add(Restrictions.eq("equipeOperacao.id",idEquipeOperacao))
                .add(Restrictions.or(Restrictions.isNotNull("usuario"), Restrictions.isNotNull("pessoa")));
		return dc;
	}

	/**
	 * Busca os dados dos integrantes da equipe operação informada.
	 * 
	 * @param idEquipeOperacao
	 * @return
	 */
    public List findByEmitirControleCargaDadosEquipe(Long idEquipeOperacao) {
        DetachedCriteria dc = addSqlEmitirControleCargaDadosEquipe(idEquipeOperacao);
        List retorno = super.findByDetachedCriteria(dc);
        return retorno;
    }
    
    /**
     * Row count para o método findByEmitirControleCargaDadosEquipe()
     * 
     * @param idEquipeOperacao
     * @return
     */
    public Integer getRowCountEmitirControleCargaDadosEquipe(Long idEquipeOperacao) {
        DetachedCriteria dc = addSqlEmitirControleCargaDadosEquipe(idEquipeOperacao);
        dc.setProjection(Projections.rowCount());
        List result = super.findByDetachedCriteria(dc);
        return (Integer) result.get(0);
    }


    /**
     * 
     * @param map
     * @param param
     * @param isRowCount
     * @return
     */
    private String addSqlByIdEquipeOperacao(Long idEquipeOperacao, List param, boolean isRowCount) {
    	StringBuffer sql = new StringBuffer();
		if (!isRowCount) {
	    	sql.append("select new map(ieo.idIntegranteEqOperac as idIntegranteEqOperac, ")
			.append("ieo.tpIntegrante as tpIntegrante, ")
			.append("usuario.idUsuario as usuario_idUsuario, ")
			.append("usuario.nmUsuario as usuario_nmUsuario, ")
			.append("usuario.nrMatricula as usuario_nrMatricula, ")
			.append("pessoa.idPessoa as pessoa_idPessoa, ")
			.append("pessoa.nrIdentificacao as pessoa_nrIdentificacao, ")
			.append("pessoa.tpIdentificacao as pessoa_tpIdentificacao, ")
			.append("pessoa.nmPessoa as pessoa_nmPessoa, ")
			.append("pessoa.dhInclusao as pessoa_dhInclusao, ")
			.append("pessoa.tpPessoa as pessoa_tpPessoa, ")
			.append("cargoOperacional.idCargoOperacional as cargoOperacional_idCargoOperacional, ")
			.append("cargoOperacional.dsCargo as cargoOperacional_dsCargo, ")
	    	.append("empresa.idEmpresa as empresa_idEmpresa, ")
	    	.append("empresa.tpSituacao as empresa_tpSituacao, ")
	    	.append("pessoaEmpresa.nmPessoa as empresa_pessoa_nmPessoa ")
			.append(") ");
		}
		sql.append("from ")
		.append(IntegranteEqOperac.class.getName()).append(" as ieo ")
		.append("left join ieo.usuario as usuario ")
		.append("left join ieo.pessoa as pessoa ")
		.append("left join ieo.empresa as empresa ")
		.append("left join empresa.pessoa as pessoaEmpresa ")
		.append("left join ieo.cargoOperacional as cargoOperacional ")
		.append("where ")
		.append("ieo.equipeOperacao.id = ? ")
		.append("order by usuario.nmUsuario ");

		param.add(idEquipeOperacao);
		return sql.toString();
    }
    

    /**
     * 
     * @param idEquipeOperacao
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedByIdEquipeOperacao(Long idEquipeOperacao, FindDefinition findDefinition) {
		List param = new ArrayList();
		String sql = addSqlByIdEquipeOperacao(idEquipeOperacao, param, false);
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql, findDefinition.getCurrentPage(), findDefinition.getPageSize(), param.toArray());
	    return rsp;
	    	
    }

    /**
     * 
     * @param idEquipeOperacao
     * @return
     */
    public Integer getRowCountByIdEquipeOperacao(Long idEquipeOperacao) {
		List param = new ArrayList();
		String sql = addSqlByIdEquipeOperacao(idEquipeOperacao, param, true);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql, param.toArray());
    }
}