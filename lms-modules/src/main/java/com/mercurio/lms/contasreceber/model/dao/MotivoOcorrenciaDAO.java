package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;

import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.MotivoOcorrencia;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MotivoOcorrenciaDAO extends BaseCrudDao<MotivoOcorrencia, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MotivoOcorrencia.class;
    }

    /**
     * Método de listagem da grid
     * @param dsMotivoOcorrencia Descrição do motivo ocorrência
     * @param tpMotivoOcorrencia Tipo do Motivo Ocorrência
     * @param tpSituacao Situação do Motivo Ocorrência
     * @param findDef Dados de paginação
     * @return ResultSetPage contendo os resultados da pesquisa e dados de paginação
     */
	public ResultSetPage findPaginatedMotivoOcorrencia(VarcharI18n dsMotivoOcorrencia, String tpMotivoOcorrencia, String tpSituacao, FindDefinition findDef) {
		
		SqlTemplate sql = getQueryHqlMotivoOcorrencia(dsMotivoOcorrencia, tpMotivoOcorrencia, tpSituacao);
		
		sql.addProjection("mo");
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("dv.description",LocaleContextHolder.getLocale()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("mo.dsMotivoOcorrencia", LocaleContextHolder.getLocale()));       
		
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
		
	}
	
	/**
	 * Conta quantos registros serão exibidos na listagem
	 * @param dsMotivoOcorrencia Descrição do motivo ocorrência
     * @param tpMotivoOcorrencia Tipo do Motivo Ocorrência
     * @param tpSituacao Situação do Motivo Ocorrência
	 * @return Inteiro informando quantos registros serão exibidos na listagem
	 */
	public Integer getRowCountMotivoOcorrencia(VarcharI18n dsMotivoOcorrencia, String tpMotivoOcorrencia, String tpSituacao) {
		
		SqlTemplate sql = getQueryHqlMotivoOcorrencia(dsMotivoOcorrencia, tpMotivoOcorrencia, tpSituacao);		
		
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria());
		
	}

	/**
	 * Query padrão para o Motivo Ocorrência
	 * @param dsMotivoOcorrencia Descrição do motivo ocorrência
	 * @param tpMotivoOcorrencia Tipo do motivo ocorrência
	 * @param tpSituacao Situação do motivo
	 * @return SqlTemplate - query montada
	 */
	public SqlTemplate getQueryHqlMotivoOcorrencia(VarcharI18n dsMotivoOcorrencia, String tpMotivoOcorrencia, String tpSituacao) {
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom(MotivoOcorrencia.class.getName(),"mo"); 
		sql.addFrom(DomainValue.class.getName(),"dv " +
				    "	inner join dv.domain as d ");
		sql.addFrom(DomainValue.class.getName(),"dv2 " +
	    			"	inner join dv2.domain as d2 ");
		
		sql.addCriteria("d.name","=","DM_TIPO_MOTIVO_OCORRENCIA");
		sql.addCriteria("d2.name","=","DM_STATUS");
		
		sql.addCustomCriteria("dv.value = mo.tpMotivoOcorrencia");
		sql.addCustomCriteria("dv2.value = mo.tpSituacao");
		
		sql.addCriteria("mo.tpMotivoOcorrencia","=",tpMotivoOcorrencia);
		sql.addCriteria("mo.tpSituacao","=",tpSituacao);
				
		if( dsMotivoOcorrencia != null && !dsMotivoOcorrencia.toString().equals("") ){
			sql.addCriteria(PropertyVarcharI18nProjection.createProjection("mo.dsMotivoOcorrencia"), "like", dsMotivoOcorrencia.getValue());
		}
		
		return sql;
		
	}
	
    
    /**
     * Retorna a liste de motivo de ocorrencia por tipo de motivo de ocorrencia informado
     * 
     * @author Mickaël Jalbert
     * @since 25/04/2006
     * 
     * @param String tpMotivoOcorrencia
     * @return List
     * */
    public List findByTpMotivoOcorrencia(String tpMotivoOcorrencia){
    	SqlTemplate hql = mountHql(tpMotivoOcorrencia);
    	
    	hql.addProjection("motoco");
    	
    	return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());    	
    }
    
    private SqlTemplate mountHql(String tpMotivoOcorrencia){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addInnerJoin(MotivoOcorrencia.class.getName(), "motoco");
    	
    	hql.addCriteria("motoco.tpMotivoOcorrencia", "like", tpMotivoOcorrencia);
    	
    	return hql; 
    }	

}