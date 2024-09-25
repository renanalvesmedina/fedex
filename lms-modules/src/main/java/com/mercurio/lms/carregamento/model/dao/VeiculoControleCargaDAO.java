package com.mercurio.lms.carregamento.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.VeiculoControleCarga;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class VeiculoControleCargaDAO extends BaseCrudDao<VeiculoControleCarga, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return VeiculoControleCarga.class;
    }
    
    
    
    /**
     * 
     * @param idControleCarga
     * @param param
     * @param isRowCount
     * @return
     */
	private StringBuffer addSqlByFindPaginatedByIdControleCarga(Long idControleCarga, List param, boolean isRowCount) {
		StringBuffer sql = new StringBuffer();
		if (!isRowCount) {
			sql.append("select new map(vcc.idVeiculoControleCarga as idVeiculoControleCarga, ")
				.append("meioTransporte.nrFrota as nrFrota, ")
				.append("meioTransporte.nrIdentificador as nrIdentificador, ")
				.append("pessoaProprietario.nmPessoa as nmPessoaProprietario, ")
				.append("solicitacaoContratacao.nrSolicitacaoContratacao as nrSolicitacaoContratacao, ")
				.append("filialSolicitacaoContratacao.sgFilial as sgFilialSolicitacaoContratacao, ")
				.append("vcc.dhTroca as dhTroca, ")
		    	.append("filialOrigem.sgFilial as sgFilialOrigem, ")
				.append("filialDestino.sgFilial as sgFilialDestino, ")
		    	.append("rodovia.sgRodovia as sgRodovia, ")
		    	.append("usuarioAlteraStatus.nmUsuario as nmUsuarioAlteraStatus, ")
		    	.append("localTroca.idLocalTroca as idLocalTroca, ")
				.append("localTroca.nrKmRodoviaTroca as nrKmRodoviaTroca, ")
				.append("municipio.nmMunicipio as nmMunicipio, ")
		    	.append("uf.sgUnidadeFederativa as sgUnidadeFederativa) ");
		}
		sql.append("from ")
			.append(VeiculoControleCarga.class.getName()).append(" as vcc ")
			.append("inner join vcc.controleCarga as cc ")
			.append("inner join vcc.meioTransporte as meioTransporte ")
			.append("left join vcc.usuarioByIdFuncAlteraStatus as usuarioAlteraStatus ")
			.append("left join vcc.localTroca as localTroca ")
			.append("left join localTroca.controleTrecho as controleTrecho ")
			.append("left join controleTrecho.filialByIdFilialOrigem as filialOrigem ")
			.append("left join controleTrecho.filialByIdFilialDestino as filialDestino ")
			.append("left join localTroca.rodovia as rodovia ")
			.append("left join localTroca.municipio as municipio ")
			.append("left join municipio.unidadeFederativa as uf ")
			.append("inner join meioTransporte.meioTranspProprietarios as meioTranspProprietario ")
			.append("inner join meioTranspProprietario.proprietario as proprietario ")
			.append("inner join proprietario.pessoa as pessoaProprietario ")
			.append("left join vcc.solicitacaoContratacao as solicitacaoContratacao ")
			.append("left join solicitacaoContratacao.filial as filialSolicitacaoContratacao ")
    		.append("where ")
    		.append("cc.id = ? ")
			.append("and ? between meioTranspProprietario.dtVigenciaInicial and meioTranspProprietario.dtVigenciaFinal ");

		if (!isRowCount) {
			sql.append("order by vcc.dhTroca.value desc ");
		}

		param.add(idControleCarga);
		param.add(JTDateTimeUtils.getDataAtual());
		return sql;
	}

	/**
	 * 
	 * @param idControleCarga
	 * @param findDefinition
	 * @return
	 */
    public ResultSetPage findPaginatedByIdControleCarga(Long idControleCarga, FindDefinition findDefinition) {
    	List param = new ArrayList();
    	StringBuffer sql = addSqlByFindPaginatedByIdControleCarga(idControleCarga, param, false);
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.toString(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), param.toArray());
	    return rsp;
    }

    /**
     * 
     * @param idControleCarga
     * @return
     */
    public Integer getRowCountFindPaginatedByIdControleCarga(Long idControleCarga) {
    	List param = new ArrayList();
    	StringBuffer sql = addSqlByFindPaginatedByIdControleCarga(idControleCarga, param, true);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), param.toArray());
    }
    

    /**
     * 
     * @param idControleCarga
     * @return
     */
	public Map findVeiculoAtualByIdControleCarga(Long idControleCarga) {
		StringBuffer sql = new StringBuffer()
			.append("select new map(vcc.idVeiculoControleCarga as idVeiculoControleCarga, ")
			.append("meioTransporte.idMeioTransporte as idMeioTransporte, ")
			.append("meioTransporte.tpVinculo as tpVinculo, ")
			.append("proprietario.idProprietario as idProprietario) ")
			.append("from ")
			.append(VeiculoControleCarga.class.getName()).append(" as vcc ")
			.append("inner join vcc.meioTransporte as meioTransporte ")
			.append("inner join meioTransporte.meioTranspProprietarios as meioTranspProprietario ")
			.append("inner join meioTranspProprietario.proprietario as proprietario ")
    		.append("where ")
    		.append("vcc.controleCarga.id = ? ")
    		.append("and vcc.dhTroca.value is null ")
			.append("and ? between meioTranspProprietario.dtVigenciaInicial and meioTranspProprietario.dtVigenciaFinal ");
		
		List param = new ArrayList();
		param.add(idControleCarga);
		param.add(JTDateTimeUtils.getDataAtual());

		List list = getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	    return list.isEmpty() ? null : (Map)list.get(0);
    }
}