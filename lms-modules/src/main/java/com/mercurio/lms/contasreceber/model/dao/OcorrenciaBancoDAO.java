package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.OcorrenciaBanco;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class OcorrenciaBancoDAO extends BaseCrudDao<OcorrenciaBanco, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return OcorrenciaBanco.class;
    }
    
    /**
     * Método que busca as OcorrenciasBanco de acordo com os filtros passados
     * @param criteria
     * @return ResultSetPage
     */
    public ResultSetPage findPaginatedByOcorrenciaRemessaRetornoBancos(TypedFlatMap criteria){
    	FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
    	
    	Long idBanco = criteria.getLong("banco.idBanco");
    	String tpOcorrenciaBanco = criteria.getString("ocorrenciaBancos.tpOcorrenciaBanco");
    	Short nrOcorrenciaBanco = criteria.getShort("ocorrenciaBancos.nrOcorrenciaBanco");
    	String dsOcorrenciaBanco = criteria.getString("ocorrenciaBancos.dsOcorrenciaBanco");
    	
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection(
    			new StringBuffer()
    				.append("new Map( ob.idOcorrenciaBanco as idOcorrenciaBanco, ")
    				.append(" ob.tpOcorrenciaBanco as tpOcorrenciaBanco, ")
    				.append(" b.nmBanco as nmBanco, ")
    				.append(" ob.nrOcorrenciaBanco as nrOcorrenciaBanco, ")
    				.append(" ob.dsOcorrenciaBanco as dsOcorrenciaBanco ) ")
    				.toString()
    		);
    	
    	sql.addFrom(getPersistentClass().getName(), " as ob JOIN ob.banco AS b ");
    	
    	
    	sql.addCriteria("b.idBanco", "=", idBanco);
		sql.addCriteria("ob.tpOcorrenciaBanco", "=", tpOcorrenciaBanco);
		sql.addCriteria("ob.nrOcorrenciaBanco", "=", nrOcorrenciaBanco);
		if(dsOcorrenciaBanco != null && StringUtils.isNotBlank(dsOcorrenciaBanco)){
			sql.addCustomCriteria(" (lower(ob.dsOcorrenciaBanco) like lower( ? ) ) ");
			sql.addCriteriaValue(dsOcorrenciaBanco);
		}
		
		sql.addOrderBy("b.nmBanco, ob.tpOcorrenciaBanco, ob.nrOcorrenciaBanco");
    	
    	ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(
				sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());

    	return rsp;
	}
    
    /**
     * Método que retorna o número de registros de acordo com os filtros passados
     * @param criteria
     * @return
     */
    public Integer getRowCountByOcorrenciaRemessaRetornoBancos(TypedFlatMap criteria) {
    	Long idBanco = criteria.getLong("banco.idBanco");
    	String tpOcorrenciaBanco = criteria.getString("ocorrenciaBancos.tpOcorrenciaBanco");
    	Short nrOcorrenciaBanco = criteria.getShort("ocorrenciaBancos.nrOcorrenciaBanco");
    	String dsOcorrenciaBanco = criteria.getString("ocorrenciaBancos.dsOcorrenciaBanco");

    	SqlTemplate sql = new SqlTemplate();

    	sql.addProjection("count(ob.idOcorrenciaBanco)");

    	sql.addFrom(getPersistentClass().getName(), "ob JOIN ob.banco AS b ");

    	sql.addCriteria("b.idBanco", "=", idBanco);
		sql.addCriteria("ob.tpOcorrenciaBanco", "=", tpOcorrenciaBanco);
		sql.addCriteria("ob.nrOcorrenciaBanco", "=", nrOcorrenciaBanco);
		if(dsOcorrenciaBanco != null && StringUtils.isNotBlank(dsOcorrenciaBanco)){
			sql.addCustomCriteria(" (lower(ob.dsOcorrenciaBanco) like lower( ? ) ) ");
			sql.addCriteriaValue(dsOcorrenciaBanco);
		}

		sql.addOrderBy("ob.tpOcorrenciaBanco, b.nmBanco, ob.nrOcorrenciaBanco");

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria());
		return result.intValue();
    }

    /** Método invocado aumáticamente pelo framework, seta os atributos que estão com lazy = true para false */
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("banco", FetchMode.JOIN);
    }
    
    public List findByBancoNrOcorrenciaTpOcorrencia(Long idBanco, Short nrOcorrencoaBanco, String tpOcorrenciaBanco) {
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("oco");
    	sql.addFrom(OcorrenciaBanco.class.getName(), "oco");
    	sql.addCriteria("oco.banco.id", "=", idBanco);
    	sql.addCriteria("oco.nrOcorrenciaBanco", "=", nrOcorrencoaBanco);
    	sql.addCriteria("oco.tpOcorrenciaBanco", "=", tpOcorrenciaBanco);
    	return this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }
    
    /**
     * Busca as ocorrencias de banco do tipo tpOcorrencia relacionadas ao
     * cedente identificado por idCedente.
     * 
     * @param idCedente
     * @param tpOcorrencia
     * @return
     */
    public List findComboByIdCedente(Long idCedente, String tpOcorrencia) {
    	StringBuilder hql = new StringBuilder()
    	.append(" select new map(ob.idOcorrenciaBanco as idOcorrenciaBanco, \n")
    	.append("                ob.dsOcorrenciaBanco as dsOcorrenciaBanco) \n")
        .append("   from com.mercurio.lms.contasreceber.model.Cedente c \n")
        .append("   join c.agenciaBancaria ab \n")
        .append("   join ab.banco b \n")
        .append("   join b.ocorrenciaBancos ob \n")
        .append("  where ob.tpOcorrenciaBanco = ? \n")
        .append("    and c.idCedente = ? ")
        .append("	order by ob.dsOcorrenciaBanco");
    	
    	return getAdsmHibernateTemplate().find(hql.toString(), new Object[] {tpOcorrencia, idCedente});
    }

	public List<OcorrenciaBanco> findOcorrenciaBancoForRetornoBanco(Short nrOcorrenciaBanco, Long idCedente) {
    	StringBuilder hql = new StringBuilder()
    	.append(" select ob \n")
        .append("   from com.mercurio.lms.contasreceber.model.Cedente c \n")
        .append("   join c.agenciaBancaria ab \n")
        .append("   join ab.banco b \n")
        .append("   join b.ocorrenciaBancos ob \n")
        .append("  where ob.tpOcorrenciaBanco = 'RET' \n")
        .append("    and ob.nrOcorrenciaBanco = ? ")
        .append("    and c.idCedente = ? ")
        .append("	order by ob.dsOcorrenciaBanco");
		
    	return getAdsmHibernateTemplate().find(hql.toString(), new Object[] {nrOcorrenciaBanco, idCedente});
	}
    
}