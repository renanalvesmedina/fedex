package com.mercurio.lms.portaria.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.portaria.model.ArquivoGeracao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ArquivoGeracaoDAO  extends BaseCrudDao<ArquivoGeracao, Long> {

	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("filial", FetchMode.JOIN);
		lazyFindById.put("acaoIntegracao", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}
	
	@Override
	protected void initFindListLazyProperties(Map lazyFindList) {
		lazyFindList.put("filial", FetchMode.JOIN);
		lazyFindList.put("acaoIntegracao", FetchMode.JOIN);
		super.initFindListLazyProperties(lazyFindList);
	}
	
	@Override
	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("filial", FetchMode.JOIN);
		lazyFindLookup.put("acaoIntegracao", FetchMode.JOIN);
		super.initFindLookupLazyProperties(lazyFindLookup);
	}
	
	@Override
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("filial", FetchMode.JOIN);
		lazyFindPaginated.put("acaoIntegracao", FetchMode.JOIN);
		super.initFindPaginatedLazyProperties(lazyFindPaginated);
	}
	
	@Override
	protected Class getPersistentClass() {
		return ArquivoGeracao.class;
	}

	public ResultSetPage findPaginatedDownloadArquivos(Map criteria) {
		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
		
		SqlTemplate sql =  findDownloadArquivosSqlTemplate(criteria);
		
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
	}

	private SqlTemplate findDownloadArquivosSqlTemplate(Map criteria) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("new "
				+ getPersistentClass().getName()
				+ "(arquivoGeracao.idArquivoGeracao, arquivoGeracao.nmPI, arquivoGeracao.tpDocumento, arquivoGeracao.filial,  arquivoGeracao.nrDocumento, arquivoGeracao.arquivo, arquivoGeracao.dtGeracao, to_char(arquivoGeracao.dtGeracao, 'dd/mm/yyyy HH24:MI:SS'),  arquivoGeracao.acaoIntegracao)");
		sql.addFrom(getPersistentClass().getName()+ " as arquivoGeracao " +
				"join arquivoGeracao.filial as filial " +
				"join arquivoGeracao.acaoIntegracao as acaoIntegracao " +
				"join filial.pessoa as filialPessoa ");
		sql.addCriteria("filial.id", "=", criteria.get("idFilial"));
		sql.addCriteria("arquivoGeracao.nrDocumento", "like", criteria.get("nrDocumento"));
		sql.addCriteria("arquivoGeracao.tpDocumento", "=", criteria.get("tpDocumento"));
		sql.addCriteria("trunc(arquivoGeracao.dtGeracao)", ">=", criteria.get("dtGeracaoInicial"));
		sql.addCriteria("trunc(arquivoGeracao.dtGeracao)", "<=", criteria.get("dtGeracaoFinal"));
	
		sql.addOrderBy("arquivoGeracao.filial.sgFilial");
		sql.addOrderBy("arquivoGeracao.tpDocumento");
		sql.addOrderBy("arquivoGeracao.nrDocumento");
		sql.addOrderBy("arquivoGeracao.acaoIntegracao.dsAcaoIntegracao");
		sql.addOrderBy("arquivoGeracao.dtGeracao");
		
		return sql;
		
	}

	public Integer getRowCountDownloadArquivos(Map criteria) {
		SqlTemplate sql =  findDownloadArquivosSqlTemplate(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
	}

}
