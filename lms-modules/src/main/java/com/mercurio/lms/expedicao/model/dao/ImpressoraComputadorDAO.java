package com.mercurio.lms.expedicao.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.expedicao.model.ImpressoraComputador;

public class ImpressoraComputadorDAO extends BaseCrudDao<ImpressoraComputador, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ImpressoraComputador.class;
	}

	public void removeByImpressora(final Long idImpressora) {
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = null;
				String hqlDelete = " delete ImpressoraComputador ic " +
								   " where ic.impressora.idImpressora = :idImpressora";
				query = session.createQuery(hqlDelete);
				query.setLong("idImpressora", idImpressora);
				
				query.executeUpdate();
		
				return null;
			}
		});
	}

	public void removeByImpressoras(final List<Long> ids) {
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = null;
				String hqlDelete = " delete ImpressoraComputador ic " +
								   " where ic.impressora.idImpressora in (:ids)";
				query = session.createQuery(hqlDelete);
				query.setParameterList("ids", ids);
				
				query.executeUpdate();
		
				return null;
			}
		});
	}

	public List findValidateMatricialUnica(Long idImpressora, Long idImpressoraComputador) {
		Map<String, Object> namedParams = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" select ic from ImpressoraComputador ic ");
		hql.append("  where ic.impressora.idImpressora = :idImpressora ");
		if(idImpressoraComputador != null) {
			hql.append("    and ic.idImpressoraComputador <> :idImpressoraComputador ");
			namedParams.put("idImpressoraComputador", idImpressoraComputador);
		}
		namedParams.put("idImpressora", idImpressora);
		List result = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), namedParams);
		
		return result;
	}

	public List findValidateMacTpImpressora(Long idImpressoraComputador, List<String> tpImpressora, String dsMac) {
		Map<String, Object> namedParams = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" select ic from ImpressoraComputador ic ");
		hql.append("  where ic.impressora.tpImpressora in (:tpImpressora) ");
		hql.append("  and ic.dsMac = :dsMac ");
		if(idImpressoraComputador != null) {
			hql.append("    and ic.idImpressoraComputador <> :idImpressoraComputador ");
			namedParams.put("idImpressoraComputador", idImpressoraComputador);
		}
		namedParams.put("tpImpressora", tpImpressora);
		namedParams.put("dsMac", dsMac);
		List result = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), namedParams);
		
		return result;
	}

	public List findListByCriteria(Map criterions, List order) {
		order = new ArrayList(1);
		order.add("dsLocalizacao");
		return super.findListByCriteria(criterions, order);
	}

	protected void initFindByIdLazyProperties(Map arg0) {
		arg0.put("impressora", FetchMode.JOIN);
		arg0.put("impressora.filial", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map arg0) {
		arg0.put("impressora", FetchMode.JOIN);
		arg0.put("impressora.filial", FetchMode.JOIN);
	}
	
	public boolean findValidateDWSBalanca(String dsMac, String tpImpressora){
		Boolean validaDispositivo = false;		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("ic");
		
		sql.addInnerJoin(ImpressoraComputador.class.getName(), "ic");
		sql.addInnerJoin("ic.impressora", "i");
		
		sql.addCriteria("i.tpImpressora", "=", tpImpressora);
		sql.addCriteria("ic.dsMac", "=", dsMac);
		
		ImpressoraComputador ic = (ImpressoraComputador)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		
		if(ic != null){
			validaDispositivo = true;
		}
		
		return validaDispositivo;
	}
}