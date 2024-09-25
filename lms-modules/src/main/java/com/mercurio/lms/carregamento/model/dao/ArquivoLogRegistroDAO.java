package com.mercurio.lms.carregamento.model.dao;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ArquivoLogProcessamento;
import com.mercurio.lms.carregamento.model.ArquivoLogRegistro;
import com.mercurio.lms.contratacaoveiculos.model.ChecklistMeioTransporte;
import com.mercurio.lms.entrega.model.AgendamentoDoctoServico;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ArquivoLogRegistroDAO extends BaseCrudDao<ArquivoLogRegistro, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ArquivoLogRegistro.class;
    }    
    
    public ResultSetPage findPaginatedConsultarLogCargaArquivosInHouse(TypedFlatMap criteria) {
    	FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
    	
		return findPaginated(criteria, findDef);
    }
    
    public Integer getRowCountConsultarLogCargaArquivosInHouse(TypedFlatMap criteria) {
		SqlTemplate sql = montaQuerySqlConsultarLogCargaArquivosInHouse(criteria);	
		Integer rowCountQuery = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
		
		return rowCountQuery;
	}

	private SqlTemplate montaQuerySqlConsultarLogCargaArquivosInHouse(TypedFlatMap criteria) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(" +
				"alr.idArquivoLogRegistro as idArquivoLogRegistro, " +
				"alc.idArquivoLogCompatado as idArquivoLogCompactado, " +
				"ald.idArquivoLogDescompatado as idArquivoLogDescompactado, " +
				"alp.idArquivoLogProcessamento as idArquivoLogProcessamento, " +
				"alr.dsArquivoLog as dsArquivoLog, " +
				"alr.dhInicioLeitura as dhInicioLeitura, " +
				"alr.dhFimLeitura as dhFimLeitura, " +
				"alr.qtTotalArquivoCompactado as qtTotalArquivoCompactado, " +
				"alc.dsArquivoCompactado as dsArquivoCompactado, " +
				"alc.nrTamanhoTotalKb as nrTamanhoTotalKb, " +
				"alc.qtTotalArquivoDescompactado as qtTotalArquivoDescompactado, " +
				"ald.dsArquivoDescompactado as dsArquivoDescompactado, " +
				"ald.nrTamanhoTotalKb as nrTamanhoKb, " +
				"(case when (alp.dsErroArquivo != null and LENGTH(trim(alp.dsErroArquivo)) != 0) then 'vermelha' " +
				"when (alr.dsErroArquivo != null and LENGTH(trim(alr.dsErroArquivo)) != 0) then 'vermelha' " +
				"when (alc.dsErroArquivo != null and LENGTH(trim(alc.dsErroArquivo)) != 0) then 'vermelha' " +
				"when (ald.dsErroArquivo != null and LENGTH(trim(ald.dsErroArquivo)) != 0) then 'vermelha' " +
				"else 'verde' end) as erro) ");
		
		
		hql.addFrom(ArquivoLogRegistro.class.getName() + " alr " +
					"left outer join alr.arquivosLogCompactados alc " +
					"left outer join alc.arquivosLogDescompactados ald " +
					"left outer join ald.arquivosLogProcessamentos alp ");
		
		if (criteria.getLong("mapaCarregamento") != null) {
			hql.addCriteria("alp.nrMpc", "=", criteria.getLong("mapaCarregamento"));
		}
		
		hql.addCriteria("TRUNC(alr.dhInicioLeitura.value)", ">=", criteria.getYearMonthDay("dataDeProcessamentoInicial"));
		hql.addCriteria("TRUNC(alr.dhFimLeitura.value)", "<=", criteria.getYearMonthDay("dataDeProcessamentoFinal"));
		
		if (criteria.getBoolean("comErro")) {
			hql.addCriteria("alp.blLogSucesso", "=", "N");
		}
		
		hql.addOrderBy("alc.dsArquivoCompactado");
		hql.addOrderBy("ald.dsArquivoDescompactado");
		
		return hql;
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate hql = montaQuerySqlConsultarLogCargaArquivosInHouse(criteria);
		
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(),findDef.getCurrentPage(), findDef.getPageSize(), hql.getCriteria());
	}
	
}