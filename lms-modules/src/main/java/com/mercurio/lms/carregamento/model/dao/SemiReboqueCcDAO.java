package com.mercurio.lms.carregamento.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.SemiReboqueCc;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class SemiReboqueCcDAO extends BaseCrudDao<SemiReboqueCc, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return SemiReboqueCc.class;
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
			sql.append("select new map(srcc.idSemiReboqueCc as idSemiReboqueCc, ")
				.append("meioTransporte.nrFrota as nrFrota, ")
				.append("meioTransporte.nrIdentificador as nrIdentificador, ")
				.append("pessoaProprietario.nmPessoa as nmPessoaProprietario, ")
				.append("srcc.dhTroca as dhTroca, ")
		    	.append("filialOrigem.sgFilial as sgFilialOrigem, ")
				.append("filialDestino.sgFilial as sgFilialDestino, ")
		    	.append("rodovia.sgRodovia as sgRodovia, ")
		    	.append("localTroca.idLocalTroca as idLocalTroca, ")
				.append("localTroca.nrKmRodoviaTroca as nrKmRodoviaTroca, ")
				.append("municipio.nmMunicipio as nmMunicipio, ")
		    	.append("uf.sgUnidadeFederativa as sgUnidadeFederativa) ");
		}
		sql.append("from ")
			.append(SemiReboqueCc.class.getName()).append(" as srcc ")
			.append("inner join srcc.controleCarga as cc ")
			.append("inner join srcc.meioTransporte as meioTransporte ")
			.append("left join srcc.localTroca as localTroca ")
			.append("left join localTroca.controleTrecho as controleTrecho ")
			.append("left join controleTrecho.filialByIdFilialOrigem as filialOrigem ")
			.append("left join controleTrecho.filialByIdFilialDestino as filialDestino ")
			.append("left join localTroca.rodovia as rodovia ")
			.append("left join localTroca.municipio as municipio ")
			.append("left join municipio.unidadeFederativa as uf ")
			.append("inner join meioTransporte.meioTranspProprietarios as meioTranspProprietario ")
			.append("inner join meioTranspProprietario.proprietario as proprietario ")
			.append("inner join proprietario.pessoa as pessoaProprietario ")

			.append("where ")
    		.append("cc.id = ? ")
			.append("and ? between meioTranspProprietario.dtVigenciaInicial and meioTranspProprietario.dtVigenciaFinal ");

		if (!isRowCount) {
			sql.append("order by srcc.dhTroca.value desc ");
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
	public Map findSemiReboqueCcByIdControleCarga(Long idControleCarga) {
		StringBuffer sql = new StringBuffer()
			.append("select new map(srcc.idSemiReboqueCc as idSemiReboqueCc, ")
			.append("srcc.dhTroca as dhTroca, ")
			.append("cc.idControleCarga as controleCarga_idControleCarga, ")
			.append("meioTransporte.idMeioTransporte as meioTransporte_idMeioTransporte) ")
			.append("from ")
			.append(SemiReboqueCc.class.getName()).append(" as srcc ")
			.append("inner join srcc.controleCarga as cc ")
			.append("inner join srcc.meioTransporte as meioTransporte ")
			.append("where ")
    		.append("cc.id = ? ")
			.append("and srcc.dhTroca.value is null ");

		List param = new ArrayList();
		param.add(idControleCarga);
		List lista = getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	    return lista.isEmpty() ? null : (Map)lista.get(0);
	}
}