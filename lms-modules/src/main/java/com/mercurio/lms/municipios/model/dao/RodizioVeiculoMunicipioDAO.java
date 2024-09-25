package com.mercurio.lms.municipios.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.RodizioVeiculoMunicipio;
import com.mercurio.lms.util.JTVigenciaUtils;




/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RodizioVeiculoMunicipioDAO extends BaseCrudDao<RodizioVeiculoMunicipio, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return RodizioVeiculoMunicipio.class;
    }
    
    /* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#initFindPaginatedLazyProperties(java.util.Map)
	 */
	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("municipio",FetchMode.JOIN);
		fetchModes.put("municipio.unidadeFederativa",FetchMode.JOIN);
		fetchModes.put("municipio.unidadeFederativa.pais",FetchMode.JOIN);
		fetchModes.put("diaSemana",FetchMode.JOIN);
		fetchModes.put("municipio.unidadeFederativa.pais",FetchMode.JOIN);
	}

	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#initFindByIdLazyProperties(java.util.Map)
	 */
	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("municipio",FetchMode.JOIN);
		fetchModes.put("municipio.unidadeFederativa",FetchMode.JOIN);
		fetchModes.put("municipio.unidadeFederativa.pais",FetchMode.JOIN);
		fetchModes.put("diaSemana",FetchMode.JOIN);
		fetchModes.put("municipio.unidadeFederativa.pais",FetchMode.JOIN);
	}
	
	
	//LMS-00003
	public boolean verificaRodiziosVigentes(RodizioVeiculoMunicipio rodizioVeiculoMunicipio){
		DetachedCriteria dc = createDetachedCriteria();
		if(rodizioVeiculoMunicipio.getIdRodizioVeiculoMunicipio()!= null){
			dc.add(Restrictions.ne("idRodizioVeiculoMunicipio", rodizioVeiculoMunicipio.getIdRodizioVeiculoMunicipio()));
		}
		//placa
		dc.add(Restrictions.eq("nrFinalPlaca",rodizioVeiculoMunicipio.getNrFinalPlaca()));
		//municipio
		DetachedCriteria dcMunicipio = dc.createCriteria("municipio");
		dcMunicipio.add(Restrictions.eq("idMunicipio", rodizioVeiculoMunicipio.getMunicipio().getIdMunicipio()));
		//dia da semana
		dc.add(Restrictions.eq("diaSemana",rodizioVeiculoMunicipio.getDiaSemana().getValue()));
		
		//verificar se as datas estão vigentes
		JTVigenciaUtils.getDetachedVigencia(dc, rodizioVeiculoMunicipio.getDtVigenciaInicial(), rodizioVeiculoMunicipio.getDtVigenciaFinal());
		
		
		if (findByDetachedCriteria(dc).size()>0){
			if(rodizioVeiculoMunicipio.getHrRodizioInicial()== null && rodizioVeiculoMunicipio.getHrRodizioFinal()==null){
				dc.add(Restrictions.isNull("hrRodizioInicial"));
			    dc.add(Restrictions.isNull("hrRodizioFinal"));	
			}else{			
			//verifica se as horas estão dentro de algum intervalo
			dc.add(
					Restrictions.or( 
		    			Restrictions.and(
		    						Restrictions.le("hrRodizioInicial",rodizioVeiculoMunicipio.getHrRodizioInicial()),
		    					Restrictions.or(
		    						Restrictions.isNull("hrRodizioFinal"),
		    						Restrictions.ge("hrRodizioFinal",rodizioVeiculoMunicipio.getHrRodizioInicial())
		    					)
		    			),
						Restrictions.and(
		    						Restrictions.ge("hrRodizioInicial",rodizioVeiculoMunicipio.getHrRodizioInicial()),
		    					Restrictions.or(
		    						Restrictions.sqlRestriction((rodizioVeiculoMunicipio.getHrRodizioFinal()==null?"1=1":"1=2")),
		    						Restrictions.le("hrRodizioInicial",rodizioVeiculoMunicipio.getHrRodizioFinal())
		    					)
							    					
						)    			
					)
			);
		 }	
	  }	
		return findByDetachedCriteria(dc).size()>0;
	}
	
	

	public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate hql = this.montaQueryPaginated(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false),hql.getCriteria());
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate hql = this.montaQueryPaginated(criteria);
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(),findDef.getCurrentPage(), findDef.getPageSize(),hql.getCriteria());
	}
	
	public SqlTemplate montaQueryPaginated(TypedFlatMap criteria){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map("
				+ "rvm.idRodizioVeiculoMunicipio as idRodizioVeiculoMunicipio, "
				+ "mun.nmMunicipio as municipio_nmMunicipio, "
				+ "uf.sgUnidadeFederativa as municipio_unidadeFederativa_sgUnidadeFederativa, "
				+ "pais.nmPais as municipio_unidadeFederativa_pais_nmPais, "
				+ "rvm.diaSemana as diaSemana, "
				+ "rvm.nrFinalPlaca as nrFinalPlaca, "
				+ "rvm.hrRodizioInicial as hrRodizioInicial, "
				+ "rvm.hrRodizioFinal as hrRodizioFinal, "
				+ "rvm.dtVigenciaInicial as dtVigenciaInicial, "
				+ "rvm.dtVigenciaFinal as dtVigenciaFinal)");
		
		hql.addFrom("RodizioVeiculoMunicipio rvm " 
				+ "join rvm.municipio mun "
				+ "join mun.unidadeFederativa uf "
				+ "join uf.pais pais ");
		
		
		hql.addCriteria("mun.idMunicipio", "=", criteria.getLong("municipio.idMunicipio"));
		hql.addCriteria("rvm.nrFinalPlaca", "=", criteria.getInteger("nrFinalPlaca"));
		hql.addCriteria("rvm.diaSemana", "=", criteria.getString("diaSemana"));
		hql.addCriteria("rvm.hrRodizioInicial", ">=", criteria.getTimeOfDay("hrRodizioInicial"));
		hql.addCriteria("rvm.hrRodizioFinal", "<=", criteria.getTimeOfDay("hrRodizioFinal"));
		hql.addCriteria("rvm.dtVigenciaInicial", ">=", criteria.getYearMonthDay("dtVigenciaInicial"));
		hql.addCriteria("rvm.dtVigenciaFinal", "<=", criteria.getYearMonthDay("dtVigenciaFinal"));
		
		
		hql.addOrderBy(OrderVarcharI18n.hqlOrder("pais.nmPais",LocaleContextHolder.getLocale()));
		hql.addOrderBy("uf.sgUnidadeFederativa");
		hql.addOrderBy("mun.nmMunicipio");
		hql.addOrderBy("rvm.diaSemana");
		hql.addOrderBy("rvm.nrFinalPlaca");
		hql.addOrderBy("rvm.hrRodizioInicial");
		hql.addOrderBy("rvm.dtVigenciaInicial");
		
		return hql;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}