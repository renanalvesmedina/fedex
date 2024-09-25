package com.mercurio.lms.carregamento.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ArquivoLogDescompactado;
import com.mercurio.lms.carregamento.model.ArquivoLogRegistro;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ArquivoLogDescompactadoDAO extends BaseCrudDao<ArquivoLogDescompactado, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ArquivoLogDescompactado.class;
    }
    
    public ResultSetPage  findPaginatedConsultarLogCargaArquivosInHouse(TypedFlatMap criteria) {
    	FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
    	
		return findPaginated(criteria, findDef);
    }
    
    public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate hql = montaQuerySqlConsultarLogCargaArquivosInHouse(criteria);
		
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(),findDef.getCurrentPage(), findDef.getPageSize(), hql.getCriteria());
	}
    
    public Integer getRowCountConsultarLogCargaArquivosInHouse(TypedFlatMap criteria) {
		SqlTemplate sql = montaQuerySqlConsultarLogCargaArquivosInHouse(criteria);	
		Integer rowCountQuery = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
		
		return rowCountQuery;
	}
    
    private SqlTemplate montaQuerySqlConsultarLogCargaArquivosInHouse(TypedFlatMap criteria) {
    	SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(" + 
				"alc.dsArquivoCompactado as dsArquivoCompactado, " +
				"ald.dsArquivoDescompactado as dsArquivoDescompactado, " +
				"alp.dhInicioProcessamento as dhInicioProcessamento, " +
				"alp.dhFimProcessamento as dhFimProcessamento, " +
				"alp.nrMpc as mapaCarregamento, " +
				
				"(case when (alc.dsErroArquivo != null and LENGTH(trim(alc.dsErroArquivo)) != 0 " +
				"and ald.dsErroArquivo != null and LENGTH(trim(ald.dsErroArquivo)) != 0) then 'Erro Interno' " +
				"when (alp.blLogSucesso = 'N' and alp.blLogCarrier = 'S') then 'Carrier' " +
				"when (alp.blLogSucesso = 'N' and alp.blLogData = 'S') then 'Data' " +
				"when (alp.blLogSucesso = 'N' and alp.blLogWave = 'S') then 'MpC' " +
				"when (alp.blLogSucesso = 'N' and alp.blLogDoca = 'S') then 'Doca' " +
				"when (alp.blLogSucesso = 'N' and alp.blLogDestino = 'S') then 'Destino' " +
				"when (alp.blLogSucesso = 'N' and alp.blLogVolume = 'S') then 'Volume' " +
				"when (alp.blLogSucesso = 'N' and alp.blLogRota = 'S') then 'Rota' " +
				"when (alp.blLogSucesso = 'N' and alp.blLogItem = 'S') then 'Item' " +
				"when (alp.blLogSucesso = 'N' and alp.blLogCubagem = 'S') then 'Cubagem' " +
				"when (alp.blLogSucesso = 'N' and alp.blLogPeso = 'S') then 'Peso' " +
				"when (alp.blLogSucesso = 'N' and alp.blLogTotalVolume = 'S') then 'Total Volume' " +
				"when (alp.blLogSucesso = 'N' and alp.blLogTotalPeso = 'S') then 'Total Peso' " +
				"when (alp.blLogSucesso = 'N' and alp.blLogTotalCubagem = 'S') then 'Total Cubagem' " +
				"else 'OK' end) as falha, " +
				
				"(case when (alc.dsErroArquivo != null and LENGTH(trim(alc.dsErroArquivo)) != 0) then alc.dsErroArquivo " +
				"when (ald.dsErroArquivo != null and LENGTH(trim(ald.dsErroArquivo)) != 0) then ald.dsErroArquivo " +
				"when (alp.blLogSucesso = 'N') then alp.dsErroArquivo " +
				"else '' end) as descricaoErro) ");
		
		hql.addFrom(ArquivoLogRegistro.class.getName() + " alr " +
					"left outer join alr.arquivosLogCompactados alc " +
					"left outer join alc.arquivosLogDescompactados ald " +
					"left outer join ald.arquivosLogProcessamentos alp ");
		
		hql.addCriteria("alp.arquivoLogDescompactado.idArquivoLogDescompatado", "=", criteria.getLong("idArquivoLogDescompactado"));
		
		hql.addOrderBy("alc.dsArquivoCompactado");
		hql.addOrderBy("ald.dsArquivoDescompactado");
    	
    	return hql;
    	
    }
}