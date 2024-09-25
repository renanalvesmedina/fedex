package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.MeioTransporteRotaViagem;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MeioTransporteRotaViagemDAO extends BaseCrudDao<MeioTransporteRotaViagem, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MeioTransporteRotaViagem.class;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("meioTransporteRodoviario",FetchMode.JOIN);
    	lazyFindById.put("meioTransporteRodoviario.meioTransporte",FetchMode.JOIN);
    }
    
    public boolean validateDuplicated(Long id, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal,
    		Long idMeioTransporte, Long idRotaViagem) {
    	DetachedCriteria dc = JTVigenciaUtils.getDetachedVigencia(getPersistentClass(),id,dtVigenciaInicial,dtVigenciaFinal);

    	dc.createAlias("meioTransporteRodoviario","t");
    	dc.createAlias("rotaViagem","r");
    	dc.add(Restrictions.eq("t.idMeioTransporte",idMeioTransporte));
    	dc.add(Restrictions.eq("r.idRotaViagem",idRotaViagem));
    	
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc).size() > 0;
    }
    
    public ResultSetPage findPaginatedDefault(TypedFlatMap criteria, FindDefinition findDef) {
    	SqlTemplate sql = createHqlPaginated(criteria,false);
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
    }
    
    public Integer findRowCountDefault(TypedFlatMap criteria) {
    	SqlTemplate sql = createHqlPaginated(criteria,true);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
    }

    private SqlTemplate createHqlPaginated(TypedFlatMap criteria, boolean isRowCount) {
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection("new Map(" +
    				"MT_RV.idMeioTransporteRotaViagem as idMeioTransporteRotaViagem, " +
	    			"MT_RV.dtVigenciaInicial as dtVigenciaInicial, " +
	    			"MT_RV.dtVigenciaFinal as dtVigenciaFinal, " +
	    			"M.nrFrota as nrFrota, " +
	    			"M.nrIdentificador as nrIdentificador, " +
	    			"TIPO.dsTipoMeioTransporte as dsTipoMeioTransporte, " +
	    			"MARCA.dsMarcaMeioTransporte as dsMarcaMeioTransporte, " +
	    			"MODEL.dsModeloMeioTransporte as dsModeloMeioTransporte, " +
	    			"M.nrAnoFabricao as nrAnoFabricao) ");
    	
    	sql.addFrom(MeioTransporteRotaViagem.class.getName() + " as MT_RV " +
    			"inner join MT_RV.rotaViagem as RV " +
    			"inner join MT_RV.meioTransporteRodoviario as MT " +
    			" left join MT.meioTransporte as M  " +
    			" left join M.modeloMeioTransporte as MODEL " +
    			" left join MODEL.tipoMeioTransporte as TIPO " +
    			" left join MODEL.marcaMeioTransporte as MARCA ");
    	    	
    	sql.addCriteria("RV.idRotaViagem","=",criteria.getLong("rotaViagem.idRotaViagem"));
    	sql.addCriteria("M.idMeioTransporte","=",criteria.getLong("meioTransporteRodoviario.idMeioTransporte"));
   		sql.addCriteria("MT_RV.dtVigenciaInicial",">=",criteria.getYearMonthDay("dtVigenciaInicial"));
    	sql.addCriteria("MT_RV.dtVigenciaFinal","<=",criteria.getYearMonthDay("dtVigenciaFinal"));

    	sql.addOrderBy("M.nrFrota");
    	sql.addOrderBy("MT_RV.dtVigenciaInicial");

		return sql;
	}

    public List findToConsultarRotas(Long idRotaViagem) {
    	StringBuffer hql = new StringBuffer()
    		.append(" select new Map( ")
    		.append("  MTRANSP.nrIdentificador as meioTransporte_nrIdentificador, ")
    		.append("  MTRANSP.nrFrota as meioTransporte_nrFrota, ")
    		.append("  MTRANSP.idMeioTransporte as meioTransporte_idMeioTransporte, ")
    		.append("  TIPO.dsTipoMeioTransporte as dsTipoMeioTransporte, ")
    		.append("  MARCA.dsMarcaMeioTransporte as dsMarcaMeioTransporte, ")
    		.append("  MODEL.dsModeloMeioTransporte as dsModeloMeioTransporte, ")
    		.append("  MTRANSP.nrAnoFabricao as meioTransporte_nrAnoFabricao, ")
    		.append("  MT.dtVigenciaInicial as dtVigenciaInicial, ")
    		.append("  MT.dtVigenciaFinal as dtVigenciaFinal ")
    		.append(" ) ")
    		.append(" from " + MeioTransporteRotaViagem.class.getName() + " MT ")
    		.append(" left join MT.rotaViagem as RV ")
    		.append(" left join MT.meioTransporteRodoviario as RODO ")
    		.append(" left join RODO.meioTransporte as MTRANSP ")
    		.append(" left join MTRANSP.modeloMeioTransporte as MODEL ")
    		.append(" left join MODEL.tipoMeioTransporte as TIPO ")
    		.append(" left join MODEL.marcaMeioTransporte as MARCA ")
    		.append(" where RV.idRotaViagem = ? ")
    		.append(" order by MTRANSP.nrFrota ");
    		
    	
    	return getAdsmHibernateTemplate().find(hql.toString(),idRotaViagem);
    }
    
    
    /**
     * Verifica se existe um veículo cadastrado para a rota de viagem.
     * 
     * @param idRotaViagem
     * @param idMeioTransporte
     * @return True se encontrar, caso contrário, False.
     */
    public Boolean findMeioTransporteWithRotaViagem(Long idRotaViagem, Long idMeioTransporte) {

    	StringBuffer hql = new StringBuffer()
    		.append(" from ").append(MeioTransporteRotaViagem.class.getName()).append(" as mtrv ")
    		.append("where ")
    		.append("mtrv.rotaViagem.id = ? ")
    		.append("and mtrv.meioTransporteRodoviario.id = ? ")
    		.append("and ? between mtrv.dtVigenciaInicial and mtrv.dtVigenciaFinal ");
    	
    	List param = new ArrayList();
    	param.add(idRotaViagem);
    	param.add(idMeioTransporte);
    	param.add(JTDateTimeUtils.getDataAtual());
    	
    	List lista = getAdsmHibernateTemplate().find(hql.toString(), param.toArray());
    	if (lista.isEmpty())
    		return Boolean.FALSE;

    	return Boolean.TRUE;
    }
}