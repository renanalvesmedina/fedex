package com.mercurio.lms.vendas.model.dao;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.vendas.model.CarteiraVendas;

/**
 * 
 * @author Vagner Huzalo
 * @spring.bean	
 */
public class CarteiraVendasDAO extends BaseCrudDao<CarteiraVendas, java.lang.Long> {

	
	@Override
	protected Class getPersistentClass() {
		return CarteiraVendas.class;
	}
	
	public CarteiraVendas findById(java.lang.Long id){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("cv");
		hql.addInnerJoin(CarteiraVendas.class.getName(),"cv");
		hql.addInnerJoin("fetch cv.usuario","u");
		hql.addLeftOuterJoin("fetch cv.pendencia","p");
		
		hql.addCriteria("cv.id", "=" , id);
		
		return (CarteiraVendas) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}
	
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		ResultSetPage<CarteiraVendas> rsp = super.findPaginated(criteria, findDef);
		
		Map fetchModes = new HashMap();
		fetchModes.put("usuario", FetchMode.SELECT);
		
		initilizeResultSetPage(rsp, fetchModes);
		
		FilterResultSetPage filter = new FilterResultSetPage(rsp){
			@Override
			public Map filterItem(Object item) {
				CarteiraVendas carteiraVendas = (CarteiraVendas)item;
				Map row = new HashMap();
				row.put("idCarteiraVendas", carteiraVendas.getIdCarteiraVendas());
				row.put("numeroLote", carteiraVendas.getIdCarteiraVendas());
				row.put("nmUsuario", carteiraVendas.getUsuario().getNmUsuario());
				row.put("dtInicioLote", carteiraVendas.getDtInicioLote());
				row.put("tpSituacaoAprovacao", carteiraVendas.getTpSituacaoAprovacao());
				row.put("blEfetivadoNivel1", carteiraVendas.getBlEfetivadoNivel1());
				row.put("blEfetivadoNivel2", carteiraVendas.getBlEfetivadoNivel2());
				return row;
			}
			
		};
		return (ResultSetPage) filter.doFilter();
	}
	
	
	
	
	
}
