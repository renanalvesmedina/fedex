
package com.mercurio.lms.expedicao.model.dao;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.LiberacaoNotaNatura;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class LiberacaoNotaNaturaDAO extends BaseCrudDao<LiberacaoNotaNatura, Long> {

	@Override
	public final Class<LiberacaoNotaNatura> getPersistentClass() {
		return LiberacaoNotaNatura.class;
	}

	public LiberacaoNotaNatura findLiberacaoNotaNaturaForNotaFiscalConhecimento(Integer nrNotaFiscal, Long idCliente){
		if(nrNotaFiscal != null && idCliente != null){
			StringBuffer sql = new StringBuffer();
			sql.append("from LiberacaoNotaNatura as lnn ");
			sql.append("where lnn.nrNotaFiscal = ?");
			sql.append("and lnn.cliente.id = ?");
			return (LiberacaoNotaNatura) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{nrNotaFiscal, idCliente});
		} else {
			return null;
		}
	}
	
	/**
	 * Passa os registros em Liberacao_Nota_natura com ID_DOCTO_SERVICO = parametro para tp_situacao = 'E'
	 * 
	 * @param idDoctoServico
	 * @author JonasFE
	 */
	public void atualizaTerraNaturaEmitido(Long idDoctoServico) {
		//update tem melhor performance
		StringBuffer sql = new StringBuffer()
    	.append("update ")
    	.append("LiberacaoNotaNatura as lnn ")
    	.append("set lnn.tpSituacao = 'E' ")
    	.append("where lnn.doctoServico.id = ? ");

    	List param = new ArrayList();
    	param.add(idDoctoServico);

    	super.executeHql(sql.toString(), param);
	}
	
	/**
	 * Passa os registros em Liberacao_Nota_natura com ID_DOCTO_SERVICO = parametro para tp_situacao = 'C'
	 * 
	 * @param idDoctoServico
	 * @author JonasFE
	 */
	public void atualizaTerraNaturaCancelado(Long idDoctoServico) {
		//update tem melhor performance
		StringBuffer sql = new StringBuffer()
		.append("update ")
		.append("LiberacaoNotaNatura as lnn ")
		.append("set lnn.tpSituacao = 'C' ")
		.append("where lnn.doctoServico.id = ? ");
		
		List param = new ArrayList();
		param.add(idDoctoServico);
		
		super.executeHql(sql.toString(), param);
	}
	
	public ResultSetPage findNotasNatura(TypedFlatMap criteria){
		FindDefinition fd = FindDefinition.createFindDefinition(criteria);
		
		SqlTemplate hql = generateSqlTemplateNotasNatura(criteria);
		
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(), fd.getCurrentPage(), fd.getPageSize(), hql.getCriteria());
	}

	private SqlTemplate generateSqlTemplateNotasNatura(TypedFlatMap criteria) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(lnn.nrNotaFiscal as nrNotaFiscal" +
				", lnn.dtPedido as dtPedido" +
				", conh.tpDocumentoServico || ' ' || fil.sgFilial || ' ' || conh.nrDoctoServico as docOriginal" +
				", conh.dhEmissao as dhEmissao" +
				", filDestDs.sgFilial as sgFilialDestino" +
				", lnn.tpServico as tpServico" +
				", ds.tpDocumentoServico || ' ' || filOrigDs.sgFilial || ' ' || ds.nrDoctoServico as docDevRen" +
				", ds.dhEmissao as dhEmissaoDevRev" +
				", lnn.tpSituacao as tpSituacao" +
				")" );
		
		hql.addFrom("LiberacaoNotaNatura lnn "
			+ "left join lnn.doctoServico ds "
			+ "left join ds.filialByIdFilialOrigem filOrigDs "
			+ "left join lnn.filialDestino filDestDs "
			+ "left join lnn.notaFiscalConhecimento nfc "
			+ "left join nfc.conhecimento conh "
			+ "left join conh.filialByIdFilialOrigem fil ");
		hql.addCriteria("filDestDs.idFilial", "=", criteria.get("idFilialDestino"));
		hql.addCriteria("lnn.dtPedido", ">=", criteria.get("dhEmisssaoInicial"));
		hql.addCriteria("lnn.dtPedido", "<=", criteria.get("dhEmisssaoFinal"));
		hql.addCriteria("lnn.tpSituacao", "=", criteria.get("tpSituacao"));
		return hql;
	}
	
	public Integer getRowCountNotasNatura(TypedFlatMap criteria){
		SqlTemplate hql = generateSqlTemplateNotasNatura(criteria);
		
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false), hql.getCriteria());
	}
	
}