package com.mercurio.lms.fretecarreteirocoletaentrega.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.DificuldadeColetaEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.RateioFreteCarreteiroCE;

public class DificuldadeColetaEntregaDAO extends BaseCrudDao<DificuldadeColetaEntrega, Long>{
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("cliente", FetchMode.EAGER);		
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected Class getPersistentClass() {
		return DificuldadeColetaEntrega.class;
	}
	
	
	public void storeAll(List<RateioFreteCarreteiroCE> list) {
		getAdsmHibernateTemplate().saveOrUpdateAll(list);
	}
	
	public DificuldadeColetaEntrega findByCliente(Long idCliente) {
		StringBuilder hql = new StringBuilder();					
		
		hql.append("SELECT di FROM "+ DificuldadeColetaEntrega.class.getName() + " as di  WHERE di.cliente.idCliente =  ? ");
				
		@SuppressWarnings("unchecked")
		List<DificuldadeColetaEntrega> list = getAdsmHibernateTemplate().find(hql.toString(),  new Object[] {idCliente});		
		if(list.isEmpty()){
			DificuldadeColetaEntrega df = new DificuldadeColetaEntrega();
			df.setNrFatorColeta(1);
			df.setNrFatorEntrega(1);
			return df;
		}
		return list.get(0);
	}
}