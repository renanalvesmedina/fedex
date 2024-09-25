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
import com.mercurio.lms.contasreceber.model.MotivoOcorrenciaBanco;
import com.mercurio.lms.contasreceber.model.OcorrenciaBanco;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MotivoOcorrenciaBancoDAO extends BaseCrudDao<MotivoOcorrenciaBanco, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return MotivoOcorrenciaBanco.class;
    }
    
    /**
     * M�todo que busca as OcorrenciasBanco de acordo com os filtros passados
     * @param criteria
     * @return ResultSetPage
     */
    public ResultSetPage findPaginatedByMotivoOcorrenciaRemessaRetornoBancos(TypedFlatMap criteria){
    	FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
    	
    	Long idOcorrenciaBanco = criteria.getLong("ocorrenciaBanco.idOcorrenciaBanco");
    	Short nrMotivoOcorrenciaBanco = criteria.getShort("motivoOcorrenciaBanco.nrMotivoOcorrenciaBanco");
    	String dsMotivoOcorrenciaBanco = criteria.getString("motivoOcorrenciaBanco.dsMotivoOcorrenciaBanco");
    	
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection(
    			new StringBuffer()
    				.append("new Map( mob.idMotivoOcorrenciaBanco as idMotivoOcorrenciaBanco, ")
    				.append(" mob.nrMotivoOcorrenciaBanco as nrMotivoOcorrenciaBanco, ")
    				.append(" mob.dsMotivoOcorrenciaBanco as dsMotivoOcorrenciaBanco ) ")
    				.toString()
    		);
    	
    	sql.addFrom(getPersistentClass().getName(), " as mob JOIN mob.ocorrenciaBanco AS ob ");
    	
    	
    	sql.addCriteria("ob.idOcorrenciaBanco", "=", idOcorrenciaBanco);
		sql.addCriteria("mob.nrMotivoOcorrenciaBanco", "=", nrMotivoOcorrenciaBanco);
		if(dsMotivoOcorrenciaBanco != null && StringUtils.isNotBlank(dsMotivoOcorrenciaBanco)){
			sql.addCustomCriteria(" (lower(mob.dsMotivoOcorrenciaBanco) like lower( ? ) ) ");
			sql.addCriteriaValue(dsMotivoOcorrenciaBanco);
		}
    	
		sql.addOrderBy("mob.nrMotivoOcorrenciaBanco");
    	
    	ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(
				sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());

    	return rsp;
	}
    
    /**
     * M�todo que retorna o n�mero de registros de acordo com os filtros passados
     * @param criteria
     * @return
     */
    public Integer getRowCountByMotivoOcorrenciaRemessaRetornoBancos(TypedFlatMap criteria) {
    	
    	Long idOcorrenciaBanco = criteria.getLong("ocorrenciaBanco.idOcorrenciaBanco");
    	Short nrMotivoOcorrenciaBanco = criteria.getShort("motivoOcorrenciaBanco.nrMotivoOcorrenciaBanco");
    	String dsMotivoOcorrenciaBanco = criteria.getString("motivoOcorrenciaBanco.dsMotivoOcorrenciaBanco");
    	
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection("count(mob.idMotivoOcorrenciaBanco)");
    	
    	sql.addFrom(getPersistentClass().getName(), " as mob JOIN mob.ocorrenciaBanco AS ob ");
    	
    	
    	sql.addCriteria("ob.idOcorrenciaBanco", "=", idOcorrenciaBanco);
		sql.addCriteria("mob.nrMotivoOcorrenciaBanco", "=", nrMotivoOcorrenciaBanco);
		if(dsMotivoOcorrenciaBanco != null && StringUtils.isNotBlank(dsMotivoOcorrenciaBanco)){
			sql.addCustomCriteria(" (lower(mob.dsMotivoOcorrenciaBanco) like lower( ? ) ) ");
			sql.addCriteriaValue(dsMotivoOcorrenciaBanco);
		}

		sql.addOrderBy("mob.nrMotivoOcorrenciaBanco");

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria());
		return result.intValue();
    }

    /**
     * Carregra um motivoOcorrenciaBanco de acordo com o nrMotivoOcorrenciaBanco e o idOcorrencia.
     *
     * @author Hector Julian Esnaola Junior
     * @since 11/07/2007
     *
     * @param nrMotivo
     * @param idOcorrencia
     * @return
     *
     */
    public MotivoOcorrenciaBanco findMotivoByNrMotivoAndOcorrencia(Short nrMotivo, Long idOcorrencia){
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection("mob");
    	sql.addFrom(getPersistentClass().getName() + " mob ");
    	sql.addCriteria("mob.nrMotivoOcorrenciaBanco", "=", nrMotivo);
    	sql.addCriteria("mob.ocorrenciaBanco.id", "=", idOcorrencia);
    	
    	return (MotivoOcorrenciaBanco)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
    }
    
    /** M�todo invocado aum�ticamente pelo framework, seta os atributos que est�o com lazy = true para false */
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("ocorrenciaBanco", FetchMode.JOIN);
    }

	public List<MotivoOcorrenciaBanco> findMotivoOcorrenciaForRetornoBanco(Short nrOcorrenciaBanco, Long idCedente, Short motivoOcorrenciaBancos) {
		StringBuilder hql = new StringBuilder()
	   	.append(" select mob \n")
        .append("   from com.mercurio.lms.contasreceber.model.Cedente c \n")
        .append("   join c.agenciaBancaria ab \n")
        .append("   join ab.banco b \n")
        .append("   join b.ocorrenciaBancos ob \n")
        .append("   join ob.motivoOcorrenciaBancos mob \n")
        .append("  where ob.tpOcorrenciaBanco = 'RET' \n")
        .append("    and ob.nrOcorrenciaBanco = ? ")
        .append("    and c.idCedente = ? ")
        .append("    and mob.nrMotivoOcorrenciaBanco = ?");

	   	return getAdsmHibernateTemplate().find(hql.toString(), new Object[] {nrOcorrenciaBanco, idCedente, motivoOcorrenciaBancos});
	}

}