package com.mercurio.lms.sgr.model.dao;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.sgr.model.SolicitacaoSinal;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class SolicitacaoSinalDAO extends BaseCrudDao<SolicitacaoSinal, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return SolicitacaoSinal.class;
    }

    /**
     * Retorna um DetachedCriteria para a pesquisa "SolicitacaoSinal".
     * 
     * @param map
     * @return
     */
    private SqlTemplate getDetachedCriteriaBySolicitacaoSinal(TypedFlatMap map) {
		SqlTemplate sql = new SqlTemplate();
		
		StringBuffer projecao = new StringBuffer()
			.append("distinct new map(ss.idSolicitacaoSinal as idSolicitacaoSinal, ")
			.append("ss.dhGeracao as dhGeracao, ")
			.append("ss.nrSolicitacaoSinal as nrSolicitacaoSinal, ")
			.append("ss.nrRastreador as nrRastreador, ")
			.append("ss.nrTelefoneEmpresa as nrTelefoneEmpresa, ")
			.append("ss.nmEmpresaAnterior as nmEmpresaAnterior, ")
			.append("ss.nmResponsavelEmpresa as nmResponsavelEmpresa, ")
			.append("ss.blPertProjCaminhoneiro as blPertProjCaminhoneiro, ")
			.append("ss.obSolicitacaoSinal as obSolicitacaoSinal, ")
			.append("ss.tpStatusSolicitacao as tpStatusSolicitacao, ")
			.append("controleCarga.idControleCarga as idControleCarga, ")
			.append("meioTransporte.nrFrota as meioTransporte_nrFrota, ")
			.append("meioTransporte.nrIdentificador as meioTransporte_nrIdentificador, ")
			.append("operadoraMct_pessoa.nmPessoa as operadoraMct_pessoa_nmPessoa, ")
			.append("proprietario_pessoa.nmPessoa as proprietario_pessoa_nmPessoa, ")
			.append("filial.sgFilial as filial_sgFilial, ")
			.append("filial_pessoa.nmFantasia as filial_pessoa_nmFantasia)");
				
		StringBuffer from = new StringBuffer()
			.append(SolicitacaoSinal.class.getName())
			.append(" as ss ")
			.append("join ss.controleCarga as controleCarga ")
			.append("join ss.meioTransporte as meioTransporte ")			
			.append("left join ss.meioTransporteByIdSemiReboque as meioTransporteByIdSemiReboque ")
			.append("join ss.operadoraMct as operadoraMct ")
			.append("join operadoraMct.pessoa as operadoraMct_pessoa ")
			.append("join ss.motorista as motorista ")
			.append("join ss.proprietario as proprietario ")
			.append("join proprietario.pessoa as proprietario_pessoa ")			
			.append("join ss.filial as filial ")
			.append("join filial.pessoa as filial_pessoa ");
	
		sql.addProjection(projecao.toString());
		sql.addFrom(from.toString());
		
		if (map.getString("sgFilial") != null) {
	 		sql.addCriteria("filial.sgFilial", "like", map.getString("sgFilial").toUpperCase());
	 	}
		if (map.getInteger("nrSolicitacaoSinal") != null) {
	 		sql.addCriteria("ss.nrSolicitacaoSinal", "=", map.getInteger("nrSolicitacaoSinal"));
	 	}
	 	if (map.getYearMonthDay("dataInicial") != null) {
	 		sql.addCriteria("trunc(cast (ss.dhGeracao.value as date))", ">=" , map.getYearMonthDay("dataInicial"));
	 	}
	 	if (map.getYearMonthDay("dataFinal") != null) {
	 		sql.addCriteria("trunc(cast (ss.dhGeracao.value as date))", "<=" , map.getYearMonthDay("dataFinal"));
	 	}		
		if (map.getInteger("nrRastreador") != null) {
	 		sql.addCriteria("ss.nrRastreador", "=", map.getInteger("nrRastreador"));
	 	}
		if (map.getLong("meioTransporte.idMeioTransporte") != null) {
	 		sql.addCriteria("meioTransporte.id", "=", map.getLong("meioTransporte.idMeioTransporte"));
	 	}
		if (map.getLong("filial.idFilial") != null) {
	 		sql.addCriteria("filial.id", "=", map.getLong("filial.idFilial"));
	 	}
		if (map.getLong("operadoraMct.idOperadoraMct") != null) {
	 		sql.addCriteria("operadoraMct.id", "=", map.getLong("operadoraMct.idOperadoraMct"));
	 	}
		if (map.getLong("proprietario.idProprietario") != null) {
	 		sql.addCriteria("proprietario.id", "=", map.getLong("proprietario.idProprietario"));
	 	}
		if (map.getLong("controleCarga.idControleCarga") != null) {
	 		sql.addCriteria("controleCarga.id", "=", map.getLong("controleCarga.idControleCarga"));
	 	}
		if (map.getLong("meioTransporteByIdSemiReboque.idMeioTransporteByIdSemiReboque") != null) {
	 		sql.addCriteria("meioTransporteByIdSemiReboque.id", "=", map.getLong("meioTransporteByIdSemiReboque.idMeioTransporteByIdSemiReboque"));
	 	}
		if (map.getLong("motorista.idMotorista") != null) {
	 		sql.addCriteria("motorista.id", "=", map.getLong("motorista.idMotorista"));
	 	}
		if (map.getDomainValue("tpStatusSolicitacao") != null) {
	 		sql.addCriteria("ss.tpStatusSolicitacao", "=", map.getDomainValue("tpStatusSolicitacao").getValue());
	 	}
		

	 	return sql;
    }    
    
    
    /**
     * Faz a consulta ao banco, retornando o numero de registros encontrados para determinados 
     * parametros.
     * 
     * @param map
     * @return Integer com o numero de registos com os dados da grid.
     */
    public Integer getRowCountBySolicitacaoSinal(TypedFlatMap map) {
    	SqlTemplate sql = getDetachedCriteriaBySolicitacaoSinal(map);
    	List result = getAdsmHibernateTemplate().find("select count(distinct ss.idSolicitacaoSinal) as rowCount " + sql.getSql(false),sql.getCriteria());
    	Long r = (Long)result.get(0);    	
    	return Integer.valueOf(r.intValue()); 
    }   
    
    /**
     * Retorna um map com os objetos a serem mostrados na grid.
     * 
     * @param TypedFlatMap map
     * @param FindDefinition findDefinition
     * @return ResultSetPage com os dados da grid.
     */
    public ResultSetPage findPaginatedBySolicitacaoSinal(TypedFlatMap map, FindDefinition findDefinition){
    	SqlTemplate sql = getDetachedCriteriaBySolicitacaoSinal(map);
		sql.addOrderBy("ss.nrSolicitacaoSinal");
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), sql.getCriteria());	
    } 

    
    /**
     * Alterar o campo TP_STATUS_SOLICITACAO para "CA" (Cancelado) na tabela SOLICITACAO_SINAL para o Controle de Carga 
     * em questão.
     * 
     * @param idControleCarga
     */
    public void storeCancelarSolicitacaoSinalByIdControleCarga(Long idControleCarga) {
    	StringBuffer sql = new StringBuffer()
	    	.append("update ")
	    	.append(SolicitacaoSinal.class.getName()).append(" as ss ")
	    	.append(" set ss.tpStatusSolicitacao = 'CA' ")
	    	.append("where ss.controleCarga.id = ? ");

    	List param = new ArrayList();
    	param.add(idControleCarga);

    	executeHql(sql.toString(), param);
    }
}