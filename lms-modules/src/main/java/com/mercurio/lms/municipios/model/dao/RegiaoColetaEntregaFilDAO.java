package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.RegiaoColetaEntregaFil;
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
public class RegiaoColetaEntregaFilDAO extends BaseCrudDao<RegiaoColetaEntregaFil, Long>
{

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate hql = this.montaQueryPaginated(criteria);
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), hql.getCriteria());
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate hql = this.montaQueryPaginated(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false), hql.getCriteria());
	}
	
	public SqlTemplate montaQueryPaginated(TypedFlatMap criteria){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map("
				+"filial.sgFilial as filial_sgFilial, "
				+"pessoa.nmFantasia as filial_pessoa_nmFantasia, "
				+"rcef.dsRegiaoColetaEntregaFil as dsRegiaoColetaEntregaFil, "
				+"rcef.dtVigenciaInicial as dtVigenciaInicial, "
				+"rcef.idRegiaoColetaEntregaFil as idRegiaoColetaEntregaFil, "
				+"rcef.dtVigenciaFinal as dtVigenciaFinal )");
		
		hql.addFrom("RegiaoColetaEntregaFil rcef " +
				"join rcef.filial filial " +
				"join filial.pessoa pessoa ");
		
		hql.addCriteria("filial.idFilial","=", criteria.getLong("filial.idFilial"));
		if (criteria.getString("dsRegiaoColetaEntregaFil") != null) {
			hql.addCriteria("upper(rcef.dsRegiaoColetaEntregaFil)","like", criteria.getString("dsRegiaoColetaEntregaFil").toUpperCase());
		}
		hql.addCriteria("rcef.dtVigenciaInicial",">=", criteria.getYearMonthDay("dtVigenciaInicial"));
		hql.addCriteria("rcef.dtVigenciaFinal","<=", criteria.getYearMonthDay("dtVigenciaFinal"));
		
		hql.addOrderBy("filial.sgFilial");
		hql.addOrderBy("rcef.dsRegiaoColetaEntregaFil");
		hql.addOrderBy("rcef.dtVigenciaInicial");
		
		return hql;
	}

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("filial", FetchMode.JOIN);
		fetchModes.put("filial.pessoa", FetchMode.JOIN);
        fetchModes.put("regiaoFilialRotaColEnts", FetchMode.SELECT);
	}

	protected void initFindLookupLazyProperties(Map fetchModes) {
		fetchModes.put("filial", FetchMode.JOIN);
		fetchModes.put("filial.pessoa", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("filial", FetchMode.JOIN);
		fetchModes.put("filial.pessoa", FetchMode.JOIN);
	}
	
	public boolean findRegiaoColetaEntregaVigente(RegiaoColetaEntregaFil regiaoColetaEntregaFil){
		DetachedCriteria dc = createDetachedCriteria();
		if(regiaoColetaEntregaFil.getIdRegiaoColetaEntregaFil() != null){
			dc.add(Restrictions.ne("idRegiaoColetaEntregaFil",regiaoColetaEntregaFil.getIdRegiaoColetaEntregaFil()));
		}	
		dc.add(Restrictions.eq("filial.idFilial",regiaoColetaEntregaFil.getFilial().getIdFilial()));
		dc.add(Restrictions.ilike("dsRegiaoColetaEntregaFil",regiaoColetaEntregaFil.getDsRegiaoColetaEntregaFil(),MatchMode.EXACT));
		dc = JTVigenciaUtils.getDetachedVigencia(dc,regiaoColetaEntregaFil.getDtVigenciaInicial(),regiaoColetaEntregaFil.getDtVigenciaFinal());
		
		return findByDetachedCriteria(dc).size()>0;
	}
	
	
	/**
	 * Consulta as regioes de coleta/entrega da filial vigentes na data atual
	 * @param criteria
	 * @return
	 */
	public List consultaRegiaoColetaEntregaFilialVigentes(TypedFlatMap criteria, boolean onlyVigentes){
		
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());		
		
		if(criteria != null) {
			if (criteria.getLong("filial.idFilial")!=null) {
				dc.add(Restrictions.eq("filial.idFilial", criteria.getLong("filial.idFilial")));
			}
		}
		
		if (onlyVigentes) {
			dc.add( Restrictions.ge("dtVigenciaFinal", JTDateTimeUtils.getDataAtual()));			
		}
		
		dc.addOrder(Order.asc("dsRegiaoColetaEntregaFil"));
		
		return findByDetachedCriteria(dc);
	}
	
	//verifica se a regiao está vigente
	public boolean findVigenciaRegiaoColetaEntrega(Long idRegiaoColetaEntregaFil){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("idRegiaoColetaEntregaFil", idRegiaoColetaEntregaFil));
		dc.add( Restrictions.ge("dtVigenciaFinal", JTDateTimeUtils.getDataAtual()));
		dc.add( Restrictions.le("dtVigenciaInicial", JTDateTimeUtils.getDataAtual()));
		
		return findByDetachedCriteria(dc).size()>0;
	}
	
	public boolean findIntervaloRegiaoColetaEntrega(Long idRegiaoColetaEntregaFil, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("idRegiaoColetaEntregaFil", idRegiaoColetaEntregaFil));
		dc.add( Restrictions.ge("dtVigenciaFinal", dtVigenciaFinal));
		dc.add( Restrictions.le("dtVigenciaInicial", dtVigenciaInicial));
		
		return findByDetachedCriteria(dc).size()>0;
	}
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return RegiaoColetaEntregaFil.class;
    }

   


}