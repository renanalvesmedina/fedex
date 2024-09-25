package com.mercurio.lms.carregamento.model.dao;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.MotoristaControleCarga;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MotoristaControleCargaDAO extends BaseCrudDao<MotoristaControleCarga, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MotoristaControleCarga.class;
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
			sql.append("select new map(mcc.idMotoristaControleCarga as idMotoristaControleCarga, ")
				.append("pessoaMotorista.tpIdentificacao as tpIdentificacao, ")
				.append("pessoaMotorista.nrIdentificacao as nrIdentificacao, ")
				.append("pessoaMotorista.nmPessoa as nmPessoaMotorista, ")
				.append("mcc.dhTroca as dhTroca, ")
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
			.append(MotoristaControleCarga.class.getName()).append(" as mcc ")
			.append("inner join mcc.controleCarga as cc ")
			.append("inner join mcc.motorista as motorista ")
			.append("inner join motorista.pessoa as pessoaMotorista ")
			.append("left join mcc.usuarioByIdFuncAlteraStatus as usuarioAlteraStatus ")
			.append("left join mcc.localTroca as localTroca ")
			.append("left join localTroca.controleTrecho as controleTrecho ")
			.append("left join controleTrecho.filialByIdFilialOrigem as filialOrigem ")
			.append("left join controleTrecho.filialByIdFilialDestino as filialDestino ")
			.append("left join localTroca.rodovia as rodovia ")
			.append("left join localTroca.municipio as municipio ")
			.append("left join municipio.unidadeFederativa as uf ")
			.append("where ")
    		.append("cc.id = ? ");

		if (!isRowCount) {
			sql.append("order by mcc.dhTroca.value desc ");
		}
		param.add(idControleCarga);
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
	public MotoristaControleCarga findMotoristaCcByIdControleCarga(Long idControleCarga) {
		StringBuffer sql = new StringBuffer()
			.append("select mcc from ")
			.append(MotoristaControleCarga.class.getName()).append(" as mcc ")
			.append("inner join mcc.controleCarga as cc ")
			.append("inner join fetch mcc.motorista as motorista ")
			.append("left join fetch mcc.liberacaoReguladora as lr ")
			.append("where ")
    		.append("cc.id = ? ")
			.append("and mcc.dhTroca.value is null ");

		List param = new ArrayList();
		param.add(idControleCarga);
		List lista = getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	    return lista.isEmpty() ? null : (MotoristaControleCarga)lista.get(0);
	}
}